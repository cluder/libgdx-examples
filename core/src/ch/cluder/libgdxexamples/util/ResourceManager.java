package ch.cluder.libgdxexamples.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {

	public static Skin getSkin() {
//		String skinPath = "skins/flat/skin/skin.json";
//		String skinPath = "skins/commodore64/skin/uiskin.json";
//		String skinPath = "skins/shade/skin/uiskin.json";
		String skinPath = "skins/neon/skin/neon-ui.json";
		try {
			FileHandle internal = Gdx.files.internal(skinPath);
			return new Skin(internal);
		} catch (Exception e) {
			Gdx.app.log("ResourceManager", "failed to load skin: " + skinPath, e);
			return new Skin(Gdx.files.internal("uiskin.json"));
		}
	}

}
