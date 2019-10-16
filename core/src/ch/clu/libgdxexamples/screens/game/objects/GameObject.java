package ch.clu.libgdxexamples.screens.game.objects;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public abstract class GameObject extends InputAdapter {
	public final Model model;
	public final btRigidBody object;
	public final ModelInstance modelInstance;
	public boolean moving = true;
	public MotionState motionState;

	abstract Model getModel();

	abstract btCollisionShape getCollisionShape();

	abstract void update(float deltaTime);

	public GameObject(float x, float y, float z) {
		model = getModel();
		btCollisionShape shape = getCollisionShape();

		object = createCollisionObject(shape);
		modelInstance = new ModelInstance(model);
		object.setWorldTransform(modelInstance.transform);
		modelInstance.transform.setToTranslation(x, y, z);

		motionState = new MotionState(modelInstance.transform);
		object.setMotionState(motionState);

	}

	protected btRigidBody createCollisionObject(btCollisionShape btShape) {
		btRigidBody btObject = new btRigidBody(getMass(), null, btShape);
		btObject.setCollisionFlags(
				btObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		return btObject;
	}

	protected abstract float getMass();

	public void dispose() {
		object.getCollisionShape().dispose();
		object.dispose();
		model.dispose();
	}

	public ModelInstance getModelInstance() {
		return modelInstance;
	}

	static class MotionState extends btMotionState {
		private final Matrix4 transform;

		public MotionState(final Matrix4 transform) {
			this.transform = transform;
		}

		/**
		 * For dynamic and static bodies this method is called by bullet once to get the
		 * initial state of the body. For kinematic bodies this method is called on
		 * every update, unless the body is deactivated.
		 */
		@Override
		public void getWorldTransform(final Matrix4 worldTrans) {
			worldTrans.set(transform);
		}

		/**
		 * For dynamic bodies this method is called by bullet every update to inform
		 * about the new position and rotation.
		 */
		@Override
		public void setWorldTransform(final Matrix4 worldTrans) {
			transform.set(worldTrans);
		}
	}

}
