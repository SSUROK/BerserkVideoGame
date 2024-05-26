package com.mygdx.game.objects

import com.badlogic.gdx.Game
import com.badlogic.gdx.maps.MapObject
import com.mygdx.game.base.BaseIntractableObject
import com.mygdx.game.math.maps
import com.mygdx.game.screen.GameScreen
import com.mygdx.game.sprite.InteractButton

class Warp(
    mapObject: MapObject,
    private val game: Game
): BaseIntractableObject(mapObject) {
    override var interactBtn: InteractButton? = InteractButton(this, "enter", "Enter")

    private val warpTo = mapObject.properties.get("to") as String

    override fun interact(): Boolean {
        val dest = maps[warpTo]!!
        game.screen = GameScreen(game, dest)
        return true
    }


}