package ch.clu.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;

import ch.clu.libgdxexamples.ui.screens.util.ScreenManager;
import ch.clu.libgdxexamples.util.Debugger;
import ch.clu.libgdxexamples.util.ResourceManager;

public class JoinMultiplayerScreen extends BaseUIScreen {
	String STATUS_NOT_CONNECTED = "not connected";
	String STATUS_CONNECTED = "connected";
	String STATUS_CONNECTING = "connecting ...";
	String STATUS_NO_SERVER = "no server found";

	String status = "Idle ...";
	TextField ipField;
	Socket clientSocket;
	private Label statusLabel;

	public JoinMultiplayerScreen() {
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
				ScreenManager.getInstance().setScreen(Screens.MULTIPLAYER);
			}
		});
		table.add(backButton).minWidth(150).minHeight(50).padTop(50);

		TextButton applyButton = new TextButton("Connect", defaultSkin);
		applyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					// TODO client can connect multiple times (Game <-> Menu handling)
					Gdx.app.log("MP", "connecting ...");
					setStatus(STATUS_CONNECTING);
					Socket clientSocket = Gdx.net.newClientSocket(Protocol.TCP, ipField.getText(), 5555, null);

					Gdx.app.log("MP", "connected");
					setStatus(STATUS_CONNECTED);
					GameScreen newGame = (GameScreen) Screens.GAME.get();
					newGame.setNetworkClient(clientSocket);
					ScreenManager.getInstance().setScreen(newGame);

				} catch (Exception e) {
					setStatus(STATUS_NO_SERVER);
					Gdx.app.log("MP", "cannot connect ", e);
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
//		dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.debug("DBG", "Key down:" + keycode);

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
			break;
		default:
			break;
		}
		return false;
	}
}
