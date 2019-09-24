package ch.clu.libgdxexamples.ui.screens;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.codedisaster.steamworks.SteamMatchmaking.LobbyComparison;
import com.codedisaster.steamworks.SteamMatchmaking.LobbyType;

import ch.clu.libgdxexamples.steam.LobbyData;
import ch.clu.libgdxexamples.steam.SteamHelper;

public class CreateSteamLobbyScreen extends BaseUIScreen implements Observer {

	// steam helper to access steam interfaces
	SteamHelper steam;

	// ui elements
	Label lobbyLabel;
	Table lobbyTable;

	public CreateSteamLobbyScreen() {
		steam = SteamHelper.get();
		steam.addObserver(this);
	}

	@Override
	protected Screens getPreviousScreen() {
		return Screens.MULTIPLAYER;
	}

	@Override
	protected void create() {
		clear();

		createUI();
	}

	private void createUI() {

		VerticalGroup mainGroup = new VerticalGroup();
		mainGroup.setFillParent(true);
		mainGroup.align(Align.center);
		mainGroup.space(15);

		addActor(mainGroup);

		Label title = new Label("Steam Lobbies", skin);
		mainGroup.addActor(title);

		lobbyTable = new Table(skin);
		mainGroup.addActor(lobbyTable);

		lobbyLabel = new Label("Lobby:", skin);
//		lobbyTable.add(lobbyLabel);

		HorizontalGroup buttonGroup = new HorizontalGroup();
		mainGroup.addActor(buttonGroup);

		Button createLobbyButton = new TextButton("Create Lobby", skin);
		createLobbyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				createLobby();
			}
		});
		buttonGroup.addActor(createLobbyButton);

		Button refreshLobbysButton = new TextButton("Refresh Lobbies", skin);
		refreshLobbysButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				refreshLobbys();
			}
		});
		buttonGroup.padTop(10);
		buttonGroup.addActor(refreshLobbysButton);

		setDebugAll(true);
	}

	protected void createLobby() {
		if (steam.getSteamIDLobby() != null) {
			steam.getSmm().leaveLobby(steam.getSteamIDLobby());
		}
		steam.getSmm().createLobby(LobbyType.Public, 4);
	}

	/**
	 * Trigger lobby list refresh (max 100 results)
	 * 
	 * @param lobbyTable
	 */
	private void refreshLobbys() {
		steam.getSmm().addRequestLobbyListStringFilter(SteamHelper.LOBBY_KEY_MAGIC, SteamHelper.LOBBY_VALUE_MAGIC,
				LobbyComparison.Equal);

		steam.getSmm().addRequestLobbyListResultCountFilter(100);
		steam.getSmm().requestLobbyList();

	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	public void hide() {
		steam.leaveLobby();
	}

	@Override
	public void update(Observable o, Object arg) {
		// got updated lobby list
		if (arg instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<LobbyData> datas = (List<LobbyData>) arg;
			lobbyTable.clearChildren();
			for (LobbyData d : datas) {
				String lobbyInfo = String.format("%s (#members:%s )", d.name, d.numMembers);
				lobbyTable.add(new Label(lobbyInfo, skin));
				lobbyTable.row();
			}

		}
	}

}
