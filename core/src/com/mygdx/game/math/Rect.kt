package com.mygdx.game.math

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2


/** базовый класс для любого объекта на экране. Устаревшая реализация, к удалению в следующей версии. */
open class Rect(
    protected var halfWidth : Float = 0f,
    protected var halfHeight : Float = 0f,
    var pos : Vector2 = Vector2()) {

    var r = Rectangle()


    constructor(from: Rect) : this(from.pos.x, from.pos.y, from.halfWidth, from.halfHeight)

    constructor(x: Float, y: Float, halfWidth: Float, halfHeight: Float) : this(halfHeight, halfWidth){
        this.pos.set(x, y)
    }

    fun getLeft(): Float {
        return pos.x - halfWidth
    }

    fun getTop(): Float {
        return pos.y + halfHeight
    }

    fun getRight(): Float {
        return pos.x + halfWidth
    }

    fun getBottom(): Float {
        return pos.y - halfHeight
    }

//    fun getHalfWidth(): Float {
//        return halfWidth
//    }

//    fun getHalfHeight(): Float {
//        return halfHeight
//    }

    fun getWidth(): Float {
        return halfWidth * 2f
    }

    fun getHeight(): Float {
        return halfHeight * 2f
    }

    fun set(from: Rect) {
        pos.set(from.pos)
        halfWidth = from.halfWidth
        halfHeight = from.halfHeight
    }

    fun setLeft(left: Float) {
        pos.x = left + halfWidth
    }

    fun setTop(top: Float) {
        pos.y = top - halfHeight
    }

    fun setRight(right: Float) {
        pos.x = right - halfWidth
    }

    fun setBottom(bottom: Float) {
        pos.y = bottom + halfHeight
    }

    fun setWidth(width: Float) {
        halfWidth = width / 2f
    }

    fun setHeight(height: Float) {
        halfHeight = height / 2f
    }

    fun setSize(width: Float, height: Float) {
        halfWidth = width / 2f
        halfHeight = height / 2f
    }

    fun isMe(touch: Vector2): Boolean {
        return touch.x >= getLeft() && touch.x <= getRight() && touch.y >= getBottom() && touch.y <= getTop()
    }

    fun isOutside(other: Rect): Boolean {
        return getLeft() > other.getRight() || getRight() < other.getLeft() || getBottom() > other.getTop() || getTop() < other.getBottom()
    }

    override fun toString(): String {
        return "Rectangle: pos" + pos + " size(" + getWidth() + ", " + getHeight() + ")"
    }
}