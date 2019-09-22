package ch.clu.libgdxexamples.ui.screens;

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

import ch.clu.libgdxexamples.data.GameData;
import ch.clu.libgdxexamples.ui.screens.util.ScreenManager;
import ch.clu.libgdxexamples.util.Debugger;
import ch.clu.libgdxexamples.util.ResourceManager;
import ch.clu.libgdxexamples.util.SteamHelper;

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

		table.add(new Label("Name", defaultSkin));

		String playerName = SteamHelper.get().getSteamFriends().getPersonaName();
		nameField = new TextField(playerName, defaultSkin);
		table.add(nameField);

		table.row();

		{
			TextButton btn = addButton("Start Multiplayer Server", defaultSkin, table);
			table.getCell(btn).colspan(2);
			btn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					GameScreen game = (GameScreen) Screens.GAME.get();
					game.startNetworkServer();
					ScreenManager.getInstance().setScreen(game);
				}
			});
		}

		table.row();

		{
			TextButton btn = addButton("Join Multiplayer ...", defaultSkin, table);
			table.getCell(btn).colspan(2);

			btn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenManager.getInstance().setScreen(Screens.JOIN_MP);
				}
			});
		}
		table.row();

		{
			TextButton btn = addButton("Back", defaultSkin, table);
			btn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
				}
			});
			table.getCell(btn).colspan(2);

		}

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
		GameData.get().playerName = nameField.getText();
//		dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.debug("DBG", "Key down:" + keycode);

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
			break;
		default:
			break;
		}
		return false;
	}

}
