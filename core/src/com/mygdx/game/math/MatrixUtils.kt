package com.mygdx.game.math

import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4

class MatrixUtils {

    companion object {
        /**
         * Расчёт матрицы перехода 4x4
         * @param mat итоговая матрица преобразований
         * @param src исходный квадрат
         * @param dst итоговый квадрат
         */
        fun calcTransitionMatrix(mat: Matrix4, src: Rect, dst: Rect) {
            val scaleX = dst.getWidth() / src.getWidth()
            val scaleY = dst.getHeight() / src.getHeight()
            mat.idt().translate(dst.pos.x, dst.pos.y, 0f).scale(scaleX, scaleY, 1f)
                .translate(-src.pos.x, -src.pos.y, 0f)
        }

        /**
         * Расчёт матрицы перехода 3x3
         * @param mat итоговая матрица преобразований
         * @param src исходный квадрат
         * @param dst итоговый квадрат
         */
        fun calcTransitionMatrix(mat: Matrix3, src: Rect, dst: Rect) {
            val scaleX = dst.getWidth() / src.getWidth()
            val scaleY = dst.getHeight() / src.getHeight()
            mat.idt().translate(dst.pos.x, dst.pos.y).scale(scaleX, scaleY)
                .translate(-src.pos.x, -src.pos.y)
        }
    }
}