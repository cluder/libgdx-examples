package ch.cluder.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ch.cluder.libgdxexamples.Debugger;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;

public class MainMenuScreen extends Stage implements Screen {

	// creates the stage with a screen viewport
	public MainMenuScreen() {
		super(new ScreenViewport());

		create();
	}

	private void create() {
		clear();

		Gdx.app.setLogLevel(Logger.DEBUG);
		Skin defaultSkin = new Skin(Gdx.files.internal("uiskin.json"));

		int buttonWidth = 150;

		// button table
		Table buttonTable = new Table(defaultSkin);
		buttonTable.setFillParent(true);

		// add table to stage
		addActor(buttonTable);

		// start button
		{
			TextButton btn = new TextButton("Start", defaultSkin);
			btn.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					ScreenManager.getInstance().setScreen(new GameScreen());
					return false;
				}
			});

			// add to table
			buttonTable.add(btn).width(buttonWidth).center().fillX().row();

		}

		// exit button
		{
			TextButton btn = new TextButton("Exit", defaultSkin);

			btn.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					Gdx.app.exit();
					return false;
				}
			});
			buttonTable.add(btn).center().fillX().padTop(20).row();
		}

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		act();
		draw();

		Debugger.printDebugInfo();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		// dispose when hiding
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.debug("DBG", "Key down:" + keycode);

		switch (keycode) {
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
		default:
			break;
		}
		return true;
	}
}
