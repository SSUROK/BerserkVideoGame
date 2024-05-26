package com.mygdx.game.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.math.atlas
import com.mygdx.game.math.textWidth
import com.mygdx.game.math.textXPos
import com.mygdx.game.math.textYPos

class TextBubble(
    msg: String
): Sprite(atlas.findRegions("textBubble"), text=msg) {

    private val middleSize = if (msg.length > 40) 5 else 3

    init{
        setHeightProportion(320f)
        pos = Vector2(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f + 200f)
    }

//    override fun draw(batch: SpriteBatch, font: BitmapFont) {
//        val x = Gdx.graphics.width / 2 - halfWidth
//        val y = (Gdx.graphics.height / 2f) + 100f
//        batch.draw(
//            atlasParts[0],
//            x, y,
//            halfWidth, halfHeight,
//            getWidth(),
//            getHeight(),
//            scale, scale,
//            angle
//        )
//        font.draw(batch, msg, x, y + halfHeight + 50f, getWidth() - 10f, 1, true)
//        for (i in 0 .. middleSize){
//            if (i < msg.length/2) {
//                batch.draw(
//                    atlasParts[1],
//                    Gdx.graphics.width / 2 - i * middleSize * 8f,
//                    Gdx.graphics.height / 2 - halfHeight,
//                    getWidth(),
//                    getHeight(),)
//            }else{
//                batch.draw(
//                    atlasParts[1],
//                    Gdx.graphics.width / 2 + i * middleSize * 8f,
//                    Gdx.graphics.height / 2 - halfHeight,
//                    getWidth(),
//                    getHeight(),)
//            }
//        }
//    }

//    atlasParts[frame],
//    getLeft(), getBottom(),
//    halfWidth, halfHeight,
//    getWidth(), getHeight(),
//    scale, scale,
//    angle

}