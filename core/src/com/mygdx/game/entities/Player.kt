package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.MapProperties
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
import kotlin.math.abs


class Player(map: TiledMap, x: Float, y: Float): Entity(map, x, y) {

    public var weaponEquipped = false

    private val rectPool: Pool<Rectangle> = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle {
            return Rectangle()
        }
    }
    private val tiles: Array<Rectangle> = Array<Rectangle>()
    init{
        // load the koala frames, split them, and assign them to Animations
        texture = Texture("data/textures/character.png")
        val s = TextureRegion.split(texture, 32, 32)
//        regions = s[0]
        stand = Animation(0.15f, s[0][0], s[0][1], s[0][2], s[0][3], s[0][4], s[0][5],
            s[0][6], s[0][7], s[0][8], s[0][9])
        jump = Animation(0f, s[0][0])
        walk = Animation(0.15f, s[2][0], s[2][1], s[2][2], s[2][3], s[2][4], s[2][5],
            s[2][6], s[2][7], s[2][8], s[2][9])
        attack = Animation(0.15f, s[3][0], s[3][1], s[3][2], s[3][3], s[3][4], s[3][5],
            s[3][6], s[3][7], s[3][8], s[3][9], s[3][0])
        walk?.playMode = Animation.PlayMode.LOOP_PINGPONG
        stand?.playMode = Animation.PlayMode.LOOP_PINGPONG
        attack?.playMode = Animation.PlayMode.LOOP_PINGPONG


//         figure out the width and height of the koala for collision
//         detection and rendering by converting a koala frames pixel
//         size into world units (1 unit == 16 pixels)
        WIDTH = 1 / 16f * s[0][0].regionWidth.toFloat()
        HEIGHT = 1 / 16f * s[0][0].regionHeight.toFloat()
    }

    override fun render(deltaTime: Float, renderer: OrthogonalTiledMapRenderer) {
        // based on the koala state, get the animation frame
        var frame: TextureRegion? = null
        frame = when (state) {
            State.Standing-> stand?.getKeyFrame(stateTime)
            State.Walking -> walk?.getKeyFrame(stateTime)
            State.Jumping -> jump?.getKeyFrame (stateTime)
            State.Attacking -> attack?.getKeyFrame(stateTime)
        }
        // draw the koala, depending on the current velocity
        // on the x-axis, draw the koala facing either right
        // or left
        val batch: Batch =  renderer.batch
        batch.begin()
        if (facesRight) {
            batch.draw(frame, position.x, position.y, WIDTH, HEIGHT)
        } else {
            batch.draw(frame, position.x + WIDTH, position.y, -WIDTH, HEIGHT)
        }
        batch.end()
    }

    override fun update(delta: Float) {
        super.update(delta)
        if (delta == 0f) return
        if (screen == null) return
        var deltaTime = delta

        if (delta > 0.1f) deltaTime = 0.1f

        this.stateTime += deltaTime

        // check input and apply to velocity & state
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || screen!!.isTouched(0.25f, 0.75f, endY=0.5f)) && this.grounded) {
            jump()
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.F) || screen!!.isTouched(0.25f, 0.75f, 0.5f))) {
            attack()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || screen!!.isTouched(
                0f, 0.25f)) {
            moveLeft()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || screen!!.isTouched(
                0.75f, 1f)) {
            moveRight()
        }

//        if (Gdx.input.isKeyJustPressed(Keys.B)) debug = !debug

        // apply gravity if we are falling
        this.velocity.add(0f, GRAVITY)


        // clamp the velocity to the maximum, x-axis only
        this.velocity.x =
            MathUtils.clamp(this.velocity.x, -this.MAX_VELOCITY, this.MAX_VELOCITY)

        if (this.state == State.Attacking && this.stateTime > ATTACK_TIME) {
            this.stateTime = 0f
            if (this.grounded) {
                this.state = State.Jumping
            } else this.state = State.Standing
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
            endX = (this.position.x + this.WIDTH + this.velocity.x).toInt()
            startX = endX
        } else {
            endX = (this.position.x + this.velocity.x).toInt()
            startX = endX
        }
        startY = this.position.y.toInt()
        endY = (this.position.y - this.HEIGHT).toInt()
        getTiles(startX, startY, endX, endY, tiles)
        playerRect.x += this.velocity.x
        for (tile in tiles) {
            if (playerRect.overlaps(tile)) {
                this.velocity.x = 0f
                break
            }
        }
        playerRect.x = this.position.x

        // if the this is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom
        if (this.velocity.y > 0) {
            endY = (this.position.y + this.HEIGHT + this.velocity.y).toInt()
            startY = endY
        } else {
            endY = (this.position.y + this.velocity.y).toInt()
            startY = endY
        }
        startX = this.position.x.toInt()
        endX = (this.position.x + this.WIDTH).toInt()
        getTiles(startX, startY, endX, endY, tiles)
        playerRect.y += velocity.y
        for (tile in tiles) {
            if (playerRect.overlaps(tile)) {
                // we actually reset the koala y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing :)
                if (velocity.y > 0) {
                    position.y = tile.y - HEIGHT
                    // we hit a block jumping upwards, let's destroy it!
                    val layer = map.layers["walls"] as TiledMapTileLayer
                    layer.setCell(tile.x.toInt(), tile.y.toInt(), null)
                } else {
                    position.y = tile.y + tile.height
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
        this.position.add(this.velocity)
        this.velocity.scl(1 / deltaTime)

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        this.velocity.x *= this.DAMPING
    }

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

    fun keyDown(keycode: Int){
        when (keycode) {
            Input.Keys.A or Input.Keys.LEFT -> {moveLeft()}
            Input.Keys.D or Input.Keys.RIGHT -> {moveRight()}
            Input.Keys.SPACE or Input.Keys.UP -> {jump()}
        }
    }

    fun touchDown(v: Vector2, pointer: Int, button: Int){
        when (v){
            Vector2(0f, 0.25f) -> {moveLeft()}
            Vector2(0.25f, 0.75f) -> {moveRight()}
            Vector2(0.75f, 1f) -> {jump()}
        }
    }

    fun jump(){
        this.velocity.y += this.JUMP_VELOCITY
        this.state = State.Jumping
        this.grounded = false
    }

    fun moveLeft(){
        this.velocity.x = -this.MAX_VELOCITY
        if (this.grounded) this.state = State.Walking
        this.facesRight = false
    }

    fun moveRight(){
        this.velocity.x = this.MAX_VELOCITY
        if (this.grounded) this.state = State.Walking
        this.facesRight = true
    }

    fun attack(){
        if (this.weaponEquipped) {
            this.velocity.x = 0f
            println("Attacking")
            this.state = State.Attacking
        }
    }

    fun createCollisionRect(): Rectangle {
        val playerRect: Rectangle = rectPool.obtain()
        playerRect.set(this.position.x, this.position.y, this.WIDTH, this.HEIGHT)
        return playerRect
    }
}