package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.mygdx.game.entities.Player
import com.mygdx.game.sprite.TextBubble

abstract class BaseIntractableObject(private val mapObject: MapObject): Intractable, Sprite (
    Array<AtlasRegion>()
){
    private var pointX: Float?= null
    private var pointY: Float?= null
    private var active: Boolean = true
    private var msg: String? = null
    private var activateMsg: String? = null
    private var objAsRect: Rectangle
    private var msgBubble: TextBubble? = null
    private var activateMsgBubble: TextBubble? = null
    override var playerNear: Boolean = false

    init{
        val or = (mapObject as RectangleMapObject).rectangle
        objAsRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
        pointX = objAsRect.x
        pointY = objAsRect.y
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
    }
    override fun interact(): Boolean {
        return false
    }

    override fun drawUI(player: Player, font: BitmapFont, batch: SpriteBatch){
        val playerRect = player.createCollisionRect()
        playerNear = false
        if (!playerRect.overlaps(objAsRect)) return
        playerNear = true
        if (!active) {
            if (activateMsg != null) {
                activateMsgBubble!!.draw(batch, font)
                return
            }
        }
        if (msgBubble!= null) {
            msgBubble!!.draw(batch, font)
        }
        interactBtn?.draw(batch, font)
    }

    override fun drawUI(
        player: Player,
        font: BitmapFont,
        batch: SpriteBatch,
        renderBatch: SpriteBatch,
    ) {
        return
    }
    override fun playerInRange(player: Player): Boolean {
        val playerRect = player.createCollisionRect()
        return playerRect.overlaps(objAsRect)
    }
}