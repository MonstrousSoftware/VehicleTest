package com.monstrous.vehicletest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.antzGames.gdx.ode4j.ode.DHinge2Joint;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;


public class World implements Disposable {

    public boolean showDebug;
    public Car theCar;                // car under player control
    private Array<GameObject> gameObjects;
    private Array<Car> cars;
    private ModelBatch modelBatch;
    private GameFactory factory;
    private PhysicsWorld physicsWorld;
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private UserCarController userCarController;
    private Vector3 focusPosition;

    public World(SceneManager sceneManager, SceneAsset sceneAsset, UserCarController userCarController ) {
        this.sceneManager = sceneManager;
        this.sceneAsset = sceneAsset;
        this.userCarController = userCarController;


        showDebug = false;
        gameObjects = new Array<>();
        cars = new Array<>();
        modelBatch = new ModelBatch();
        focusPosition = new Vector3();

        physicsWorld = new PhysicsWorld();

        factory = new GameFactory(physicsWorld, sceneManager, sceneAsset);
        rebuild();

    }

    // can be called from GUI when settings have changed
    public void rebuild() {
        // delete all objects
        for (GameObject go : gameObjects) {
            if (go.body != null)
                physicsWorld.remove(go);
            if (go.scene != null)
                sceneManager.removeScene(go.scene);
        }
        gameObjects.clear();
        cars.clear();


        gameObjects.add( factory.makeGroundPlane() );
        gameObjects.add( factory.makeArrows() );    // XYZ arrows, only in debug view

        // create and position model instances
        for (int i = 0; i < 3; i++) {
            float x = (float) Math.random();
            float z = 30 + (float) Math.random();
            float y = 5 + 6 * i;
            GameObject go = factory.makeCube(50, 5, x, y, z);
            gameObjects.add(go);
        }


        for (int i = 0; i < 10; i++) {
            float x = (float) Math.random();
            float z = 20 + (float) Math.random();
            float y = 5 + 2 * i;

            //GameObject go = factory.makeCylinder(1, 1, 0.3f, x, y, z);
            GameObject go = factory.makeTrafficCone(x, y, z);
            gameObjects.add(go);
        }

        theCar = new Car(userCarController);
        buildCar(theCar, 14,3,0);
        cars.add(theCar);

        Car car = new Car(new CarController());
        buildCar(car, 0,3,5);
        cars.add(car);

        car = new Car(new CarController());
        buildCar(car, 20, 3,5);
        cars.add(car);
    }

    // where to focus camera? At user controlled car position
    public Vector3 getFocusPosition() {
        theCar.chassisObject.instance.transform.getTranslation(focusPosition);
        return focusPosition;
    }

    private GameObject buildCar(Car car, float x, float y, float z) {

        GameObject chassis = factory.makeChassis(x, y, z);
        gameObjects.add( chassis );
        GameObject w0 = factory.makeWheel(chassis, 0);
        GameObject w1 = factory.makeWheel(chassis, 1);
        GameObject w2 = factory.makeWheel(chassis, 2);
        GameObject w3 = factory.makeWheel(chassis, 3);
        gameObjects.add(w0);
        gameObjects.add(w1);
        gameObjects.add(w2);
        gameObjects.add(w3);
        car.joints = new DHinge2Joint[4];
        car.joints[0] = factory.makeWheelJoint(chassis, w0, true);
        car.joints[1] = factory.makeWheelJoint(chassis, w1, true);
        car.joints[2] = factory.makeWheelJoint(chassis, w2, false);
        car.joints[3] = factory.makeWheelJoint(chassis, w3, false);
        car.chassisObject = chassis;
        return chassis;
    }

    public void update( float deltaTime ) {

        // update user controller car and AI controlled cars (e.g. steering and accelerator)
        for(Car c : cars )
            c.update(deltaTime);

        // collision and rigid bodies
        physicsWorld.update(gameObjects);
    }



    // show collision shapes (GameObject.instance)
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
    }
}
