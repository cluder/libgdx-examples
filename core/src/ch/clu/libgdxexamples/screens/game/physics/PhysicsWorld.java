package ch.clu.libgdxexamples.screens.game.physics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;
import com.badlogic.gdx.physics.bullet.collision.ebtDispatcherQueryType;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw.DebugDrawModes;

import ch.clu.libgdxexamples.screens.game.objects.BulletContactListener;
import ch.clu.libgdxexamples.screens.game.objects.GameObject;

public class PhysicsWorld {
	private static String tag = "PhysicsWorld";
	// bullet physics
	private btDefaultCollisionConfiguration collisionConfig;
	private btCollisionDispatcher dispatcher;
	public btBroadphaseInterface broadphase;
	public btConstraintSolver solver;
	public btDynamicsWorld collisionWorld;
	private BulletContactListener contactListener;
	public DebugDrawer debugDrawer = null;
	public int maxSubSteps = 5;
	public float fixedTimeStep = 1f / 60f;

	Map<btCollisionObject, GameObject> collisionObjectMap = new HashMap<>();

	public PhysicsWorld() {
		init();
	}

	private void init() {
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		solver = new btSequentialImpulseConstraintSolver();
		collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		collisionWorld.setGravity(new Vector3(0, -10, 0));
		contactListener = new BulletContactListener(collisionObjectMap);
		debugDrawer = new DebugDrawer();
		debugDrawer.setDebugMode(DebugDrawModes.DBG_DrawWireframe | DebugDrawModes.DBG_DrawFeaturesText
				| DebugDrawModes.DBG_DrawText | DebugDrawModes.DBG_DrawContactPoints);
		collisionWorld.setDebugDrawer(debugDrawer);

	}

	public void add(GameObject obj) {
		collisionObjectMap.put(obj.object, obj);
		collisionWorld.addRigidBody(obj.object);
	}

	public void setDebug(boolean b) {
		if (b) {
			debugDrawer.setDebugMode(DebugDrawModes.DBG_DrawWireframe | DebugDrawModes.DBG_DrawFeaturesText
					| DebugDrawModes.DBG_DrawText | DebugDrawModes.DBG_DrawContactPoints);
		} else {
			debugDrawer.setDebugMode(0);
		}
	}

	public void render(ModelBatch modelBatch) {
		if (debugDrawer.getDebugMode() <= 0) {
			return;
		}
		modelBatch.flush();
		debugDrawer.begin(modelBatch.getCamera());
		collisionWorld.debugDrawWorld();
		debugDrawer.end();
	}

	boolean tmp = false;

	public void update(float deltaTime) {

//		for (Entry<btCollisionObject, GameObject> entry : collisionObjectMap.entrySet()) {
//			btCollisionObject obj = entry.getKey();
//			int activationState = obj.getActivationState();
//			int islandTag = obj.getIslandTag();
//			float friction = obj.getFriction();
//			Vector3 interpolationLinearVelocity = obj.getInterpolationLinearVelocity();
//
//			Gdx.app.log(tag, String.format("act state:%s tag:%s friction:%s linV:%s", activationState, islandTag,
//					friction, interpolationLinearVelocity));
//			tmp = true;
//			obj.setFriction(0);
//		}

		collisionWorld.stepSimulation(deltaTime, maxSubSteps, fixedTimeStep);

	}

	private boolean checkCollision(btCollisionObject o1, btCollisionObject o2) {
		boolean hit = false;
		CollisionObjectWrapper co1 = new CollisionObjectWrapper(o1);
		CollisionObjectWrapper co2 = new CollisionObjectWrapper(o2);

		btCollisionAlgorithm algo = dispatcher.findAlgorithm(co1.wrapper, co2.wrapper, null,
				ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS);
		if (algo != null) {
			btDispatcherInfo di = new btDispatcherInfo();
			btManifoldResult result = new btManifoldResult(co1.wrapper, co2.wrapper);

			algo.processCollision(co1.wrapper, co2.wrapper, di, result);

			btPersistentManifold persistentManifold = result.getPersistentManifold();

			int numContacts = persistentManifold.getNumContacts();
			hit = numContacts > 0;
			if (hit) {
				Gdx.app.log(tag, String.format("hit:%s (%s)", hit, numContacts));
			}

			dispatcher.freeCollisionAlgorithm(algo.getCPointer());
			result.dispose();
			di.dispose();
		}

		co1.dispose();
		co2.dispose();

		return hit;
	}

	public void dispose() {
		contactListener.dispose();
		collisionWorld.dispose();

		if (solver != null)
			solver.dispose();
		if (broadphase != null)
			broadphase.dispose();
		if (dispatcher != null)
			dispatcher.dispose();
		if (collisionConfig != null)
			collisionConfig.dispose();
	}
}
