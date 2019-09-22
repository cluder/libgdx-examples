package ch.clu.libgdxexamples.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameData {
	public String playerName;

	private static GameData singleton;

	private GameData() {
		Preferences preferences = Gdx.app.getPreferences("GameData");
	}

	public static GameData get() {
		if (singleton == null) {
			singleton = new GameData();
		}
		return singleton;
	}
}
