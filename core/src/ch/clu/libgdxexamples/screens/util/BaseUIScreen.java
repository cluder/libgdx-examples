package ch.clu.libgdxexamples.screens.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.codedisaster.steamworks.SteamAPI;

import ch.clu.libgdxexamples.util.Debugger;
import ch.clu.libgdxexamples.util.ResourceManager;
import ch.clu.libgdxexamples.util.ScreenManager;

public abstract class BaseUIScreen extends Stage implements Screen {
	Screens previousScreen = null;
	protected Skin skin = ResourceManager.getSkin();

	public BaseUIScreen() {
		super(new ScreenViewport());
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		create();
	}

	abstract protected void create();

	abstract protected Screens getPreviousScreen();

	@Override
	public void render(float delta) {
		SteamAPI.runCallbacks();

		Debugger.printDebugInfo();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		act();
		draw();

	}

	@Override
	public void resize(int width, int height) {
//		Gdx.app.log(this.getClass().getName(), "resize (" + width + "x" + height + ")");
		getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	protected TextButton addButton(String text, Skin defaultSkin, Table buttonTable) {
		int buttonWidth = 200;
		TextButton btn = new TextButton(text, defaultSkin);
		buttonTable.add(btn).center().fillX().padTop(20).width(buttonWidth).height(70);
		return btn;
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.ESCAPE:
			ScreenManager.getInstance().setScreen(getPreviousScreen());
			break;
		default:
			break;
		}
		return false;
	}

}
