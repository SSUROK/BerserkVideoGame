package com.mygdx.game.base

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array


/**
 * Entity - базовый класс для игровых сущностей.
 * Он предоставляет основные методы и свойства, общие для всех сущностей.
 *
 * @param map карта, на которой размещена сущность.
 * @param x координата X сущности на карте.
 * @param y координата Y сущности на карте.
 */
abstract class Entity(
    val map: TiledMap,
    spawnPoint: RectangleMapObject,
    sprite: Array<AtlasRegion>
) : Sprite(sprite) {

    protected var hp = 0
    var velocity = Vector2()
    val MAX_VELOCITY = 10f
    var JUMP_VELOCITY = 40f
    var v0 = Vector2()
//    var HEIGHT: Float = 0f
//    var WIDTH: Float = 0f
//    var COLLISION_HEIGHT: Float = 0f
//    var COLLISION_WIDTH: Float = 0f
    val DAMPING = 0.87f
    val ATTACK_TIME = 1.65f
//    var position = Vector2()
    var state = State.Standing
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

    /**
     * Перечисление состояний сущности.
     */
    enum class State {
        Standing,
        Walking,
        Jumping,
        Attacking
    }

    init{
        pos.set(spawnPoint.rectangle.x/16f , spawnPoint.rectangle.y/16f)
    }

    /**
     * Обновляет состояние сущности.
     *
     * @param delta время в секундах, прошедшее с последнего обновления.
     */
    override fun update(delta: Float) {

    }

    /**
     * Уничтожает сущность.
     */
    override fun destroy() {
        super.destroy()
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
}