package ch.clu.libgdxexamples.screens.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.codedisaster.steamworks.SteamAPI;

import ch.clu.libgdxexamples.screens.game.objects.Floor;
import ch.clu.libgdxexamples.screens.game.objects.Player;
import ch.clu.libgdxexamples.screens.game.physics.PhysicsWorld;
import ch.clu.libgdxexamples.screens.menu.widgets.ChatArea;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.util.Debugger;
import ch.clu.libgdxexamples.util.ScreenManager;

public class GameScreen extends InputAdapter implements Screen {
	String tag = GameScreen.class.getSimpleName();
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

	Environment env;

	// chat
	Stage uiStage;
	public ChatArea chatArea;

	private Image uiTextureImage;
	private InputMultiplexer inputMultiplexer;

	Player player;
	private Floor floor;

	boolean debug = false;

	PhysicsWorld physicsWorld;

	public GameScreen() {
		create();
	}

	private void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		physicsWorld = new PhysicsWorld();

		// ui stage
		uiStage = new Stage(new ScreenViewport());

		// init chat listener and fields
		float chatWidth = Gdx.graphics.getWidth() * 0.3f;
		float chatHeight = Gdx.graphics.getHeight() * 0.2f;

		chatArea = new ChatArea(chatWidth, chatHeight);
		Actor actor = chatArea.getActor();
		actor.setPosition(chatWidth / 2 + 20, chatHeight / 2 + 50);
		uiStage.addActor(actor);

		Texture uiTexture = new Texture("text.png");
		uiTextureImage = new Image(uiTexture);
		uiStage.addActor(uiTextureImage);

		// model batch used to render 3d objects
		modelBatch = new ModelBatch();

		// create a 3d camera
		cam3d = new PerspectiveCamera(67, chatWidth, chatHeight);
		cam3d.position.set(10f, 10f, 10f);
		cam3d.lookAt(0, 0, 0);
		cam3d.near = 0.1f;
		cam3d.far = 1000f;
		cam3d.update();

		// light
		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		env.set(new ColorAttribute(ColorAttribute.Specular, 0.2f, 0.1f, 0.7f, 1f));
		env.add(new DirectionalLight().set(0.7f, 0.7f, 0.7f, -1f, -0.8f, -0.2f));

		player = new Player(0, 5, 0);
		floor = new Floor();

		physicsWorld.add(floor);
		physicsWorld.add(player);

		// debug axes / grid
		createGrid();

		updateUIPosition();

		// remove focus from text field when something else is clicked
		uiStage.getRoot().addCaptureListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor target = event.getTarget();
				if (!(target instanceof TextField))
					uiStage.setKeyboardFocus(null);
				return false;
			}
		});

		physicsWorld.setDebug(true);

	}

	@Override
	public void show() {

		inputMultiplexer = new InputMultiplexer();
		inputController = new CameraInputController(cam3d);
		inputMultiplexer.addProcessor(inputController);
		inputMultiplexer.addProcessor(chatArea);
		inputMultiplexer.addProcessor(uiStage);
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(player);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		SteamAPI.runCallbacks();

		// clear screen and paint a black background
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// update physics
		physicsWorld.update(delta);

		// update player and stages
		uiStage.act(delta);
//		player.update(delta);

		// draw axes/grid
		modelBatch.begin(cam3d);
		modelBatch.render(axesInstance, env);
		modelBatch.render(floor.getModelInstance(), env);
		modelBatch.render(player.getModelInstance(), env);
		physicsWorld.render(modelBatch);
		modelBatch.end();

		// draw ui
		uiStage.draw();

		Debugger.printDebugInfo();

//		checkCollision(player.object, floor.object);
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
//		dispose();
	}

	@Override
	public void dispose() {
		uiStage.dispose();
		axesModel.dispose();
		modelBatch.dispose();

		floor.dispose();
		player.dispose();

		physicsWorld.dispose();
	}

	/**
	 * Sets/updates the position of the UI elements.
	 */
	private void updateUIPosition() {
		int yPos = (int) (Gdx.app.getGraphics().getHeight() - uiTextureImage.getHeight());
		uiTextureImage.setPosition(Gdx.app.getGraphics().getWidth() - uiTextureImage.getWidth(), yPos);
		yPos = (int) (Gdx.graphics.getHeight() * 0.15f);
	}

	/**
	 * Grid with debug axis copied from BaseG3dTest.
	 */
	private void createGrid() {
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

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
			break;
		case Keys.R:
			// reset
			player.object.clearForces();
			player.reset();
			break;
		case Keys.D:
			// reset
			debug = !debug;
			break;
		case Keys.SPACE:
			// reset
			player.object.activate();
			player.object.applyCentralForce(new Vector3(0, 1, 0).scl(30));
			Gdx.app.log(GameScreen.class.getSimpleName(), "Applying force");
			break;
		default:
			break;
		}
		return false;
	}

}
