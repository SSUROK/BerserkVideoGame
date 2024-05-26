package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.mygdx.game.base.Entity
import com.mygdx.game.math.atlas
import com.mygdx.game.math.charAtlas
import kotlin.math.abs


/**
 * Player - класс, представляющий игрока.
 * Этот класс расширяет базовый класс Entity и предоставляет функциональность для управления игровым персонажем.
 *
 * @param map карта, на которой размещен игрок.
 * @param x координата X игрока на карте.
 * @param y координата Y игрока на карте.
 */
class Player(map: TiledMap, spawnPoint: RectangleMapObject): Entity(
    map,
    spawnPoint,
    charAtlas.regions
) {

    public var weaponEquipped = false
    var goLeft = false
    var goRight = false

    private val rectPool: Pool<Rectangle> = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle {
            return Rectangle()
        }
    }
    private val tiles: Array<Rectangle> = Array<Rectangle>()

    /** Инициализация игрового персонажа. Устанавливается атлас текстур, затем создаются основные
     * анимации персонажа. */
    init{
        stand = Animation(0.15f, charAtlas.findRegions("idle"), Animation.PlayMode.LOOP_PINGPONG)
        jump = Animation(0f, charAtlas.findRegions("jump"))
        walk = Animation(0.15f, charAtlas.findRegions("walk"), Animation.PlayMode.LOOP_PINGPONG)
        attack = Animation(0.15f, charAtlas.findRegions("attack"), Animation.PlayMode.LOOP_PINGPONG)


//         figure out the width and height of the koala for collision
//         detection and rendering by converting a koala frames pixel
//         size into world units (1 unit == 16 pixels)
        setHeightProportion(2.5f)
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

    /** метод обновления состояния игрового персонажа со временем. реализует управление персонажем.
     *  должен вызываться из метода обновления экрана */
    override fun update(delta: Float) {
        super.update(delta)
        if (delta == 0f) return
        if (screen == null) return
        var deltaTime = delta

        if (delta > 0.1f) deltaTime = 0.1f

        stateTime += deltaTime

        if (goLeft) {
            moveLeft()
        }

        if (goRight) {
            moveRight()
        }

//        if (Gdx.input.isKeyJustPressed(Keys.B)) debug = !debug

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
        if (abs(this.velocity.x) < 1) {
            this.velocity.x = 0f
            if (this.grounded && this.state != State.Attacking) this.state = State.Standing
        }

        // multiply by delta time so we know how far we go in this frame
        this.velocity.scl(deltaTime)

        // perform collision detection & response, on each axis, separately
        // if the this is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        val playerRect = createCollisionRect()
        var startX: Int
        var startY: Int
        var endX: Int
        var endY: Int
        if (this.velocity.x > 0) {
            endX = (pos.x + halfWidth + velocity.x).toInt()
            startX = endX
        } else {
            startX = (pos.x + velocity.x).toInt()
            endX = startX
        }
        startY = pos.y.toInt()
        endY = (pos.y + getHeight()).toInt()
        getTiles(startX, startY, endX, endY, tiles)
        playerRect.x += velocity.x
        for (tile in tiles) {
            if (playerRect.overlaps(tile)) {
                velocity.x = 0f
                break
            }
        }
        playerRect.x = pos.x

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
        playerRect.y += velocity.y
        for (tile in tiles) {
            if (playerRect.overlaps(tile)) {
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
        rectPool.free(playerRect)

        // unscale the velocity by the inverse delta time and set
        // the latest position
        pos.add(this.velocity)
        velocity.scl(1 / deltaTime)

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        this.velocity.x *= this.DAMPING
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

    /** метод для прыжеов персонажа */
    fun jump(){
        if (!this.grounded) { return }
        this.velocity.y += this.JUMP_VELOCITY
        this.state = State.Jumping
        this.grounded = false
    }

    /** метод движения налево */
    private fun moveLeft(){
        velocity.x = -MAX_VELOCITY
        if (grounded) state = State.Walking
        facesRight = false
    }
    /** метод движения направо */
    private fun moveRight(){
        velocity.x = MAX_VELOCITY
        if (grounded) state = State.Walking
        facesRight = true
    }



    /** метод атаки персонажа */
    fun attack(){
        if (this.weaponEquipped) {
            this.velocity.x = 0f
            println("Attacking")
            this.state = State.Attacking
        }
    }

    /** метод создания коллизии персонажа */
    fun createCollisionRect(): Rectangle {
        val playerRect: Rectangle = rectPool.obtain()
        playerRect.set(pos.x, pos.y, getWidth(), getHeight())
        return playerRect
    }

    override fun touchDown(touch: Vector2, pointer: Int, button: Int): Boolean {
        when {
            touch.x in 0f .. 0.25f -> {goLeft = true}
            touch.x in 0.75f .. 1f -> {goRight = true}
            touch.x in 0.25f .. 0.75f && touch.y in 0f.. 0.5f -> jump()
            touch.x in 0.25f .. 0.75f && touch.y in 0.5f.. 1f -> attack()
        }
        return false
    }

    override fun touchUp(touch: Vector2, pointer: Int, button: Int): Boolean {
        goLeft = false
        goRight = false
        return false
    }
}