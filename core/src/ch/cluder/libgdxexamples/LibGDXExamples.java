package ch.cluder.libgdxexamples;

import com.badlogic.gdx.Game;

import ch.cluder.libgdxexamples.ui.screens.Screens;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;

public class LibGDXExamples extends Game {

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);

		ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
	}

}
