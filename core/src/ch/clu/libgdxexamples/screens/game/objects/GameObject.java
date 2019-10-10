package ch.clu.libgdxexamples.screens.game.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public abstract class GameObject {
	public final Model model;
	public final btCollisionObject object;
	public final ModelInstance modelInstance;
	public boolean moving = true;

	abstract Model getModel();

	abstract btCollisionObject getObject();

	abstract void update(float deltaTime);

	public GameObject() {
		model = getModel();
		object = getObject();
		modelInstance = new ModelInstance(model);
		object.setWorldTransform(modelInstance.transform);

	}

	public void dispose() {
		object.getCollisionShape().dispose();
		object.dispose();
		model.dispose();
	}

	public ModelInstance getModelInstance() {
		return modelInstance;
	}

}
