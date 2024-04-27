package com.mygdx.game.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs


open abstract class Entity(
    val map: TiledMap,
    x: Float,
    y: Float
) : Sprite(){

    protected var hp = 0
    var velocity = Vector2()
    val MAX_VELOCITY = 10f
    var JUMP_VELOCITY = 40f
    var v0 = Vector2()
    var HEIGHT: Float = 0f
    var WIDTH: Float = 0f
    val DAMPING = 0.87f
    val ATTACK_TIME = 1.65f
    var position = Vector2()
    var state = State.Walking
    var stateTime = 0f
    var texture: Texture? = null
    var facesRight = true
    var grounded = false
    val GRAVITY = -2.5f
    var screen: BaseScreen? = null

    var stand: Animation<TextureRegion>? = null
    var walk: Animation<TextureRegion>? = null
    var jump: Animation<TextureRegion>? = null
    var attack: Animation<TextureRegion>? = null

    enum class State {
        Standing,
        Walking,
        Jumping,
        Attacking
    }

    init{
        position.set(x, y)
    }

    override fun update(delta: Float) {

    }

    open fun render(deltaTime: Float, renderer: OrthogonalTiledMapRenderer){
    }


    override fun destroy() {
        super.destroy()
//        boom()
    }

//    fun damage(damage: Int) {
//        frame = 1
//        damageAnimateTimer = 0f
//        hp -= damage
//        if (hp <= 0) {
//            hp = 0
//            destroy()
//        }
//    }

    companion object{

    }

//    fun getDamage(): Int {
//        return damage
//    }

//    fun getHp(): Int {
//        return hp
//    }
//
//    fun getV(): Vector2 {
//        return v
//    }

//    private fun shoot() {
//        val bullet: Bullet = bulletPool.obtain()
//        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, damage, bulletHeight)
//        bulletSound.play()
//    }

//    private fun boom() {
//        val explosion: Explosion = explosionPool.obtain()
//        explosion.set(getHeight(), pos)
//    }
}