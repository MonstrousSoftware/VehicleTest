package com.monstrous.vehicletest;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import org.ode4j.ode.*;

public class World implements Disposable {

    private static float MAX_SPEED = 100f;
    private static float MAX_STEER_ANGLE = (float) (0.2f*Math.PI);


    public boolean showDebug;
    private Car car;
    private Array<GameObject> gameObjects;
    private ModelBatch modelBatch;
    private DWorld world;
    private DSpace space;
    private GameFactory factory;
    private PhysicsWorld physicsWorld;
    private CarView carView;
    public GameObject theCar;               // special game object

    public World(SceneManager sceneManager, Car car, CarView carView ) {
        this.car = car;
        this.carView = carView;
        showDebug = false;
        gameObjects = new Array<>();
        modelBatch = new ModelBatch();

        physicsWorld = new PhysicsWorld();

        factory = new GameFactory(physicsWorld, sceneManager);
        rebuild();
    }

    // can be called from GUI when settings have changed
    public void rebuild() {

        gameObjects.add( factory.makeGroundPlane() );

        // create and position model instances
//        for(int i = 0; i < 10; i++) {
//            float x = (float)Math.random();
//            float z = (float)Math.random();
//            float y = 5 + 2*i;
//            GameObject go = factory.makeCube(1, 1, x, y, z);
//            gameObjects.add(go);
//        }


        for(int i = 0; i < 10; i++) {
            float x = (float)Math.random();
            float z = 20+(float)Math.random();
            float y = 5 + 2*i;

            GameObject go = factory.makeCylinder(1, 1, 0.3f, x, y, z);
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
        carView.setTransforms(theCar.instance.transform, w0.instance.transform, w1.instance.transform,
                                w2.instance.transform, w3.instance.transform);

        gameObjects.add( factory.makeArrows() );    // XYZ arrows

    }


    float speed = 0f;
    float steerAngle = 0.025f;

    public void updateCar(CarController carController, float deltaTime) {

        // Steering
        if (carController.leftPressed && steerAngle < MAX_STEER_ANGLE) {
            steerAngle++;
        }
        if (carController.rightPressed && steerAngle > -MAX_STEER_ANGLE) {
            steerAngle--;
        }
        // Throttle/Brake
        if (carController.forwardPressed && speed < MAX_SPEED) {
            speed += deltaTime;
        } else if (carController.backwardPressed && speed > 0) {   // braking
            speed -= 4 * deltaTime;
        } else if (speed > 0) {  // coasting
            speed -= deltaTime;
        }

        // rather specific to car joints
        for(GameObject go : gameObjects ) {

            DJoint j = go.joint;
            if(j == null)
                continue;

            if (j instanceof DHinge2Joint) {
                DHinge2Joint j2 = (DHinge2Joint) j;
                double curturn = j2.getAngle1();
                j2.setParamVel((steerAngle - curturn) * 1.0f);
                j2.setParamVel2(speed);
            }

            j.getBody(0).enable();
            j.getBody(1).enable();
        }
    }

    public void update( float deltaTime, CarController carController ) {

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
