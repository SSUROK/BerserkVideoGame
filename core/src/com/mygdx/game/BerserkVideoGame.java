package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.screen.FirstScreen;
import com.mygdx.game.screen.MainMenu;

/**
 * BerserkVideoGame - главный класс игры, управляющий ее жизненным циклом и состояниями.
 */

public class BerserkVideoGame extends Game {
	SpriteBatch batch;
	Texture img;

	/**
	 * Вызывается при создании приложения. В этом методе инициализируются ресурсы
	 * и устанавливается первый экран игры.
	 */
	@Override
	public void create () {
		setScreen(new MainMenu(this));
	}
}
