package com.mygdx.game.utils

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Regions {

    /**
     * Разбивает TextureRegion на фреймы
     * @param region регион
     * @param rows количество строк
     * @param cols количество столбцов
     * @param frames количество фреймов
     * @return массив регионов
     */
    companion object{
        fun split(region: TextureRegion?, rows: Int, cols: Int, frames: Int): Array<TextureRegion?> {
            if (region == null) throw RuntimeException("Split null region")
            val regions = arrayOfNulls<TextureRegion>(frames)
            val tileWidth = region.regionWidth / cols
            val tileHeight = region.regionHeight / rows
            var frame = 0
            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    regions[frame] =
                        TextureRegion(region, tileWidth * j, tileHeight * i, tileWidth, tileHeight)
                    if (frame == frames - 1) return regions
                    frame++
                }
            }
            return regions
        }
    }

}