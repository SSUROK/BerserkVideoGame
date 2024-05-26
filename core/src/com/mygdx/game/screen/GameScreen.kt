package com.mygdx.game.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.base.BaseIntractable
import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Player
import com.mygdx.game.objects.Warp


/**
 * FirstScreen - экран начала игры.
 * Отображает начало игры, загружает карту и управляет игровым процессом.
 * Наследуется от класса BaseScreen.
 */
class GameScreen(game: Game, private val mapFilePath: String) : BaseScreen(game) {

    private var textTriggers : MapObjects? = null
    private var chestTriggers : MapObjects? = null
    private var activeChest : RectangleMapObject? = null
    private var map: TiledMap? = null
    private var playerInWarp = false
    private lateinit var spawnPoint: RectangleMapObject
    private val mapLayers = mutableMapOf<String, MapLayer?>()
    private val mapIntractableObjects = mutableListOf<BaseIntractable>()
    private val mapStaticObjects = mutableListOf<BaseIntractable>()
    private lateinit var activeWarpPoint: MapObject
    private val font = BitmapFont()

    /**
     * Вызывается при создании экрана. Здесь происходит инициализация всех необходимых
     * элементов, таких как камера, игрок, фон и т.д.
     */
    override fun show() {
        super.show()
        map = TmxMapLoader().load(mapFilePath)
        renderer = OrthogonalTiledMapRenderer(map, 1/16f)

        getLayers()
        spawnPlayer()

        batch = SpriteBatch()

        camera = OrthographicCamera()
        camera!!.setToOrtho(false, 30f, 20f)
        camera!!.update()
        camera!!.position.y = player!!.pos.y + 5f
        font.data.setScale(5f)
        font.color = Color.BLACK
    }

    /**
     * Вызывается при отрисовке экрана. Здесь происходит отрисовка всех элементов
     * и обработка логики взаимодействия с игровыми объектами.
     */
    override fun render(delta: Float) {
        super.render(delta)
        batch?.begin()
        for (o in mapIntractableObjects){
            o.render(player!!, font, batch!!)
        }
        batch?.end()
    }

    private fun getLayers(){
        mapLayers["poi"] = map?.layers?.get("POI")
        spawnPoint = mapLayers["poi"]!!.objects.get("spawn") as RectangleMapObject
        mapLayers["text"] = map?.layers?.get("TextTriggers")
        mapLayers["chest"] = map?.layers?.get("ChestTriggers")
        mapLayers["warps"] = map?.layers?.get("WarpPoints")
        mapLayers["hazards"] = map?.layers?.get("Hazards")

//        for (poi in mapLayers["poi"]!!.objects){
//
//        }

        for (w in mapLayers["warps"]!!.objects){
            mapIntractableObjects.add(Warp(w, game))
        }
    }


    private fun spawnPlayer(){
        player = Player(map!!, spawnPoint)
        player?.screen = this
    }

    /**
     * Обрабатывает событие нажатия на экране.
     * Здесь обрабатывается открытие сундука и активация текстового сообщения о нападении.
     *
     * @param screenX x-координата нажатия на экране.
     * @param screenY y-координата нажатия на экране.
     * @param pointer указатель события.
     * @param button кнопка, которая была нажата (левая, правая и т.д.).
     */
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val xTouch = screenX/SCREEN_WIDTH
        val yTouch = screenY/SCREEN_HEIGHT
        for (o in mapIntractableObjects){
            o.interactBtn.touchDown(Vector2(screenX.toFloat(), screenY.toFloat()), pointer, button)
        }
        player?.touchDown(Vector2(xTouch, yTouch), pointer, button)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        for (o in mapIntractableObjects){
            o.interactBtn.touchUp(Vector2(screenX.toFloat(), screenY.toFloat()), pointer, button)
        }
        player?.touchUp(Vector2(screenX.toFloat(), screenY.toFloat()), pointer, button)
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> {
                player?.goLeft = true
            }
            Input.Keys.D, Input.Keys.RIGHT -> {
                player?.goRight = true
            }
            Input.Keys.SPACE, Input.Keys.UP -> {
                player?.jump()
            }
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> {
                player?.goLeft = false
            }

            Input.Keys.D, Input.Keys.RIGHT -> {
                player?.goRight = false
            }
        }
        return false
    }
}