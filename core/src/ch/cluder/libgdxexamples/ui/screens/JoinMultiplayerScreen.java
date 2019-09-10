package ch.cluder.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;

import ch.cluder.libgdxexamples.Debugger;
import ch.cluder.libgdxexamples.ui.screens.util.ScreenManager;
import ch.cluder.libgdxexamples.util.ResourceManager;

public class JoinMultiplayerScreen extends BaseUIScreen {
	String STATUS_NOT_CONNECTED = "not connected";
	String CONNECTED = "connected";
	String CONNECTING = "connecting ...";

	String status = "Idle ...";
	TextField ipField;
	Socket clientSocket;
	private Label statusLabel;
	String playerName;

	public JoinMultiplayerScreen(String name) {
		this.playerName = name;
	}

	@Override
	protected void create() {

		clear();
		Gdx.app.setLogLevel(Logger.DEBUG);
		Skin defaultSkin = ResourceManager.getSkin();

		// button table
		Table table = new Table(defaultSkin);
		addActor(table);

		table.setFillParent(true);
		table.setSkin(defaultSkin);

		Label ipLabel = new Label("Server IP", defaultSkin);
		table.add(ipLabel);

		ipField = new TextField("<IP>", defaultSkin);
		ipField.setText("localhost");
		table.add(ipField);

		table.row();

		TextButton backButton = new TextButton("Back", defaultSkin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().setScreen(new MultiplayerScreen());
			}
		});
		table.add(backButton).minWidth(150).minHeight(50).padTop(50);

		TextButton applyButton = new TextButton("Connect", defaultSkin);
		applyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					Gdx.app.log("MP", "connecting ...");
					setStatus(CONNECTING);
					SocketHints hints = new SocketHints();
					hints.connectTimeout = 5000;
					Socket clientSocket = Gdx.net.newClientSocket(Protocol.TCP, ipField.getText(), 5555, hints);

					Gdx.app.log("MP", "connected");
					setStatus(CONNECTED);
					GameScreen newGame = new GameScreen(clientSocket);
					newGame.playerName = playerName;
					ScreenManager.getInstance().setScreen(newGame);

				} catch (Exception e) {
					Gdx.app.log("MP", "cannot connect ", e);
					setStatus(STATUS_NOT_CONNECTED);
				}

			}
		});
		table.add(applyButton).minWidth(150).minHeight(50).padTop(50);

		table.row();

		statusLabel = new Label("Status: " + status, defaultSkin);
		table.add(statusLabel);

		setStatus(STATUS_NOT_CONNECTED);
//		table.debug();

	}

	private void setStatus(String status) {
		statusLabel.setText(status);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		act();
		draw();

		Debugger.printDebugInfo();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.debug("DBG", "Key down:" + keycode);

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(new MainMenuScreen());
			break;
		default:
			break;
		}
		return false;
	}
}
