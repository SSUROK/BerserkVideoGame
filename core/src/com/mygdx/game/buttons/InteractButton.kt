package com.mygdx.game.buttons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.base.BaseButton
import com.mygdx.game.base.BaseIntractable
import com.mygdx.game.math.atlas

class InteractButton(
    private val obj: BaseIntractable,
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
        obj.interact()
    }

//    override fun draw(batch: SpriteBatch, font: BitmapFont) {
//        batch.draw(
//            atlasParts[frame],
//            getLeft(), getBottom(),
//            halfWidth, halfHeight,
//            getWidth(), getHeight(),
//            scale, scale,
//            angle
//        )
//        font.draw(batch, text, getLeft(), getBottom())
//    }
}