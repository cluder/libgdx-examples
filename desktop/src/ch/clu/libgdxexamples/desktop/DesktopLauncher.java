package ch.clu.libgdxexamples.desktop;

import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import ch.clu.libgdxexamples.LibGDXExamples;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800, 600);
		config.setTitle("libGDX Examples");
		config.setResizable(true);

		// start on left screen
		Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
		int xOffset = 0;
		if (monitors.length > 1) {
			xOffset = -400;
		}
		config.setWindowPosition(xOffset, 400);

		new Lwjgl3Application(new LibGDXExamples(), config);
	}
}
