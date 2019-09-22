package ch.clu.libgdxexamples;

import com.badlogic.gdx.Game;

import ch.clu.libgdxexamples.ui.screens.Screens;
import ch.clu.libgdxexamples.ui.screens.util.ScreenManager;
import ch.clu.libgdxexamples.util.SteamHelper;

public class LibGDXExamples extends Game {
	SteamHelper sh = SteamHelper.get();

	@Override
	public void create() {
		sh.initSteam();

		ScreenManager.getInstance().initialize(this);

		ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
	}

}
