package ch.clu.libgdxexamples.screens.menu.widgets;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.codedisaster.steamworks.SteamID;

import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyChatMessage;
import ch.clu.libgdxexamples.util.ResourceManager;

@SuppressWarnings("deprecation")
public class ChatArea extends InputAdapter implements Observer {

	Table mainTable;
	TextArea textArea;
	TextField textField;

	public ChatArea(float width, float height) {
		init(width, height);
	}

	public ChatArea() {
		float width = Gdx.graphics.getWidth() * 0.4f;
		float height = Gdx.graphics.getHeight() * 0.5f;
		init(width, height);
	}

	private void init(float width, float height) {

		SteamHelper.get().addObserver(this);
		Skin skin = ResourceManager.getSkin();

		// vertical group
		mainTable = new Table(skin);
		textArea = new TextArea("", skin);

		mainTable.add(textArea).width(width).height(height);
		mainTable.row();

		// text field containing previous chat
		textArea.setPrefRows(10);
		textArea.setDisabled(true);

		// input field
		HorizontalGroup inputGroup = new HorizontalGroup();
		textField = new TextField("", skin);
		inputGroup.addActor(new Label("Chat: ", skin));
		inputGroup.addActor(textField);
		mainTable.add(inputGroup);
	}

	public Actor getActor() {
		return mainTable;
	}

	public TextField getTextField() {
		return textField;
	}

	public void addMessage(String msg) {
		textArea.appendText(msg + "\n");
	}

	// input processor
	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.ESCAPE:
//			textField.setDisabled(true);
			break;
		case Keys.ENTER:
			String text = textField.getText();

			SteamID steamIDLobby = SteamHelper.get().getSteamIDLobby();
			if (steamIDLobby != null)
				SteamHelper.get().getSmm().sendLobbyChatMsg(steamIDLobby, text);

			textField.setText("");
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);

		return false;

	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LobbyChatMessage) {
			LobbyChatMessage lobbyChatMessage = (LobbyChatMessage) arg;
			textArea.appendText(lobbyChatMessage.username + ": " + lobbyChatMessage.msg + "\n");
		}
	}
}
