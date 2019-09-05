package ch.cluder.libgdxexamples.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import ch.cluder.libgdxexamples.ui.screens.MainMenuScreen;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;

public class UIInputController implements InputProcessor {

	TextField chatField;
	TextArea chatArea;

	public UIInputController(TextField chatField, TextArea chatArea) {
		this.chatField = chatField;
		this.chatArea = chatArea;
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.debug("DBG", "Key down:" + keycode);

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(new MainMenuScreen());
			break;
		case Keys.ENTER:
			if (chatArea.getLines() > 5) {
				String s = chatArea.getText().substring(chatArea.getText().indexOf('\n') + 1);
				chatArea.setText(s);
			}
			chatArea.appendText(chatField.getText() + "\n");
			chatField.setText("");
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
		Gdx.app.debug("DBG", "Key typed:" + character);

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
