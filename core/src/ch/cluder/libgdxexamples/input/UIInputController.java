package ch.cluder.libgdxexamples.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import ch.cluder.libgdxexamples.net.NetworkClient;
import ch.cluder.libgdxexamples.ui.screens.GameScreen;
import ch.cluder.libgdxexamples.ui.screens.MainMenuScreen;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;

public class UIInputController implements InputProcessor {

	TextField chatField;
	TextArea chatArea;
	NetworkClient netClient;
	GameScreen game;

	public UIInputController(GameScreen game) {
		this.game = game;
		this.chatField = game.chatField;
		this.chatArea = game.chatArea;
		this.netClient = game.netClient;
	}

	@Override
	public boolean keyDown(int keycode) {
//		Gdx.app.debug("DBG", "Key down:" + keycode);

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(new MainMenuScreen());
			break;
		case Keys.ENTER:
			if (chatArea.getLines() > 5) {
				// limit text in text area to 5 lines
				String s = chatArea.getText().substring(chatArea.getText().indexOf('\n') + 1);
				chatArea.setText(s);
			}
			String text = chatField.getText();
			chatArea.appendText(game.playerName + ":" + text + "\n");
			chatField.setText("");
			if (netClient != null) {
				netClient.sendChat(text);
			}
			break;
		case Keys.FORWARD_DEL:
			chatArea.setText("");
			chatField.setText("");
			break;
		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Gdx.app.debug("DBG", "Key up:" + keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
//		Gdx.app.debug("DBG", "Key typed:" + character);

		switch (character) {
		case '\b':
			removeLastCharacter(chatField);
			break;
		case Keys.ENTER:
			chatField.setText("");
			break;
		case 127:
			// DEL
			break;
		default:
			// by default, append the typed character to the textfield
			chatField.appendText("" + character);
			break;
		}

		return false;
	}

	private void removeLastCharacter(TextField chatField) {
		String currentText = chatField.getText();
		if (currentText.isEmpty() == false) {
			String newText = currentText.substring(0, currentText.length() - 1);
			chatField.setText(newText);
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
