package ch.clu.libgdxexamples.util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import ch.clu.libgdxexamples.screens.util.Screens;

public class ScreenManager {

	// Singleton: unique instance
	private static ScreenManager instance;

	// Reference to game
	private Game game;

	// Singleton: private constructor
	private ScreenManager() {
		super();
	}

	// Singleton: retrieve instance
	public static ScreenManager getInstance() {
		if (instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}

	// Initialization with the game class
	public void initialize(Game game) {
		this.game = game;
	}

	public void setScreen(Screens screenType) {
		if (screenType != null) {
			game.setScreen(screenType.get());
		}
	}

	public void setScreen(Screen screen) {
		game.setScreen(screen);

	}
}
