package com.mygdx.game.objects

import com.badlogic.gdx.maps.MapObject
import com.mygdx.game.base.BaseIntractable
import com.mygdx.game.buttons.InteractButton

class Chest (
    private val mapObject: MapObject
): BaseIntractable(mapObject) {
    override val interactBtn: InteractButton = InteractButton(this, "use", "Open")
}