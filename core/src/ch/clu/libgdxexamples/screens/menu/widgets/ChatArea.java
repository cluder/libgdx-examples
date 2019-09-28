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

	public ChatArea() {

		SteamHelper.get().addObserver(this);
		Skin skin = ResourceManager.getSkin();

		// vertical group
		mainTable = new Table(skin);
		textArea = new TextArea("", skin);

		mainTable.add(textArea).width(Gdx.graphics.getWidth() * 0.4f).height(Gdx.graphics.getHeight() * 0.5f);
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

	// input processor
	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.ESCAPE:

			break;
		case Keys.ENTER:
			String text = textField.getText();
//
//			messages.add(text);
//			if (messages.size() > maxSize) {
//				messages.removeFirst();
//			}

			SteamID steamIDLobby = SteamHelper.get().getSteamIDLobby();
			SteamHelper.get().getSmm().sendLobbyChatMsg(steamIDLobby, text);

//			textArea.appendText(text + "\n");
//			textArea.clear();
//			for (String s : messages) {
//				textArea.appendText(s + "\n");
//			}

			textField.setText("");

			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LobbyChatMessage) {
			LobbyChatMessage lobbyChatMessage = (LobbyChatMessage) arg;
			textArea.appendText(lobbyChatMessage.username + ": " + lobbyChatMessage.msg + "\n");
		}
	}
}
