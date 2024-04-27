package com.mygdx.game.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.base.BaseScreen
import com.mygdx.game.entities.Player


class FirstScreen(game: Game) : BaseScreen(game) {

    private var textTriggers : MapObjects? = null
    private var chestTriggers : MapObjects? = null
    private var activeChest : RectangleMapObject? = null
    val font = BitmapFont()
    override fun show() {
        super.show()
        map = TmxMapLoader().load("data/maps/map1/act0.tmx")
        renderer = OrthogonalTiledMapRenderer(map, 1/16f)
        val poiLayer: MapLayer? = map?.getLayers()?.get("POI")
        val spawnPoint = poiLayer?.objects?.get("spawn") as RectangleMapObject
        textTriggers = map?.getLayers()?.get("TextTriggers")?.objects
        chestTriggers = map?.getLayers()?.get("ChestTriggers")?.objects
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
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if(activeChest!= null){
            val or = activeChest!!.rectangle
            val trRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
            activeChest!!.properties.put("open", true)
            player?.weaponEquipped = true
            textTriggers?.get("attackMsg")?.properties?.put("active", true)
        }
        return super.touchDown(screenX, screenY, pointer, button)
    }
}