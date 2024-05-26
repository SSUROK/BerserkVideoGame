package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.mygdx.game.base.Entity
import com.mygdx.game.math.atlas
import com.mygdx.game.math.charAtlas
import kotlin.math.abs


/**
 * Player - класс, представляющий игрока.
 * Этот класс расширяет базовый класс Entity и предоставляет функциональность для управления игровым персонажем.
 *
 * @param map карта, на которой размещен игрок.
 * @param x координата X игрока на карте.
 * @param y координата Y игрока на карте.
 */
class Player(map: TiledMap, spawnPoint: RectangleMapObject): Entity(
    map,
    spawnPoint,
    charAtlas.regions
) {

    public var weaponEquipped = false
    var goLeft = false
    var goRight = false

    /** Инициализация игрового персонажа. Устанавливается атлас текстур, затем создаются основные
     * анимации персонажа. */
    init{
        stand = Animation(0.15f, charAtlas.findRegions("idle"), Animation.PlayMode.LOOP_PINGPONG)
        jump = Animation(0f, charAtlas.findRegions("jump"))
        walk = Animation(0.15f, charAtlas.findRegions("walk"), Animation.PlayMode.LOOP_PINGPONG)
        attack = Animation(0.15f, charAtlas.findRegions("attack"), Animation.PlayMode.LOOP_PINGPONG)

        setHeightProportion(2.5f)
    }

    /** метод обновления состояния игрового персонажа со временем. реализует управление персонажем.
     *  должен вызываться из метода обновления экрана */
    override fun update(delta: Float) {
        super.update(delta)
        if (goLeft) {
            moveLeft()
        }

        if (goRight) {
            moveRight()
        }
    }

    /** метод для прыжеов персонажа */
    private fun jump(){
        if (!grounded) { return }
        velocity.y += JUMP_VELOCITY
        state = State.Jumping
        grounded = false
    }

    /** метод движения налево */
    private fun moveLeft(){
        velocity.x = -MAX_VELOCITY
        if (grounded) state = State.Walking
        facesRight = false
    }

    /** метод движения направо */
    private fun moveRight(){
        velocity.x = MAX_VELOCITY
        if (grounded) state = State.Walking
        facesRight = true
    }



    /** метод атаки персонажа */
    private fun attack(){
        if (weaponEquipped) {
            velocity.x = 0f
            println("Attacking")
            state = State.Attacking
        }
    }

    override fun touchDown(touch: Vector2, pointer: Int, button: Int): Boolean {
        when {
            touch.x in 0f .. 0.25f -> {goLeft = true}
            touch.x in 0.75f .. 1f -> {goRight = true}
            touch.x in 0.25f .. 0.75f && touch.y in 0f.. 0.5f -> jump()
            touch.x in 0.25f .. 0.75f && touch.y in 0.5f.. 1f -> attack()
        }
        return false
    }

    override fun touchUp(touch: Vector2, pointer: Int, button: Int): Boolean {
        goLeft = false
        goRight = false
        return false
    }
}