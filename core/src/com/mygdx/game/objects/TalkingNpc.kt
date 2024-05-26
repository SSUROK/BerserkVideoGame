package com.mygdx.game.objects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.mygdx.game.base.BaseIntractableEntity
import com.mygdx.game.entities.Npc
import com.mygdx.game.sprite.InteractButton

class TalkingNpc  (
    private val mapObject: RectangleMapObject,
    map: TiledMap
): BaseIntractableEntity(mapObject, map), Npc {
    override var interactBtn: InteractButton? = InteractButton(this, "use", "Talk")

    override fun interact(): Boolean {
        active = false
        return true
    }
}