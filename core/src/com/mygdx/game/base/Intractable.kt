package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.entities.Player
import com.mygdx.game.sprite.InteractButton

interface Intractable {

//    var pointX: Float?
//    var pointY: Float?
//    var active: Boolean?
//    var msg: String?
//    var activateMsg: String?
    var interactBtn: InteractButton?
    var playerNear: Boolean
//    var objAsRect: Rectangle?
//    var msgBubble: TextBubble?
//    var activateMsgBubble: TextBubble?

    fun interact(): Boolean

    fun drawUI(player: Player, font: BitmapFont, batch: SpriteBatch)
    fun drawUI(player: Player, font: BitmapFont, batch: SpriteBatch, renderBatch: SpriteBatch)

    fun playerInRange(player: Player): Boolean
}