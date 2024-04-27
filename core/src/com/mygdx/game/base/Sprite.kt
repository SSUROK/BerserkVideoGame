package com.mygdx.game.base

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.math.Rect
import com.mygdx.game.utils.Regions
import java.util.Arrays

abstract class Sprite
    (var angle: Float = 0f,
     var scale:Float = 1f,
     var regions: Array<TextureRegion?>,
     var frame: Int = 0,
     var destroyed: Boolean = false): Rect(){

     constructor(region: TextureRegion?):this(regions=arrayOf(region))

    constructor(region: TextureRegion?, rows: Int, cols: Int, frames: Int) : this(
        regions=Regions.split(region, rows, cols, frames)
    )

    constructor() : this(regions=arrayOf(null))

    fun setHeightProportion(height: Float) {
        setHeight(height)
        val aspect = regions[frame]!!.regionWidth / regions[frame]!!.regionHeight.toFloat()
        setWidth(height * aspect)
    }

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

    open fun update(delta: Float) {}

    open fun resize(worldBounds: Rect) {}

//    fun setAngle(angle: Float) {
//        this.angle = angle
//    }
//
//    fun setScale(scale: Float) {
//        this.scale = scale
//    }

    fun isDestroyed(): Boolean {
        return destroyed
    }

    open fun destroy() {
        destroyed = true
    }

    fun flushDestroy() {
        destroyed = false
    }
}