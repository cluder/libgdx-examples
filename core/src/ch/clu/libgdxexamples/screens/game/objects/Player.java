package ch.clu.libgdxexamples.screens.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

// extends model instance
public class Player extends GameObject {
	float fallSpeed = 1;

	public Player() {
	}

	@Override
	Model getModel() {
		ModelBuilder mb = new ModelBuilder();
		return mb.createSphere(1, 1, 1, 10, 10, //
				new Material(ColorAttribute.createDiffuse(Color.GREEN)), //
				ColorAttribute.Diffuse);
	}

	@Override
	btCollisionObject getObject() {
		btCollisionObject btObject = new btCollisionObject();
		btObject.setCollisionShape(new btSphereShape(0.5f));
		btObject.setCollisionFlags(
				btObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

		return btObject;
	}

	public void reset() {
		modelInstance.transform.setTranslation(0, 5, 0);
	}

	@Override
	public void update(float deltaTime) {
		if (!moving) {
			return;
		}
		fallSpeed = -0.5f;
		float y = fallSpeed * deltaTime;
		modelInstance.transform.translate(0, y, 0);
		object.setWorldTransform(modelInstance.transform);
	}

}
