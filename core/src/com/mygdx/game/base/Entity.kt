package com.mygdx.game.base

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import kotlin.math.abs


/**
 * Entity - базовый класс для игровых сущностей.
 * Он предоставляет основные методы и свойства, общие для всех сущностей.
 *
 * @param map карта, на которой размещена сущность.
 * @param x координата X сущности на карте.
 * @param y координата Y сущности на карте.
 */
abstract class Entity(
    val map: TiledMap,
    spawnPoint: RectangleMapObject,
    sprite: Array<AtlasRegion>
) : Sprite(atlasParts=sprite) {

    protected var hp = 0
    var velocity = Vector2()
    private val rectPool: Pool<Rectangle> = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle {
            return Rectangle()
        }
    }
    private val tiles: Array<Rectangle> = Array<Rectangle>()

    var state = State.Standing
    var stateTime = 0f
    var texture: Texture? = null
    var facesRight = true
    var grounded = false
    var screen: BaseScreen? = null

    var stand: Animation<TextureRegion>? = null
    var walk: Animation<TextureRegion>? = null
    var jump: Animation<TextureRegion>? = null
    var attack: Animation<TextureRegion>? = null

    /**
     * Перечисление состояний сущности.
     */
    enum class State {
        Standing,
        Walking,
        Jumping,
        Attacking
    }

    override fun draw(batch: SpriteBatch) {
        batch.begin()
        val frame = when (state) {
            State.Standing-> stand?.getKeyFrame(stateTime)
            State.Walking -> walk?.getKeyFrame(stateTime)
            State.Jumping -> jump?.getKeyFrame (stateTime)
            State.Attacking -> attack?.getKeyFrame(stateTime)
        }
        var face = getWidth()
        var posx = getLeft()
        if (!facesRight) { face = -face; posx = getRight() }
        batch.draw(
            frame,
            posx, pos.y,
            halfWidth, halfHeight,
            face, getHeight(),
            scale, scale,
            angle
        )
        batch.end()
    }

    init{
        pos.set(spawnPoint.rectangle.x/16f , spawnPoint.rectangle.y/16f)
    }

    /**
     * Обновляет состояние сущности.
     *
     * @param delta время в секундах, прошедшее с последнего обновления.
     */
    override fun update(delta: Float) {
        if (delta == 0f) return
        if (screen == null) return
        var deltaTime = delta

        if (delta > 0.1f) deltaTime = 0.1f

        stateTime += deltaTime

        // apply gravity if we are falling
        velocity.add(0f, GRAVITY)

//        if (velocity.x == 0f && grounded){ state = State.Standing }
//
//        if (goLeft)
//            velocity.add(-deltaTime, 0f)
//        else if (goRight)
//            velocity.add(deltaTime, 0f)
//        else
//            velocity.x *= DAMPING

        // clamp the velocity to the maximum, x-axis only
        velocity.x =
            MathUtils.clamp(velocity.x, - MAX_VELOCITY, MAX_VELOCITY)

        if (state == State.Attacking && stateTime > ATTACK_TIME) {
            stateTime = 0f
            state = if (grounded) {
                State.Jumping
            } else State.Standing
        }

        // If the velocity is < 1, set it to 0 and set state to Standing
        if (abs(velocity.x) < 1) {
            velocity.x = 0f
            if (grounded && state != State.Attacking) state = State.Standing
        }

        // multiply by delta time so we know how far we go in this frame
        velocity.scl(deltaTime)

        // perform collision detection & response, on each axis, separately
        // if the this is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        val rect = createCollisionRect()
        var startX: Int
        var startY: Int
        var endX: Int
        var endY: Int
        if (velocity.x > 0) {
            endX = (pos.x + halfWidth + velocity.x).toInt()
            startX = endX
        } else {
            startX = (pos.x + velocity.x).toInt()
            endX = startX
        }
        startY = pos.y.toInt()
        endY = (pos.y + getHeight()).toInt()
        getTiles(startX, startY, endX, endY, tiles)
        rect.x += velocity.x
        for (tile in tiles) {
            if (rect.overlaps(tile)) {
                velocity.x = 0f
                break
            }
        }
        rect.x = pos.x

        // if the this is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom
        if (velocity.y > 0) {
            endY = (pos.y + getHeight() + velocity.y).toInt()
            startY = endY
        } else {
            endY = (pos.y + velocity.y).toInt()
            startY = endY
        }
        startX = pos.x.toInt()
        endX = (pos.x + getWidth()).toInt()
        getTiles(startX, startY, endX, endY, tiles)
        rect.y += velocity.y
        for (tile in tiles) {
            if (rect.overlaps(tile)) {
                // we actually reset the koala y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing :)
                if (velocity.y > 0) {
                    pos.y = tile.y - getHeight()
                    // we hit a block jumping upwards, let's destroy it!
//                    val layer = map.layers["walls"] as TiledMapTileLayer
//                    layer.setCell(tile.x.toInt(), tile.y.toInt(), null)
                } else {
                    pos.y = tile.y + tile.height
                    // if we hit the ground, mark us as grounded so we can jump
                    grounded = true
                }
                velocity.y = 0f
                break
            }
        }
        rectPool.free(rect)

        // unscale the velocity by the inverse delta time and set
        // the latest position
        pos.add(velocity)
        velocity.scl(1 / deltaTime)

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        velocity.x *= DAMPING
    }

    /**
     * Уничтожает сущность.
     */
    override fun destroy() {
        super.destroy()
    }

    /** получает от карты все тайлы вокруг игрока для обработки коллизий, нахождения триггеров */
    private fun getTiles(startX: Int, startY: Int, endX: Int, endY: Int, tiles: Array<Rectangle>) {
        val layer = map.layers?.get("Ground") as TiledMapTileLayer
        rectPool.freeAll(tiles)
        tiles.clear()
        for (y in startY..endY) {
            for (x in startX..endX) {
                val cell = layer.getCell(x, y)
                if (cell != null) {
                    val rect = rectPool.obtain()
                    rect[x.toFloat(), y.toFloat(), 1f] = 1f
                    tiles.add(rect)
                }
            }
        }
    }

    /** метод создания коллизии персонажа */
    fun createCollisionRect(): Rectangle {
        val rect: Rectangle = rectPool.obtain()
        rect.set(pos.x, pos.y, getWidth(), getHeight())
        return rect
    }

    companion object{
        const val DAMPING = 0.87f
        const val ATTACK_TIME = 1.65f
        const val MAX_VELOCITY = 10f
        const val JUMP_VELOCITY = 40f
        const val GRAVITY = -2.5f
    }

}