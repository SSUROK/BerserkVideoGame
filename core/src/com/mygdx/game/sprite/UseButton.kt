//package com.mygdx.game.sprite
//
//import com.badlogic.gdx.Game
//import com.badlogic.gdx.graphics.Texture
//import com.badlogic.gdx.graphics.g2d.TextureAtlas
//import com.mygdx.game.base.BaseButton
//
//class UseButton: BaseButton(Texture("button.png")) {
//
//    private val MARGIN = 0.025f
//
//    private var game: Game? = null
//
//    fun playBt(atlas: TextureAtlas, game: Game?) {
//        this.game = game
//    }
//
//    override fun resize(worldBounds: Recta) {
//        setHeightProportion(0.25f)
//        setLeft(worldBounds.getLeft() + MARGIN)
//        setBottom(worldBounds.getBottom() + MARGIN)
//    }
//
//    override fun action() {
//        game!!.setScreen(GameScreen())
//    }
//}