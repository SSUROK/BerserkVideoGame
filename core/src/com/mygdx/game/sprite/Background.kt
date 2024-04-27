package com.mygdx.game.sprite

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mygdx.game.base.Sprite
import com.mygdx.game.math.Rect

class Background(
    val region: Texture
): Sprite(TextureRegion(region)) {

    override fun resize(worldBounds: Rect) {
        setHeightProportion(worldBounds.getHeight())
        pos.set(worldBounds.pos)
    }
}