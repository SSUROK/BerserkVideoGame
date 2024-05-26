package com.mygdx.game.objects

import com.badlogic.gdx.maps.MapObject
import com.mygdx.game.base.BaseIntractableObject
import com.mygdx.game.sprite.InteractButton

class Chest (
    private val mapObject: MapObject
): BaseIntractableObject(mapObject) {
    override var interactBtn: InteractButton? = InteractButton(this, "use", "Open")
}