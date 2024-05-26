package com.mygdx.game.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.buttons.InteractButton
import com.mygdx.game.entities.Player
import com.mygdx.game.math.textWidth
import com.mygdx.game.math.textXPos
import com.mygdx.game.math.textYPos

abstract class BaseIntractable(private val mapObject: MapObject): Intractable {
    private var pointX: Float?= null
    private var pointY: Float?= null
    private var active: Boolean = true
    private var msg: String? = null
    private var activateMsg: String? = null
    abstract val interactBtn: InteractButton
    private var objAsRect: Rectangle
    private var msgBubble: TextBubble? = null
    private var activateMsgBubble: TextBubble? = null

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

    fun render(player: Player, font: BitmapFont, batch: SpriteBatch){
        val playerRect = player.createCollisionRect()
        if (!playerRect.overlaps(objAsRect)) return
        if (!active) {
            if (activateMsg != null) {
                activateMsgBubble!!.draw(batch, font)
                return
            }
        }
        if (msgBubble!= null) {
            msgBubble!!.draw(batch, font)
        }
        interactBtn.draw(batch, font)
    }
}