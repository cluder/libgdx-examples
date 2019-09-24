package ch.clu.libgdxexamples.util;

import com.badlogic.gdx.Gdx;

public class Debugger {
	static long lastUpdate = System.currentTimeMillis();
	static boolean enabled = false;

	/**
	 * Prints the heap usage at a specific interval.
	 */
	public static void printDebugInfo() {
		if (!enabled) {
			return;
		}
		if (lastUpdate + 10000 < System.currentTimeMillis()) {
			long javaHeap = Gdx.app.getJavaHeap();
			long nativeHeap = Gdx.app.getNativeHeap();
			Gdx.app.debug("DBG", String.format("heap:%.2f MB (native:%.2f MB)", javaHeap / 10024.0 / 1024.0,
					+nativeHeap / 1024.0 / 1024.0));

			lastUpdate = System.currentTimeMillis();
		}
	}
}
