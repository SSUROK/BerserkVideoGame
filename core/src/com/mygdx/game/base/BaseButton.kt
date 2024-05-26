package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array


abstract class BaseButton(region: Array<AtlasRegion>, text: String) : Sprite(region, text=text) {
    private var pointer = 0
    private var pressed = false
    override fun touchDown(touch: Vector2, pointer: Int, button: Int): Boolean {
        if (pressed || !isMe(touch)) {
            return false
        }
        this.pointer = pointer
        pressed = true
        scale = PRESS_SCALE
        return false
    }

    override fun touchUp(touch: Vector2, pointer: Int, button: Int): Boolean {
        if (this.pointer != pointer || !pressed) {
            return false
        }
        pressed = false
        scale = 1f
        if (isMe(touch)) {
            action()
            return false
        }
        return false
    }

    abstract fun action()

    companion object {
        private const val PRESS_SCALE = 0.9f
    }
}

