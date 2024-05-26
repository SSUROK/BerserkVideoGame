//package com.mygdx.game.sprite
//
//import com.badlogic.gdx.graphics.Texture
//import com.badlogic.gdx.graphics.g2d.TextureRegion
//import com.mygdx.game.base.Sprite
//import com.mygdx.game.math.Rect
//
///**
// * Background - класс, представляющий фон игровой сцены.
// * Этот класс отображает фон с помощью текстуры.
// *
// * @property region текстура для отображения фона.
// */
//class Background(
//    val region: Texture
//): Sprite(TextureRegion(region)) {
//
//    /**
//     * Метод изменяет размер фона и устанавливает его позицию так, чтобы он занимал всю область мира.
//     *
//     * @param worldBounds область мира, в которой отображается фон.
//     */
//    override fun resize(worldBounds: Rect) {
//        setHeightProportion(worldBounds.getHeight())
//        pos.set(worldBounds.pos)
//    }
//}