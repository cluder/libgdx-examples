package ch.clu.libgdxexamples.screens.menu;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.codedisaster.steamworks.SteamID;

import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyChatMessage;
import ch.clu.libgdxexamples.util.ResourceManager;

public class ChatArea extends InputAdapter implements Observer {

	private Deque<String> messages = new ArrayDeque<>();
	private int maxSize = 10;

	VerticalGroup vGroup;
	TextArea textArea;
	TextField textField;

	public ChatArea() {

		SteamHelper.get().addObserver(this);
		Skin skin = ResourceManager.getSkin();

		// vertical group
		vGroup = new VerticalGroup();

		// text field containing previous chat
		textArea = new TextArea("", skin);
		textArea.setPrefRows(10);
		textArea.setDisabled(true);
		textArea.setSize(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getWidth() * 0.5f);
		vGroup.addActor(textArea);

		// input field
		textField = new TextField("", skin);
		vGroup.addActor(textField);

	}

	public Actor getActor() {
		return vGroup;
	}

	// input processor
	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.ESCAPE:

			break;
		case Keys.ENTER:
			String text = textField.getText();

			messages.add(text);
			if (messages.size() > maxSize) {
				messages.removeFirst();
			}

			SteamID steamIDLobby = SteamHelper.get().getSteamIDLobby();
			SteamHelper.get().getSmm().sendLobbyChatMsg(steamIDLobby, text);

//			textArea.appendText(text + "\n");
			textArea.clear();
			for (String s : messages) {
				textArea.appendText(s + "\n");
			}

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
