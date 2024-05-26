package com.mygdx.game.objects

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.mygdx.game.base.BaseIntractableObject
import com.mygdx.game.sprite.InteractButton

class TextMsg(mapObj: MapObject): BaseIntractableObject(mapObj) {
    override var interactBtn: InteractButton? = null
}