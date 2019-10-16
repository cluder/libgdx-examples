package ch.clu.libgdxexamples.screens.game.objects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

// extends model instance
public class Player extends GameObject {

	float mass = 1; // 5 kg

	boolean downPressed;
	boolean upPressed;
	boolean leftPressed;
	boolean rightPressed;

	public Player(float x, float y, float z) {
		super(x, y, z);

	}

	@Override
	protected float getMass() {

		return 0.1f;
	}

	@Override
	Model getModel() {
		ModelBuilder mb = new ModelBuilder();
		Model model = mb.createSphere(1, 1, 1, 10, 10, //
				new Material(ColorAttribute.createDiffuse(Color.GREEN)), //
				ColorAttribute.Diffuse);
		return model;
	}

	@Override
	btCollisionShape getCollisionShape() {
		return new btSphereShape(0.5f);
	}

	public void reset() {
		Vector3 x = new Vector3();
		Quaternion q = new Quaternion();
		object.translate(new Vector3(0, 6, 0));
		modelInstance.transform.setTranslation(0, 5, 0);
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W:
			upPressed = true;
			object.applyCentralImpulse(new Vector3(0, 10, 0));
			System.out.println("apply force");
			break;
		case Keys.S:
			downPressed = true;
			break;
		case Keys.A:
			leftPressed = true;
			break;
		case Keys.D:
			rightPressed = true;
			break;
		default:
			break;
		}
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W:
			upPressed = false;
			break;
		case Keys.S:
			downPressed = false;
			break;
		case Keys.A:
			leftPressed = false;
			break;
		case Keys.D:
			rightPressed = false;
			break;
		default:
			break;
		}
		return super.keyDown(keycode);
	}
}
