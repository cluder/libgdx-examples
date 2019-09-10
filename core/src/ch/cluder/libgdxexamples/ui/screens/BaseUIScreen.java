package ch.cluder.libgdxexamples.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class BaseUIScreen extends Stage implements Screen {

	public BaseUIScreen() {
		super(new ScreenViewport());
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		create();
	}

	abstract protected void create();

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(this.getClass().getName(), "resize (" + width + "x" + height + ")");
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

}
