package ch.cluder.libgdxexamples;

public class GameData {
	public String playerName;

	private static GameData singleton;

	private GameData() {
	}

	public static GameData get() {
		if (singleton == null) {
			singleton = new GameData();
		}
		return singleton;
	}
}
