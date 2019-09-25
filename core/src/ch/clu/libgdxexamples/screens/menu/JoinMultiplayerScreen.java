package ch.clu.libgdxexamples.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;

import ch.clu.libgdxexamples.screens.util.BaseUIScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.util.ScreenManager;

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
	protected Screens getPreviousScreen() {
		return Screens.MULTIPLAYER;
	}

	@Override
	protected void create() {

		clear();
		Gdx.app.setLogLevel(Logger.DEBUG);

		// button table
		Table table = new Table(skin);
		addActor(table);

		table.setFillParent(true);
		table.setSkin(skin);

		Label ipLabel = new Label("Server IP", skin);
		table.add(ipLabel);

		ipField = new TextField("<IP>", skin);
		ipField.setText("localhost");
		table.add(ipField);

		table.row();

		TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().setScreen(getPreviousScreen());
			}
		});
		table.add(backButton).minWidth(150).minHeight(50).padTop(50);

		TextButton applyButton = new TextButton("Connect", skin);
		applyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				try {
//					// TODO client can connect multiple times (Game <-> Menu handling)
//					Gdx.app.log("MP", "connecting ...");
//					setStatus(STATUS_CONNECTING);
//					Socket clientSocket = Gdx.net.newClientSocket(Protocol.TCP, ipField.getText(), 5555, null);
//
//					Gdx.app.log("MP", "connected");
//					setStatus(STATUS_CONNECTED);
//					ScreenManager.getInstance().setScreen(Screens.GAME);
//
//				} catch (Exception e) {
//					setStatus(STATUS_NO_SERVER);
//					Gdx.app.log("MP", "cannot connect ", e);
//				}

			}
		});
		table.add(applyButton).minWidth(150).minHeight(50).padTop(50);

		table.row();

		statusLabel = new Label("Status: " + status, skin);
		table.add(statusLabel);

		setStatus(STATUS_NOT_CONNECTED);
//		table.debug();

	}

	private void setStatus(String status) {
		statusLabel.setText(status);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

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

}
