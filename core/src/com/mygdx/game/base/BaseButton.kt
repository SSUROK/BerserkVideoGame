//package com.mygdx.game.base
//
//import com.badlogic.gdx.graphics.Texture
//import com.badlogic.gdx.math.Vector2
//
//
//abstract class BaseButton(region: Texture) : Sprite(region) {
//    private var pointer = 0
//    private var pressed = false
//    fun touchDown(touch: Vector2?, pointer: Int, button: Int): Boolean {
//        if (pressed || !isMe(touch!!)) {
//            return false
//        }
//        this.pointer = pointer
//        pressed = true
//        scale = PRESS_SCALE
//        return false
//    }
//
//    fun touchUp(touch: Vector2?, pointer: Int, button: Int): Boolean {
//        if (this.pointer != pointer || !pressed) {
//            return false
//        }
//        pressed = false
//        scale = 1f
//        if (isMe(touch!!)) {
//            action()
//            return false
//        }
//        return false
//    }
//
//    abstract fun action()
//
//    companion object {
//        private const val PRESS_SCALE = 0.9f
//    }
//}
//
