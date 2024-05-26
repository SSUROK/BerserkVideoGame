package com.mygdx.game.objects

import com.badlogic.gdx.Game
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.base.BaseIntractable
import com.mygdx.game.base.Intractable
import com.mygdx.game.buttons.InteractButton
import com.mygdx.game.math.maps
import com.mygdx.game.screen.GameScreen

class Warp(
    mapObject: MapObject,
    private val game: Game
): BaseIntractable(mapObject) {
    override val interactBtn: InteractButton = InteractButton(this, "enter", "Enter")

    private val warpTo = mapObject.properties.get("to") as String

    override fun interact(): Boolean {
        val dest = maps[warpTo]!!
        game.screen = GameScreen(game, dest)
        return true
    }


}