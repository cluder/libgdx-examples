package ch.clu.libgdxexamples.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;

import ch.clu.libgdxexamples.data.GameData;
import ch.clu.libgdxexamples.screens.game.GameScreen;
import ch.clu.libgdxexamples.screens.util.BaseUIScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.util.ScreenManager;

public class MultiplayerScreen extends BaseUIScreen {
	TextField nameField;

	public MultiplayerScreen() {

	}

	@Override
	protected Screens getPreviousScreen() {
		return Screens.MAIN_MENU;
	}

	@Override
	protected void create() {

		clear();
		Gdx.app.setLogLevel(Logger.DEBUG);

		// button table
		Table table = new Table(skin);
		addActor(table);

		table.setFillParent(true);
		table.setSkin(skin);

		table.add(new Label("Name", skin));

		String playerName = SteamHelper.get().getSF().getPersonaName();
		nameField = new TextField(playerName, skin);
		table.add(nameField);

		table.row();

		{
			TextButton btn = addButton("Start Multiplayer Server", skin, table);
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
			TextButton btn = addButton("Join Multiplayer ...", skin, table);
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
			TextButton btn = addButton("Steam Lobbies ...", skin, table);
			table.getCell(btn).colspan(2);

			btn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenManager.getInstance().setScreen(Screens.LIST_STEAM_LOBBY);
				}
			});
		}
		table.row();

		{
			TextButton btn = addButton("Back", skin, table);
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
		GameData.get().playerName = nameField.getText();
//		dispose();
	}

}
