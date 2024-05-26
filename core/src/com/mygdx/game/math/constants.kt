package com.mygdx.game.math

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas

fun textXPos(text: String) = Gdx.graphics.width/2f - text.length*55f
val textYPos = Gdx.graphics.height/2f + 100f
val textWidth = Gdx.graphics.width*0.8f
val atlas = TextureAtlas("data/textures/atlas.txt")
val charAtlas = TextureAtlas("data/textures/charAtlas.txt")
val npcAtlas = TextureAtlas("data/textures/npcAtlas.txt")
val enemiesAtlas = TextureAtlas("data/textures/enemiesAtlas.txt")
val maps = mapOf("map1_1" to "data/maps/map1_1.tmx",
    "map1_2" to "data/maps/map1_2.tmx",
    "map1_3" to "data/maps/map1_3.tmx")