package ch.cluder.libgdxexamples.ui.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;

public enum Screens {
	MAIN_MENU, //
	SETTINGS, //
	MULTIPLAYER, //
	JOIN_MP, //
	GAME; //

	Map<Screens, Screen> screens = new HashMap<>();

	public Screen get() {
		Screen screen;
		switch (this) {
		case MAIN_MENU:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new MainMenuScreen());
			}
			return screen;
		case SETTINGS:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new SettingsScreen());
			}
			return screen;
		case MULTIPLAYER:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new MultiplayerScreen());
			}
			return screen;
		case JOIN_MP:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new JoinMultiplayerScreen());
			}
			return screen;
		case GAME:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new GameScreen());
			}
			return screen;
		default:
			break;
		}
		throw new RuntimeException("unknown enum");
	}
}
