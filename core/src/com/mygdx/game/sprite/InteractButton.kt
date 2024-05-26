package com.mygdx.game.sprite

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.base.BaseButton
import com.mygdx.game.base.Intractable
import com.mygdx.game.math.atlas

class InteractButton(
    private val obj: Intractable,
    private val regionName: String = "use",
    text: String = "Use"
): BaseButton(
    atlas.findRegions(regionName),
    text=text
) {

    init {
        setHeightProportion(160f)
        pos = Vector2(Gdx.graphics.width / 2f, halfHeight + 50f)
    }

    override fun action() {
        if (obj.playerNear)
            obj.interact()
    }
}