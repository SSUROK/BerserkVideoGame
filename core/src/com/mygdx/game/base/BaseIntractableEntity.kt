package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.mygdx.game.entities.Player
import com.mygdx.game.math.npcAtlas
import com.mygdx.game.sprite.TextBubble

abstract class BaseIntractableEntity(
    private val mapObject: RectangleMapObject,
    map: TiledMap
): Intractable, Entity(
    map=map,
    spawnPoint=mapObject,
    sprite= npcAtlas.findRegions(
        mapObject.properties.get("entity").toString()
    )){
    var active: Boolean = true
    private var msg: String? = null
    private var activateMsg: String? = null
    private var msgBubble: TextBubble? = null
    private var activateMsgBubble: TextBubble? = null
    override var playerNear: Boolean = false

    init{
        active = if (mapObject.properties.get("active") != null)
            mapObject.properties.get("active") as Boolean
        else true
        msg = if (mapObject.properties.get("msg") != null)
            mapObject.properties.get("msg") as String
        else null
        activateMsg = if (mapObject.properties.get("activateMsg")!= null)
            mapObject.properties.get("activateMsg") as String
        else null
        if (msg!=null)msgBubble = TextBubble(msg!!)
        if (activateMsg!=null)activateMsgBubble = TextBubble(activateMsg!!)

        stand = Animation(0.15f, npcAtlas.findRegions(
            mapObject.properties.get("entity").toString() + "-idle"),
            Animation.PlayMode.LOOP_PINGPONG)
        val h = if (mapObject.properties.get("entityHeight") != null)
            mapObject.properties.get("entityHeight") as Float
        else 2.5f
        setHeightProportion(h)
    }
    override fun interact(): Boolean {
        return false
    }

    override fun drawUI(player: Player, font: BitmapFont, batch: SpriteBatch) {
        return
    }

    override fun drawUI(player: Player, font: BitmapFont, batch: SpriteBatch,
                        renderBatch: SpriteBatch) {
        super.draw(renderBatch)
        playerNear = false
        if (!playerInRange(player)) return
        playerNear = true
        if (!active) {
            if (activateMsg != null) {
                activateMsgBubble!!.draw(batch, font)
                return
            }
        }
        if (msgBubble!= null && !active) {
            msgBubble!!.draw(batch, font)
        }
        if (active)
            interactBtn?.draw(batch, font)
    }

    override fun playerInRange(player: Player): Boolean {
        val playerRect = player.createCollisionRect()
        val thisRect = createCollisionRect()
        return (playerRect.overlaps(thisRect))
    }
}