package ch.clu.libgdxexamples.screens.game.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class Floor extends GameObject {

	public Floor() {
	}

	@Override
	Model getModel() {
		String floorFbx = "level1/floor.obj";
		AssetManager mgr = new AssetManager();
		mgr.load(floorFbx, Model.class);
		mgr.finishLoading();

		Model model = mgr.get(floorFbx);
		return model;
	}

	@Override
	btCollisionObject getObject() {
		btCollisionShape btShape = new btBoxShape(new Vector3(10, 0.3f, 10));

		btCollisionObject btObject = new btCollisionObject();
		btObject.setCollisionShape(btShape);
		btObject.setCollisionFlags(
				btObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		return btObject;
	}

	@Override
	void update(float deltaTime) {

	}

}
