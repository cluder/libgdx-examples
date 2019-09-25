package ch.clu.libgdxexamples;

import com.badlogic.gdx.Game;

import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.util.ScreenManager;

public class LibGDXExamples extends Game {
	SteamHelper sh = SteamHelper.get();

	@Override
	public void create() {
		sh.initSteam();

		ScreenManager.getInstance().initialize(this);

		ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
	}

}
