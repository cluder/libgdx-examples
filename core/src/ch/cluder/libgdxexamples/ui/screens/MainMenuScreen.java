package ch.cluder.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;

import ch.cluder.libgdxexamples.Debugger;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;
import ch.cluder.libgdxexamples.util.ResourceManager;

public class MainMenuScreen extends BaseUIScreen {

	protected void create() {
		clear();

		Gdx.app.setLogLevel(Logger.DEBUG);
		Skin defaultSkin = ResourceManager.getSkin();

		// button table
		Table buttonTable = new Table(defaultSkin);
		buttonTable.setFillParent(true);

		// add table to stage
		addActor(buttonTable);

		// start button
		TextButton btn = addButton("Start", defaultSkin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenManager.getInstance().setScreen(Screens.GAME);
				return false;
			}
		});

		buttonTable.row();

		btn = addButton("Multiplayer ...", defaultSkin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenManager.getInstance().setScreen(Screens.MULTIPLAYER);
				return false;
			}
		});

		buttonTable.row();

		// settings button
		btn = addButton("Settings ...", defaultSkin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenManager.getInstance().setScreen(Screens.SETTINGS);
				return false;
			}
		});

		buttonTable.row();

		// exit button
		btn = addButton("Exit", defaultSkin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.exit();
				return false;
			}
		});

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
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
//		dispose();
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
