package com.mygdx.game.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
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
import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Player


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
    private var poiLayer: MapLayer? = null
    private var warpPoints: MapObjects? = null
    private var playerInWarp = false
    private lateinit var activeWarpPoint: MapObject
    val font = BitmapFont()

    /**
     * Вызывается при создании экрана. Здесь происходит инициализация всех необходимых
     * элементов, таких как камера, игрок, фон и т.д.
     */
    override fun show() {
        super.show()
        map = TmxMapLoader().load(mapFilePath)
        renderer = OrthogonalTiledMapRenderer(map, 1/16f)
        poiLayer = map?.getLayers()?.get("POI")
        val spawnPoint = poiLayer?.objects?.get("spawn") as RectangleMapObject
        textTriggers = map?.getLayers()?.get("TextTriggers")?.objects
        chestTriggers = map?.getLayers()?.get("ChestTriggers")?.objects
        warpPoints = map?.getLayers()?.get("WarpPoints")?.objects

        val entitySpawns = poiLayer?.objects
        for (obj in entitySpawns!!) {
            if (obj.name == "entitySpawn") {
                val entity = obj.properties.get("entity")

            }
        }

        batch = SpriteBatch()

        player = Player(map!!, spawnPoint.rectangle.x/16f, spawnPoint.rectangle.y/16f)
        player?.screen = this
        camera = OrthographicCamera()
        camera!!.setToOrtho(false, 30f, 20f)
        camera!!.update()
        camera!!.position.y = player!!.position.y + 5f
        font.data.setScale(5f)
        font.color = Color.BLACK
    }

    /**
     * Вызывается при отрисовке экрана. Здесь происходит отрисовка всех элементов
     * и обработка логики взаимодействия с игровыми объектами.
     */
    override fun render(delta: Float) {
        super.render(delta)
        val playerRect = player?.createCollisionRect()
        if (textTriggers != null) {
            for (obj in textTriggers!!) {
                val or = (obj as RectangleMapObject).rectangle
                val trRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
                if (playerRect!!.overlaps(trRect) && obj.properties.get("active") as Boolean) {
                    val text = obj.properties.get("msg") as String
                    batch?.begin()
                    font.draw(batch, text, Gdx.graphics.width/2f-600f, Gdx.graphics.height/2f + 100f, 1200f, 1, true)
                    batch?.end()
                }
            }
        }
        if (chestTriggers!= null) {
            for (obj in chestTriggers!!) {
                val or = (obj as RectangleMapObject).rectangle
                val trRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
                if (playerRect!!.overlaps(trRect) && !(obj.properties.get("open") as Boolean)) {
                    val text = obj.properties.get("msg") as String
                    if (activeChest == null) activeChest = obj
                    batch?.begin()
                    font.draw(batch, text, Gdx.graphics.width/2f-600f, Gdx.graphics.height/2f + 100f, 1200f, 1, true)
                    batch?.end()
                } else if (activeChest != null) activeChest = null
            }
        }
        if (warpPoints!= null) {
            for (obj in warpPoints!!) {
                val or = (obj as RectangleMapObject).rectangle
                val trRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
                val active = if (obj.properties.get("active") != null) obj.properties.get("active") as Boolean else true
                if (playerRect!!.overlaps(trRect) && active) {
                    val text = obj.properties.get("msg") as String
                    if (activeChest == null) activeChest = obj
                    playerInWarp = true
                    activeWarpPoint = obj
                    batch?.begin()
                    font.draw(batch, text, Gdx.graphics.width/2f-600f, Gdx.graphics.height/2f + 100f, 1200f, 1, true)
                    batch?.end()
                } else if (activeChest != null) activeChest = null
            }
        }
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

        when {
            xTouch in 0f .. 0.25f -> {player?.goLeft = true}
            xTouch in 0.75f .. 1f -> {player?.goRight = true}
            xTouch in 0.25f .. 0.75f && yTouch in 0f.. 0.5f -> {player?.jump()}
            xTouch in 0.25f .. 0.75f && yTouch in 0.5f.. 1f && !playerInWarp -> {
                player?.attack()}
            xTouch in 0.25f .. 0.75f && yTouch in 0.5f.. 1f && playerInWarp -> {
                val d = activeWarpPoint.properties.get("to") as String
                val dest = maps[d]!!
                game.screen = GameScreen(game, dest)
            }
        }

        if(activeChest!= null){
            val or = activeChest!!.rectangle
            val trRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
            activeChest!!.properties.put("open", true)
            player?.weaponEquipped = true
            textTriggers?.get("attackMsg")?.properties?.put("active", true)
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        player?.goRight = false
        player?.goLeft = false
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