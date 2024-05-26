package com.mygdx.game.sprite

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.base.Sprite
import com.mygdx.game.math.atlas

class TextBubble(
    msg: String
): Sprite(atlas.findRegions("textBubble"), text=msg) {

    private val middleSize = if (msg.length > 40) 5 else 3

    init{
        setHeightProportion(320f)
        pos = Vector2(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f + 200f)
    }

}