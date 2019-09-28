package ch.clu.libgdxexamples.screens.menu;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ch.clu.libgdxexamples.screens.menu.widgets.ChatArea;
import ch.clu.libgdxexamples.screens.util.BaseUIScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyData;
import ch.clu.libgdxexamples.steam.data.LobbyMember;
import ch.clu.libgdxexamples.util.ScreenManager;

@SuppressWarnings("deprecation")
public class SteamLobbyScreen extends BaseUIScreen implements Observer {

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
		lobbyMemberTable = new Table(skin);
		horizGroup.addActor(lobbyMemberTable);

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
