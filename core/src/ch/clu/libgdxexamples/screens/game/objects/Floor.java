package ch.clu.libgdxexamples.screens.game.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class Floor extends GameObject {

	public Floor() {
		super(0, 0, 0);
		object.setCollisionFlags(object.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
	}

	@Override
	protected float getMass() {
		return 0;
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
	btCollisionShape getCollisionShape() {
		return new btBoxShape(new Vector3(10, 0.3f, 10));
	}

	@Override
	void update(float deltaTime) {

	}

}
