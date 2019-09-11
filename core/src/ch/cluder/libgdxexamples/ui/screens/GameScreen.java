package ch.cluder.libgdxexamples.ui.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ch.cluder.libgdxexamples.Debugger;
import ch.cluder.libgdxexamples.input.UIInputController;
import ch.cluder.libgdxexamples.net.NetwerkServer;
import ch.cluder.libgdxexamples.net.NetworkClient;
import ch.cluder.libgdxexamples.util.ResourceManager;

public class GameScreen implements Screen {
	// perspective 3d camera
	public PerspectiveCamera cam3d;
	public CameraInputController inputController;

	// axes / grid (from BaseG3dTest)
	final float GRID_MIN = -10f;
	final float GRID_MAX = 10f;
	final float GRID_STEP = 1f;
	public ModelBatch modelBatch;
	public Model axesModel;
	public ModelInstance axesInstance;
	public Model shipModel;
	public ModelInstance shipInstance;

	// chat
	Stage uiStage;
	public TextField chatField;
	public TextArea chatArea;

	private Image uiTextureImage;
	private InputMultiplexer inputMultiplexer;

	boolean isServer = false;
	NetwerkServer server;

	public NetworkClient netClient;
	public String playerName = "Player";

	public GameScreen() {
		create();
	}

	public GameScreen(boolean startServer) {
		create();

		isServer = true;
		server = new NetwerkServer();
		server.start();

		netClient = new NetworkClient(Gdx.net.newClientSocket(Protocol.TCP, "localhost", 5555, null));
	}

	public GameScreen(Socket clientSocket) {
		create();
		this.netClient = new NetworkClient(clientSocket);
	}

	private void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		// ui stage
		uiStage = new Stage(new ScreenViewport());

		// init chat listener and fields
		Skin defaultSkin = ResourceManager.getSkin();

		chatField = new TextField("", defaultSkin);
		chatField.setWidth(200);
		chatField.setMessageText("type to chat");
		uiStage.addActor(chatField);

		chatArea = new TextArea("", defaultSkin);
		chatArea.setWidth(200);
		chatArea.setPrefRows(5);
		chatArea.setHeight(chatArea.getPrefHeight());
		uiStage.addActor(chatArea);

		Texture uiTexture = new Texture("text.png");
		uiTextureImage = new Image(uiTexture);
		uiStage.addActor(uiTextureImage);

		// model batch used to render 3d objects
		modelBatch = new ModelBatch();

		// create a 3d camera
		cam3d = new PerspectiveCamera(67, width, height);
		cam3d.position.set(10f, 10f, 10f);
		cam3d.lookAt(0, 0, 0);
		cam3d.near = 0.1f;
		cam3d.far = 1000f;
		cam3d.update();

		// load 3d model
		AssetManager mgr = new AssetManager();
		mgr.load("ship/ship.g3dj", Model.class);
		mgr.finishLoading();

		shipModel = mgr.get("ship/ship.g3dj");
		shipInstance = new ModelInstance(shipModel);
		shipInstance.transform.scl(5);
		shipInstance.transform.translate(0.5f, 0.5f, 0);

		inputMultiplexer = new InputMultiplexer();
		inputController = new CameraInputController(cam3d);
		inputMultiplexer.addProcessor(inputController);
		inputMultiplexer.addProcessor(new UIInputController(this));

		Gdx.input.setInputProcessor(inputMultiplexer);

		// debug axes / grid
		createGridid();

		updateUIPosition();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		// clear screen and paint a black background
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		uiStage.act();
		uiStage.draw();

		handleServerCommunication();

		// draw axes/grid
		modelBatch.begin(cam3d);
		modelBatch.render(axesInstance);
		modelBatch.render(shipInstance);
		modelBatch.end();

		Debugger.printDebugInfo();
		shipInstance.transform.rotate(0, 1, 0, 0.5f);
	}

	private void handleServerCommunication() {
		if (netClient == null) {
			return;
		}
		String response = netClient.getServerResponse();
		if (response == null) {
			return;
		}

		Gdx.app.log("GameScreen", "adding chatline:" + response);
		addChatLine("unknown player", response);

	}

	@Override
	public void resize(int width, int height) {

		// update 2d stage
		uiStage.getViewport().update(width, height, true);

		updateUIPosition();

		// update 3d camera
		cam3d.viewportHeight = height;
		cam3d.viewportWidth = width;
		cam3d.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		if (server != null) {
			server.stop();
		}
		dispose();
	}

	@Override
	public void dispose() {
		uiStage.dispose();
		axesModel.dispose();
		modelBatch.dispose();
		shipModel.dispose();
	}

	/**
	 * Sets/updates the position of the UI elements.
	 */
	private void updateUIPosition() {
		int yPos = (int) (Gdx.app.getGraphics().getHeight() - uiTextureImage.getHeight());
		uiTextureImage.setPosition(Gdx.app.getGraphics().getWidth() - uiTextureImage.getWidth(), yPos);
		yPos = (int) (Gdx.graphics.getHeight() * 0.15f);
		chatField.setPosition(Gdx.graphics.getWidth() * 0.01f, yPos);
		chatArea.setPosition(Gdx.graphics.getWidth() * 0.01f, yPos + chatField.getHeight());
	}

	/**
	 * Grid with debug axis copied from BaseG3dTest.
	 */
	private void createGridid() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked,
				new Material());
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		builder = modelBuilder.part("axes", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 100, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 100, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 100);
		axesModel = modelBuilder.end();
		axesInstance = new ModelInstance(axesModel);
	}

	public String addChatLine(String name, String text) {
		chatArea.appendText(name + ":" + text + "\n");
		chatField.setText("");
		return text;
	}

}
