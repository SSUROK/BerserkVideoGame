package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.math.Rect
import java.util.Arrays

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
abstract class Sprite
    (var angle: Float = 0f,
     var scale:Float = 1f,
     var regions: Array<TextureRegion?>,
     var frame: Int = 0,
     var destroyed: Boolean = false): Rect(){

     constructor(region: TextureRegion?):this(regions=arrayOf(region))

    constructor() : this(regions=arrayOf(null))

    /**
     * Устанавливает пропорциональную высоту спрайта.
     *
     * @param height новая высота спрайта.
     */
    fun setHeightProportion(height: Float) {
        setHeight(height)
        val aspect = regions[frame]!!.regionWidth / regions[frame]!!.regionHeight.toFloat()
        setWidth(height * aspect)
    }

    /**
     * Отрисовывает спрайт.
     *
     * @param batch объект SpriteBatch для отрисовки.
     */
    fun draw(batch: SpriteBatch) {
        batch.draw(
            regions[frame],
            getLeft(), getBottom(),
            halfWidth, halfHeight,
            getWidth(), getHeight(),
            scale, scale,
            angle
        )
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
}