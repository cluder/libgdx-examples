package ch.clu.libgdxexamples.screens.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;

import ch.clu.libgdxexamples.screens.game.GameScreen;
import ch.clu.libgdxexamples.screens.menu.ListSteamLobbiesScreen;
import ch.clu.libgdxexamples.screens.menu.MainMenuScreen;
import ch.clu.libgdxexamples.screens.menu.MultiplayerScreen;
import ch.clu.libgdxexamples.screens.menu.SettingsScreen;
import ch.clu.libgdxexamples.screens.menu.SteamLobbyScreen;

/**
 * Enumeration of game screens. Also acts as a cache.
 */
public enum Screens {
	MAIN_MENU, //
	SETTINGS, //
	MULTIPLAYER, //
	LIST_STEAM_LOBBY, //
	STEAM_LOBBY, //
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
		case LIST_STEAM_LOBBY:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new ListSteamLobbiesScreen());
			}
			return screen;
		case STEAM_LOBBY:
			screen = screens.get(this);
			if (screen == null) {
				screens.put(this, screen = new SteamLobbyScreen());
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
