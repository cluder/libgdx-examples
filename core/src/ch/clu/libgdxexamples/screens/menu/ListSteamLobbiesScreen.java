package ch.clu.libgdxexamples.screens.menu;

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
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamMatchmaking.LobbyType;

import ch.clu.libgdxexamples.screens.util.BaseUIScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyData;
import ch.clu.libgdxexamples.steam.data.LobbyDataList;
import ch.clu.libgdxexamples.util.ScreenManager;

@SuppressWarnings("deprecation")
public class ListSteamLobbiesScreen extends BaseUIScreen implements Observer {

	// steam helper to access steam interfaces
	SteamHelper steam;

	// ui elements
	Table lobbyListTable;

	public ListSteamLobbiesScreen() {
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
		mainGroup.space(5);

		addActor(mainGroup);

		Label title = new Label("Steam Lobby List", skin);
		mainGroup.addActor(title);

		lobbyListTable = new Table(skin);
		mainGroup.addActor(lobbyListTable);

		HorizontalGroup buttonGroup = new HorizontalGroup();
		mainGroup.addActor(buttonGroup);

		Button createLobbyButton = new TextButton("Create Lobby", skin);
		createLobbyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				createLobby();
				ScreenManager.getInstance().setScreen(Screens.STEAM_LOBBY);
			}
		});

		buttonGroup.addActor(createLobbyButton);

		Button refreshLobbysButton = new TextButton("Refresh Lobbies", skin);
		refreshLobbysButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				refreshLobbies();
			}
		});
		buttonGroup.padTop(5);
		buttonGroup.addActor(refreshLobbysButton);

		refreshLobbies();

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
	 * @param lobbyListTable
	 */
	private void refreshLobbies() {
//		steam.getSmm().addRequestLobbyListStringFilter(SteamHelper.LOBBY_KEY_MAGIC, SteamHelper.LOBBY_VALUE_MAGIC,
//				LobbyComparison.Equal);
		steam.getSmm().addRequestLobbyListResultCountFilter(5);

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
		if (arg instanceof LobbyDataList) {
			LobbyDataList lobbyDataList = (LobbyDataList) arg;
			updateLobbyList(lobbyDataList);
		}
	}

	private void updateLobbyList(LobbyDataList lobbyDataList) {
		List<LobbyData> datas = lobbyDataList.lobbyList;
		lobbyListTable.clearChildren();
		for (LobbyData d : datas) {
			String lobbyInfo = String.format("%s (#members:%s )", d.name, d.numMembers);
			lobbyListTable.add(new Label(lobbyInfo, skin));

			TextButton joinLobbyBtn = new TextButton("Join", skin);
			joinLobbyBtn.pad(10);
			joinLobbyBtn.setUserObject(d.lobbyID);
			joinLobbyBtn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					SteamID id = (SteamID) joinLobbyBtn.getUserObject();
					ScreenManager.getInstance().setScreen(Screens.STEAM_LOBBY);
					steam.getSmm().joinLobby(id);
				}
			});
			lobbyListTable.add(joinLobbyBtn);
			lobbyListTable.row();
		}
	}

}
