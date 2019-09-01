package ch.cluder.libgdxexamples.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import ch.cluder.libgdxexamples.LibGDXExamples;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800, 600);
		config.setTitle("libGDX Examples");
		config.setResizable(true);

		new Lwjgl3Application(new LibGDXExamples(), config);
	}
}
