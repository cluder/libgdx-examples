package ch.clu.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;

import ch.clu.libgdxexamples.util.ScreenManager;

public class MainMenuScreen extends BaseUIScreen {

	@Override
	protected Screens getPreviousScreen() {
		return null;
	}

	protected void create() {
		clear();

		Gdx.app.setLogLevel(Logger.DEBUG);

		// button table
		Table buttonTable = new Table(skin);
		buttonTable.setFillParent(true);
		addActor(buttonTable);

		// start button
		TextButton btn = addButton("Start", skin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenManager.getInstance().setScreen(Screens.GAME);
				return false;
			}
		});

		buttonTable.row();

		btn = addButton("Multiplayer ...", skin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenManager.getInstance().setScreen(Screens.MULTIPLAYER);
				return false;
			}
		});

		buttonTable.row();

		// settings button
		btn = addButton("Settings ...", skin, buttonTable);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenManager.getInstance().setScreen(Screens.SETTINGS);
				return false;
			}
		});

		buttonTable.row();

		// exit button
		btn = addButton("Exit", skin, buttonTable);
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
		super.render(delta);

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
