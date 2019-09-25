package ch.clu.libgdxexamples.screens.menu;

import java.util.LinkedHashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;

import ch.clu.libgdxexamples.screens.util.BaseUIScreen;
import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.util.ScreenManager;
import ch.clu.libgdxexamples.util.WindowMode;

public class SettingsScreen extends BaseUIScreen {
	SelectBox<String> resolutionDropDown;
	SelectBox<String> windowMode;

	@Override
	protected Screens getPreviousScreen() {
		return Screens.MAIN_MENU;
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

		// get possible display modes
		LinkedHashSet<String> modes = new LinkedHashSet<String>();
		final DisplayMode[] displayModes = Gdx.graphics.getDisplayModes();
		for (DisplayMode displayMode : displayModes) {
			modes.add(displayMode.width + "x" + displayMode.height);
		}

		// resolution drop down
		table.add("Resolution").left().padTop(20).padRight(20);
		resolutionDropDown = new SelectBox<String>(skin);
		resolutionDropDown.setItems(modes.toArray(new String[modes.size()]));
		resolutionDropDown.setSelected(Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
		table.add(resolutionDropDown).left().padTop(20).padRight(20);
		table.row();

		// window mode drop down
		table.add("Window Mode").left().padTop(20).padRight(20);
		windowMode = new SelectBox<String>(skin);
		windowMode.setItems(WindowMode.BORDERLESS.toString(), WindowMode.WINDOWED.toString());
		windowMode.setSelected(WindowMode.WINDOWED.toString());
		table.add(windowMode).left().padTop(20).padRight(20);
		table.row();

		table.add("Volume").left().padTop(20).padRight(20);
		Slider volumeSlider = new Slider(0, 100, 5, false, skin);
		volumeSlider.setValue(100);
		table.add(volumeSlider).left().padTop(20).padRight(20);
		table.row();

		// apply/back button

		TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().setScreen(Screens.MAIN_MENU);
			}
		});
		table.add(backButton).minWidth(150).minHeight(50).padTop(50);

		TextButton applyButton = new TextButton("Apply", skin);
		applyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				applyChanges();
			}
		});
		table.add(applyButton).minWidth(150).minHeight(50).padTop(50);

//		table.debug();

	}

	private void applyChanges() {
		// apply window settings
		String selectedRes = resolutionDropDown.getSelected();
		int wWidth = Integer.valueOf(selectedRes.substring(0, selectedRes.indexOf("x")));
		int wHeight = Integer.valueOf(selectedRes.substring(selectedRes.indexOf("x") + 1, selectedRes.length()));

		String selectedWindowMode = windowMode.getSelected();
		WindowMode newMode = WindowMode.fromLabel(selectedWindowMode);
		switch (newMode) {
		case WINDOWED:
			Gdx.graphics.setUndecorated(false);
			Gdx.graphics.setWindowedMode(wWidth, wHeight);
			break;
		case BORDERLESS:
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(wWidth, wHeight);
			break;
		default:
			break;
		}

		// TODO apply sound settings

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
