package com.mygdx.game.entities

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.base.BaseIntractableEntity
import com.mygdx.game.base.Entity
import com.mygdx.game.math.enemiesAtlas
import com.mygdx.game.math.npcAtlas

class Enemy(
    mapObject: RectangleMapObject,
    map: TiledMap,
    private val trigger: RectangleMapObject? = null
): Entity(
    map= map,
    spawnPoint= mapObject,
    sprite= enemiesAtlas.findRegions(
        mapObject.properties.get("entity").toString())
){

    init{
        val h = if (mapObject.properties.get("entityHeight") != null)
            mapObject.properties.get("entityHeight") as Float
        else 2.5f
        setHeightProportion(h)
        stand = Animation(0.15f, enemiesAtlas.findRegions(
            mapObject.properties.get("entity").toString() + "-idle"),
            Animation.PlayMode.LOOP_PINGPONG)
        jump = Animation(0.15f, enemiesAtlas .findRegions(
            mapObject.properties.get("entity").toString() + "-jump"),
            Animation.PlayMode.LOOP_PINGPONG)
    }

    fun update(delta: Float, player: Player) {
        super.update(delta)
        val playerRect = player.createCollisionRect()
        val or = trigger!!.rectangle
        val trRect = Rectangle(or.x/16f, or.y/16f, or.width/16f, or.height/16f)
        if (playerRect.overlaps(trRect) &&
            trigger.properties?.get("active") as Boolean) {
            jump()
            trigger.properties?.put("active", false)
        }
    }

    private fun jump() {
        if (!grounded) { return }
        velocity.y += JUMP_VELOCITY
        velocity.x = -MAX_VELOCITY
        facesRight = false
        state = State.Jumping
        grounded = false
    }
}