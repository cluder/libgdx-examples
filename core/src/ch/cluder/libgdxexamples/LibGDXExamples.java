package ch.cluder.libgdxexamples;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ch.cluder.libgdxexamples.input.UIInputController;

public class LibGDXExamples extends ApplicationAdapter {
	SpriteBatch uiTextBatch;
	Texture uiText;

	OrthographicCamera cam2d;

	// perspective 3d camera
	public PerspectiveCamera cam3d;
	public CameraInputController inputController;

	// axes / grid (from BaseG3dTest)
	final float GRID_MIN = -10f;
	final float GRID_MAX = 10f;
	final float GRID_STEP = 1f;
	public Model axesModel;
	public ModelInstance axesInstance;
	public ModelBatch modelBatch;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		modelBatch = new ModelBatch();

		cam2d = new OrthographicCamera();
		cam2d.setToOrtho(false, 800, 600);

		// create a 3d camera
		cam3d = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam3d.position.set(10f, 10f, 10f);
		cam3d.lookAt(0, 0, 0);
		cam3d.near = 0.1f;
		cam3d.far = 1000f;
		cam3d.update();

		// create an input multiplexer to use multiple input handler
		// (1st person camera and our own UI input handler)
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputController = new CameraInputController(cam3d);
		inputMultiplexer.addProcessor(inputController);
		inputMultiplexer.addProcessor(new UIInputController());

		Gdx.input.setInputProcessor(inputMultiplexer);

		uiTextBatch = new SpriteBatch();
		uiText = new Texture("text.png");

		// debug axes / grid
		createAxes();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		// update 2d camera size
		cam2d.setToOrtho(false, width, height);
		cam2d.update();

		// update 3d camera
		cam3d.viewportHeight = height;
		cam3d.viewportWidth = width;
		cam3d.update();
	}

	@Override
	public void render() {
		// clear screen and paint a black background
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// draw axes/grid
		modelBatch.begin(cam3d);
		modelBatch.render(axesInstance);
		modelBatch.end();

		// draw text in upper left corner
		uiTextBatch.setProjectionMatrix(cam2d.combined);
		uiTextBatch.begin();
		int yPos = (int) (Gdx.app.getGraphics().getHeight() - uiText.getHeight());
		uiTextBatch.draw(uiText, Gdx.app.getGraphics().getWidth() - uiText.getWidth(), yPos);
		uiTextBatch.end();

		Debugger.printDebugInfo();
	}

	@Override
	public void dispose() {
		uiTextBatch.dispose();
		uiText.dispose();
	}

	/**
	 * Axses / Grid copied from BaseG3dTest.
	 */
	private void createAxes() {
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
}
