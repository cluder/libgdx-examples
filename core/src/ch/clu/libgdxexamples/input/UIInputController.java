package ch.clu.libgdxexamples.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import ch.clu.libgdxexamples.data.GameData;
import ch.clu.libgdxexamples.screens.game.GameScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.util.ScreenManager;

public class UIInputController extends InputAdapter {

	GameScreen game;

	public UIInputController(GameScreen game) {
		this.game = game;
	}

	@Override
	public boolean keyDown(int keycode) {
		TextArea chatArea = game.chatArea;

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
			break;
		case Keys.ENTER:

			if (chatArea.getLines() > 5) {
				// limit text in text area to 5 lines
				String s = chatArea.getText().substring(chatArea.getText().indexOf('\n') + 1);
				chatArea.setText(s);
			}

			String text = game.addChatLine(GameData.get().playerName, game.chatField.getText());

			break;
		case Keys.FORWARD_DEL:
			chatArea.setText("");
			game.chatField.setText("");
			break;
		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		TextField chatField = game.chatField;
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

}
