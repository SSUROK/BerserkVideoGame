package com.mygdx.game.base

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.ScreenUtils
import com.mygdx.game.entities.Player


abstract class BaseScreen (
    val game: Game
): Screen, InputAdapter() {
    var map: TiledMap? = null
    var renderer: OrthogonalTiledMapRenderer? = null
    var camera: OrthographicCamera? = null
    var player: Player? = null

    val SCREEN_WIDTH = Gdx.graphics.width.toFloat()
    val SCREEN_HEIGHT = Gdx.graphics.height.toFloat()

    var title: TextureRegion? = null
    var batch: SpriteBatch? = null
    var time = 0f


    private var touch: Vector2? = null

    override fun show() {
        Gdx.input.inputProcessor = this
    }

    override fun resume() {}

    override fun render(delta: Float) {
        // clear the screen
        ScreenUtils.clear(0.7f, 0.7f, 1.0f, 1f)

        // get the delta time
        val deltaTime = Gdx.graphics.deltaTime

        // update the koala (process input, collision detection, position update)
        player?.update(deltaTime)

        // let the camera follow the koala, x-axis only
        camera?.position?.x = player?.position?.x
        camera?.update()

        // set the TiledMapRenderer view based on what the
        // camera sees, and render the map
        renderer?.setView(camera)
        renderer?.render()

        // render the koala
        player?.render(deltaTime, renderer!!)

    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    open fun update(deltaTime: Float) {}

    override fun dispose() {}
    override fun hide() {
        Gdx.app.debug("Berserk", "dispose ${this.javaClass.name}");
        batch?.dispose();
        title?.texture?.dispose();
    }

    //    override fun resize(width: Int, height: Int) {
//        screenBounds!!.setSize(width.toFloat(), height.toFloat())
//        screenBounds!!.setLeft(0f)
//        screenBounds!!.setBottom(0f)
//
//        val aspect = width / height.toFloat()
//        worldBounds!!.setHeight(1f)
//        worldBounds!!.setWidth(1f * aspect)
//        MatrixUtils.calcTransitionMatrix(worldToGl!!, worldBounds!!, glBounds!!)
//        batch!!.setProjectionMatrix(worldToGl)
//        MatrixUtils.calcTransitionMatrix(screenToWorld!!, screenBounds!!, worldBounds!!)
//        resize(worldBounds!!)
//    }

//    open fun resize(worldBounds: Rect) {
//        println(("resize width = " + worldBounds.getWidth()).toString() + " height = " + worldBounds.getHeight())
//    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
//        player?.attack(screenX, screenY, pointer)
        return super.touchDragged(screenX, screenY, pointer)
    }

    fun isTouched(startX: Float, endX: Float, startY: Float = 0f, endY: Float = 1f): Boolean {
        for (i in 0..1) {
            val y = Gdx.input.getY(i) / Gdx.graphics.height.toFloat()
            val x = Gdx.input.getX(i) / Gdx.graphics.width.toFloat()
            if (Gdx.input.isTouched(i) && x >= startX && x <= endX && startY <= y && y <= endY) {
                return true
            }
        }
        return false
    }
}