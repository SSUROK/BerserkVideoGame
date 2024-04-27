package com.mygdx.game.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mygdx.game.base.BaseScreen
import com.mygdx.game.math.Rect
import com.mygdx.game.sprite.Background


class MainMenu(game:Game) : BaseScreen(game) {

    private var background: Background? = null

    private var bg: Texture? = null

    override fun show() {
        title = TextureRegion(Texture(Gdx.files.internal("data/badlogic.jpg")),
            0, 0,  256, 256)
        batch = SpriteBatch()
        batch!!.projectionMatrix.setToOrtho2D(0f, 0f, SCREEN_WIDTH, SCREEN_HEIGHT)
//        exitButton = exitBt(atlas)
//        playButton = playBt(atlas, game)
//        music.play()
//        music.setLooping(true)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch?.begin();
        batch?.draw(title,
            SCREEN_WIDTH/2-128, SCREEN_HEIGHT/2-128)
        batch?.end();

        time += delta;
        if (time > 1) {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
                game.setScreen(FirstScreen(game));
//                Gdx.app.debug("Berserk", "Go to next screen")
            }
        }
    }

//    override fun resize(worldBounds: Rect) {
//        background!!.resize(worldBounds)
//    }

//    private fun update(delta: Float) {
//
//    }

//    private fun draw() {
//        batch!!.begin()
//        background!!.draw(batch!!)
//        batch!!.end()
//    }
}