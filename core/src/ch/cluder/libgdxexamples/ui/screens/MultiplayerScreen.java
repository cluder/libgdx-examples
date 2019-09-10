package ch.cluder.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;

import ch.cluder.libgdxexamples.Debugger;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;
import ch.cluder.libgdxexamples.util.ResourceManager;

public class MultiplayerScreen extends BaseUIScreen {
	TextField nameField;

	public MultiplayerScreen() {

	}

	@Override
	protected void create() {

		clear();
		Gdx.app.setLogLevel(Logger.DEBUG);
		Skin defaultSkin = ResourceManager.getSkin();

		// button table
		Table table = new Table(defaultSkin);
		addActor(table);

		table.setFillParent(true);
		table.setSkin(defaultSkin);

		table.add(new Label("Player", defaultSkin));
		nameField = new TextField("Player", defaultSkin);
		table.add(nameField);

		table.row();

		TextButton startMP = addButton("Start Multiplayer Server", defaultSkin, table);
		table.getCell(startMP).colspan(2);
		startMP.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameScreen newGame = new GameScreen(true);
				newGame.playerName = nameField.getText();
				ScreenManager.getInstance().setScreen(newGame);
			}
		});

		table.row();

		TextButton joinMP = addButton("Join Multiplayer ...", defaultSkin, table);
		table.getCell(joinMP).colspan(2);

		joinMP.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().setScreen(new JoinMultiplayerScreen(nameField.getText()));
			}
		});

//		table.debug();

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
			ScreenManager.getInstance().setScreen(new MainMenuScreen());
			break;
		default:
			break;
		}
		return false;
	}

}
