package ch.clu.libgdxexamples.screens.game.objects;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class BulletContactListener extends ContactListener {
	String tag = BulletContactListener.class.getSimpleName();

	Map<btCollisionObject, GameObject> objMap;

	public BulletContactListener(Map<btCollisionObject, GameObject> objMap) {
		this.objMap = objMap;
	}

	@Override
	public boolean onContactAdded(btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1,
			int partId1, int index1) {
		Gdx.app.log(tag, String.format("onContactAdded: "));

		GameObject gameObject = objMap.get(colObj0);
		if (gameObject == null) {
			return true;
		}
		gameObject.moving = false;

		return true;
	}

}
