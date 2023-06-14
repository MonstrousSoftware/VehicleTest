package com.monstrous.vehicletest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import org.ode4j.ode.*;

public class World implements Disposable {

    private static float MAX_SPEED = 100f;
    private static float MAX_STEER_ANGLE = (float) (0.2f*Math.PI);


    public boolean showDebug;
    private Car car;
    private Array<GameObject> gameObjects;
    private ModelBatch modelBatch;
    private GameFactory factory;
    private PhysicsWorld physicsWorld;
    public GameObject theCar;               // special game object
    private Vector3 focusPosition;
    private DHinge2Joint[] joints;
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;

    public World(SceneManager sceneManager, SceneAsset sceneAsset, Car car ) {
        this.sceneManager = sceneManager;
        this.sceneAsset = sceneAsset;
        this.car = car;
        focusPosition = new Vector3();
        showDebug = false;
        gameObjects = new Array<>();
        modelBatch = new ModelBatch();

        physicsWorld = new PhysicsWorld();

        factory = new GameFactory(physicsWorld, sceneManager, sceneAsset);
        rebuild();

    }

    // can be called from GUI when settings have changed
    public void rebuild() {
        // delete all objects
        for(GameObject go : gameObjects ) {
            if (go.body != null)
                physicsWorld.remove(go);
            if(go.scene != null)
                sceneManager.removeScene(go.scene);
        }
        gameObjects.clear();


        gameObjects.add( factory.makeGroundPlane() );

        // create and position model instances
        for(int i = 0; i < 3; i++) {
            float x = (float)Math.random();
            float z = 30+(float)Math.random();
            float y = 5 + 6*i;
            GameObject go = factory.makeCube(50, 5, x, y, z);
            gameObjects.add(go);
        }


        for(int i = 0; i < 10; i++) {
            float x = (float)Math.random();
            float z = 20+(float)Math.random();
            float y = 5 + 2*i;

            //GameObject go = factory.makeCylinder(1, 1, 0.3f, x, y, z);
            GameObject go = factory.makeTrafficCone(x, y, z);
            gameObjects.add(go);
        }


        theCar = factory.makeChassis(0,3,0);
        gameObjects.add( theCar );
        GameObject w0 = factory.makeWheel(theCar, 0);
        GameObject w1 = factory.makeWheel(theCar, 1);
        GameObject w2 = factory.makeWheel(theCar, 2);
        GameObject w3 = factory.makeWheel(theCar, 3);
        gameObjects.add(w0);
        gameObjects.add(w1);
        gameObjects.add(w2);
        gameObjects.add(w3);
        joints = new DHinge2Joint[4];
        joints[0] = factory.makeWheelJoint(theCar, w0, true);
        joints[1] = factory.makeWheelJoint(theCar, w1, true);
        joints[2] = factory.makeWheelJoint(theCar, w2, false);
        joints[3] = factory.makeWheelJoint(theCar, w3, false);

        gameObjects.add( factory.makeArrows() );    // XYZ arrows, only in debug view

    }

    public Vector3 getFocusPosition() {
        return focusPosition;
    }

    float speed = 0f;
    float steerAngle = 0.025f;
    public static float STEER_SPEED = 10f;

    public void updateCar(CarController carController, float deltaTime) {


        // joints chassis-wheel
        for(int i = 0; i < 4; i ++ ) {
            DHinge2Joint j2 = joints[i];

            if( i < 2) {
                double curturn = j2.getAngle1();
                j2.setParamVel((Math.toRadians(-car.steerAngle) - curturn) * 1.0f);      // ignored for non-steering wheels which are locked
            }
            j2.setParamVel2(0.01f* car.driveShaftRPM);

            j2.getBody(0).enable();
            j2.getBody(1).enable();
        }

        theCar.instance.transform.getTranslation(focusPosition); // for camera pointing

    }

    public void update( float deltaTime, CarController carController ) {
        if(Gdx.input.isKeyPressed(Input.Keys.R))
            rebuild();

        updateCar(carController, deltaTime);
        physicsWorld.update(gameObjects);

        //car.transform.set(theCar.instance.transform);       // copy game object transform to Car.transform
    }




    public void renderDebug(Camera cam, Environment environment ) {
        if(!showDebug)
            return;

        modelBatch.begin(cam);
        for(GameObject go : gameObjects )
            if(go.instance != null)
                modelBatch.render(go.instance, environment);
        modelBatch.end();

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        physicsWorld.dispose();
        gameObjects.clear();
    }
}
