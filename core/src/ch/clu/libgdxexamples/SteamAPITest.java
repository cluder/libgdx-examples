package ch.clu.libgdxexamples;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamApps;

import ch.clu.libgdxexamples.util.SteamHelper;

public class SteamAPITest {

	public static void main(String[] args) {
		SteamHelper sh = SteamHelper.get();
		sh.initSteam();

		if (SteamAPI.isSteamRunning()) {
			System.out.println("Steam is running");
		}

		SteamApps apps = new SteamApps();
		String currentGameLanguage = apps.getCurrentGameLanguage();
		System.out.println(currentGameLanguage);

		SteamAPI.printDebugInfo(System.out);

		SteamAPI.shutdown();

	}

}
