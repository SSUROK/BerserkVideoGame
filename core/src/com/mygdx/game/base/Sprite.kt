package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.mygdx.game.math.Rect

/**
 * Sprite - базовый класс для спрайтов.
 * Предоставляет методы и свойства для отрисовки и управления спрайтами.
 *
 * @property angle угол поворота спрайта.
 * @property scale масштаб спрайта.
 * @property regions массив регионов текстур спрайта.
 * @property frame индекс текущего кадра анимации спрайта.
 * @property destroyed флаг, указывающий на уничтожение спрайта.
 */
abstract class Sprite(
    private val atlasParts: Array<TextureAtlas.AtlasRegion>,
    var angle: Float = 0f,
    var scale:Float = 1f,
    var frame: Int = 0,
    var text: String = "",
    var destroyed: Boolean = false): Rect(){

    /**
     * Устанавливает пропорциональную высоту спрайта.
     *
     * @param height новая высота спрайта.
     */
    fun setHeightProportion(height: Float) {
        setHeight(height)
        val aspect = atlasParts[frame]!!.regionWidth / atlasParts[frame]!!.regionHeight.toFloat()
        setWidth(height * aspect)
    }

    /**
     * Отрисовывает спрайт.
     *
     * @param batch объект SpriteBatch для отрисовки.
     */
    open fun draw(batch: SpriteBatch) {
        batch.draw(
            atlasParts[frame],
            getLeft(), getBottom(),
            halfWidth, halfHeight,
            getWidth(), getHeight(),
            scale, scale,
            angle
        )
    }

    open fun draw(batch: SpriteBatch, font: BitmapFont) {
        batch.begin()
        batch.draw(
            atlasParts[frame],
            getLeft(), getBottom(),
            halfWidth, halfHeight,
            getWidth(), getHeight(),
            scale, scale,
            angle
        )
        val rows = text.length/24
        font.draw(batch, text,
            getLeft(), getBottom() + halfHeight + 30f + rows * 30f,
            getWidth() - 10f, 1, true)
        batch.end()
    }

    /**
     * Обновляет состояние спрайта.
     *
     * @param delta время в секундах, прошедшее с последнего обновления.
     */
    open fun update(delta: Float) {}

    /**
     * Изменяет размеры спрайта в соответствии с границами мира.
     *
     * @param worldBounds границы мира.
     */
    open fun resize(worldBounds: Rect) {}

//    fun setAngle(angle: Float) {
//        this.angle = angle
//    }
//
//    fun setScale(scale: Float) {
//        this.scale = scale
//    }

    /**
     * Проверяет, уничтожен ли спрайт.
     *
     * @return true, если спрайт уничтожен, иначе - false.
     */
    fun isDestroyed(): Boolean {
        return destroyed
    }

    /**
     * Уничтожает спрайт.
     */
    open fun destroy() {
        destroyed = true
    }

    /**
     * Сбрасывает флаг уничтожения спрайта.
     */
    fun flushDestroy() {
        destroyed = false
    }

    open fun touchDown(touch: Vector2, pointer: Int, button: Int): Boolean {
        return false
    }

    open fun touchUp(touch: Vector2, pointer: Int, button: Int): Boolean {
        return false
    }
}