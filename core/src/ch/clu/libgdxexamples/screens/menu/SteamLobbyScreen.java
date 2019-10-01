package ch.clu.libgdxexamples.screens.menu;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ch.clu.libgdxexamples.net.packets.Messages;
import ch.clu.libgdxexamples.screens.menu.widgets.ChatArea;
import ch.clu.libgdxexamples.screens.util.BaseUIScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyChatUpdate;
import ch.clu.libgdxexamples.steam.data.LobbyData;
import ch.clu.libgdxexamples.steam.data.LobbyMember;
import ch.clu.libgdxexamples.util.NetworkUtil;
import ch.clu.libgdxexamples.util.ScreenManager;

@SuppressWarnings("deprecation")
public class SteamLobbyScreen extends BaseUIScreen implements Observer {
	String tag = SteamLobbyScreen.class.getSimpleName();
	VerticalGroup mainVertGroup;
	HorizontalGroup horizGroup;

	Label currentLobbyLabel;
	Table lobbyMemberTable;

	ChatArea chatArea;

	/**
	 * Observer update.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// lobby joined
		if (arg instanceof LobbyData) {
			LobbyData data = (LobbyData) arg;
			updateCurrentLobby(data);
		}
		if (arg instanceof LobbyChatUpdate) {
			LobbyChatUpdate data = (LobbyChatUpdate) arg;
			chatArea.addMessage(data.name + " " + data.action);
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);

	}

	@Override
	protected void create() {
		clear();

		SteamHelper.get().addObserver(this);

		mainVertGroup = new VerticalGroup();
		mainVertGroup.setFillParent(true);
		mainVertGroup.align(Align.center);
		mainVertGroup.space(5);

		addActor(mainVertGroup);

		horizGroup = new HorizontalGroup();
		horizGroup.align(Align.left);
		horizGroup.space(5);

		// lobby label
		currentLobbyLabel = new Label("Current Lobby: none", skin);
		mainVertGroup.addActor(currentLobbyLabel);

		// horiz group containing members and chat
		mainVertGroup.addActor(horizGroup);

		// left: member table
		Table lobbyMemberTableContainer = new Table(skin);
		lobbyMemberTable = new Table(skin);
		ScrollPane lobbyMemberTableScrollPane = new ScrollPane(lobbyMemberTable, skin);
		lobbyMemberTableScrollPane.setFadeScrollBars(false);
		lobbyMemberTableContainer.add(lobbyMemberTableScrollPane) //
				.height(getHeight() * 0.5f).width(getWidth() * 0.2f);

		horizGroup.addActor(lobbyMemberTableContainer);

		// right: vertical group with chat area and field
		chatArea = new ChatArea();
		horizGroup.addActor(chatArea.getActor());

		HorizontalGroup buttonGroup = new HorizontalGroup();
		buttonGroup.padTop(5);
		mainVertGroup.addActor(buttonGroup);

		Button backBtn = new TextButton("Back", skin);
		buttonGroup.addActor(backBtn);
		backBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SteamHelper.get().leaveLobby();
				ScreenManager.getInstance().setScreen(Screens.LIST_STEAM_LOBBY);
			}
		});

		Button startGame = new TextButton("Start Game", skin);
		buttonGroup.addActor(startGame);
		startGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				NetworkUtil.sendToLobbyMembers(Messages.START_GAME);
			}
		});
		setDebugAll(false);
	}

	@Override
	protected void addInputProcessor(InputMultiplexer inputMultiplexer) {
		super.addInputProcessor(inputMultiplexer);

		inputMultiplexer.addProcessor(chatArea);

	}

	private void updateCurrentLobby(LobbyData data) {
		currentLobbyLabel.setText(data.name);

		lobbyMemberTable.clear();

		for (LobbyMember m : data.members) {
			lobbyMemberTable.add(new Label(m.name, skin));
			lobbyMemberTable.row();
		}
	}

	@Override
	protected Screens getPreviousScreen() {
		return Screens.LIST_STEAM_LOBBY;
	}

}
