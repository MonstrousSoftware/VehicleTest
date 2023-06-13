package com.monstrous.vehicletest;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.UBJsonReader;


// OBSOLETE


// Use: fbx-conv -f -o g3db wheel.fbx


public class BWorld implements Disposable {


//    final static short GROUND_FLAG = 1<<8;
//    final static short OBJECT_FLAG = 1<<9;
//    final static short ALL_FLAG = -1;
//
//    private Array<ModelInstance> instances;
//    private ModelInstance ground;
//    private ModelInstance ball;
//    public ModelInstance wheels[];
//    private ModelInstance wheel;
//    private ModelInstance chassis;
//    private btCollisionShape groundShape;
//    private btCollisionShape ballShape;
//    private btCollisionShape wheelShape;
//    private btCollisionShape chassisShape;
//    private btRigidBody groundObject;
//    private btRigidBody ballObject;
//    private btRigidBody wheelObject;
//    private btRigidBody looseWheelObject;
//    public btRigidBody chassisObject;
//    private Model model;
//    public btVehicleRaycaster raycaster;
//    public btRaycastVehicle vehicle;
//    public btRaycastVehicle.btVehicleTuning tuning;
//    private Car car;
//    private ModelBatch batch;
//    private BulletWorld bulletWorld;
//    public boolean showDebug = false;
//
//
//    public BWorld(Car car ) {
//        bulletWorld = new BulletWorld();
//
//        this.car = car;
//
//        batch = new ModelBatch();
//        instances = new Array<>();
//
//        // make some models
//        ModelBuilder mb = new ModelBuilder();
//        mb.begin();
//        mb.node().id = "ground";
//        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
//            .box(280f, 1f, 280f);
//        mb.node().id = "chassis";
//        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
//            .box(2.25f, 1.5f, 6f);
//        mb.node().id = "ball";
//        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
//            .sphere(1f, 1f, 1f, 10, 10);
//
//        mb.node().id = "wheel";
//        mb.part("wheel", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
//            .cylinder(0.8f, 0.3f, 0.8f, 8);
//        model = mb.end();
//
//        Model wheelModel = loadModel("models/wheel.g3db");
//
//
//        // create some model instances
//        ground = new ModelInstance(model, "ground");
//        ground.transform.setToTranslation(0, -0.5f, 0);
//        chassis = new ModelInstance(model, "chassis");
//        chassis.transform.setToTranslation(0, 3.2f, 0);         // height to drop from
//        ball = new ModelInstance(model, "ball");
//        ball.transform.setToTranslation(-4, 5f, 0);
//        wheels = new ModelInstance[4];
//        for (int i = 0; i < 4; i++) {
//            wheels[i] = new ModelInstance(wheelModel); // transform will be set by vehicle
//
//        }
//        wheel = new ModelInstance(wheelModel); // transform will be set by vehicle
//        wheel.transform.setToTranslation(3, 2, 0);
//
//        // create collision shapes
//        BoundingBox bounds = new BoundingBox();
//        Vector3 groundHalfExtents = ground.calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
//        Vector3 chassisHalfExtents = chassis.calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
//        Vector3 wheelHalfExtents = wheels[0].calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
//
//        // note: btCylinder has axis in Y direction
//        float w = wheelHalfExtents.x;
//        wheelHalfExtents.x = wheelHalfExtents.y;
//        wheelHalfExtents.y = w;
//
//
//        ballShape = new btSphereShape(0.5f);
//        chassisShape = new btBoxShape(chassisHalfExtents);
//        groundShape = new btBoxShape(groundHalfExtents);
//        wheelShape = new btCylinderShape(wheelHalfExtents);
//
//        createBodies();
//    }
//
//    public void createBodies() {
//
//        instances.clear();
//        bulletWorld.clear();
//
//        float mass = 0;
//        Vector3 localInertia = new Vector3();
//
//        btRigidBody.btRigidBodyConstructionInfo info;
//        localInertia.set(0,0,0);
//        info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, groundShape, localInertia);
//        groundObject = new btRigidBody(info);
//        groundObject.setWorldTransform(ground.transform);
//
//        mass = Settings.carMass;
//        chassisShape.calculateLocalInertia(mass, localInertia);
//        info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, chassisShape, localInertia);
//        chassisObject =  new btRigidBody(info);
//        chassis.transform.setToTranslation(0,3.2f,0);  // height to drop from
//        chassisObject.setWorldTransform(chassis.transform);
//
//        mass = 1f;
//        ballShape.calculateLocalInertia(mass, localInertia);
//        info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, ballShape, localInertia);
//        ballObject =  new btRigidBody(info);
//        ballObject.setWorldTransform(ball.transform);
//
//        mass = 0f;
//        localInertia.set(0,0,0);
//        //wheelShape.calculateLocalInertia(mass, localInertia);
//        info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, null, localInertia);
//        wheelObject =  new btRigidBody(info);
//        wheelObject.setWorldTransform(wheels[0].transform);
//
//        mass = 1f;
//        wheelShape.calculateLocalInertia(mass, localInertia);
//        info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, wheelShape, localInertia);
//        looseWheelObject =  new btRigidBody(info);
//        looseWheelObject.setWorldTransform(wheel.transform);
//
//
//
//      //  instances.add(ground);
//        instances.add(chassis);
//       // instances.add(ball);
//        instances.add(wheels[0]);
//        instances.add(wheels[1]);
//        instances.add(wheels[2]);
//        instances.add(wheels[3]);
//      //  instances.add(wheel);       // loose wheel
//
//        bulletWorld.addRigidBody(groundObject, GROUND_FLAG, ALL_FLAG);
//     //   dynamicsWorld.addRigidBody(ballObject, OBJECT_FLAG, ALL_FLAG);
//        bulletWorld.addRigidBody(chassisObject, OBJECT_FLAG, ALL_FLAG);            // do I add the chassis also as RB? as well as within vehicle?
//      //  dynamicsWorld.addRigidBody(looseWheelObject, OBJECT_FLAG, ALL_FLAG);
//
//        raycaster = bulletWorld.getRaycaster();
//        tuning = new btRaycastVehicle.btVehicleTuning();
//        // tweak as needed
//        tuning.setSuspensionStiffness(Settings.suspensionStiffness);
//        tuning.setSuspensionCompression(Settings.suspensionCompression);
//        tuning.setSuspensionDamping(Settings.suspensionCompression);
//        tuning.setMaxSuspensionForce(10000f);
//        tuning.setMaxSuspensionTravelCm(300f);
////        tuning.setFrictionSlip(5);
//
//        vehicle = new btRaycastVehicle(tuning, chassisObject, raycaster);
//        chassisObject.setActivationState(Collision.DISABLE_DEACTIVATION);
//
//        bulletWorld.addVehicle(vehicle);
//
//        vehicle.setCoordinateSystem(0, 1, 2);
//
//        // create collision shapes
//        BoundingBox bounds = new BoundingBox();
//        Vector3 groundHalfExtents = ground.calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
//        Vector3 chassisHalfExtents = chassis.calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
//        Vector3 wheelHalfExtents = wheels[0].calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
//
//
//        Vector3 point = new Vector3();
//        Vector3 direction = new Vector3(0, -1, 0);      // down vector
//        Vector3 axis = new Vector3(-1, 0, 0);            // rotation axis, pointing to the axle
//        float wheelRadius = wheelHalfExtents.z;
//        float restLength = Settings.suspensionRestLength; //wheelRadius * 0.3f;
//        float wheelOffsetY = -restLength;
//        vehicle.addWheel(point.set(chassisHalfExtents).scl(0.9f, wheelOffsetY, 0.55f), direction, axis,
//            restLength, wheelRadius, tuning, true);
//        vehicle.addWheel(point.set(chassisHalfExtents).scl(-0.9f, wheelOffsetY, 0.55f), direction, axis,
//            restLength, wheelRadius, tuning, true);
//        vehicle.addWheel(point.set(chassisHalfExtents).scl(0.9f, wheelOffsetY, -0.55f), direction, axis,
//            restLength, wheelRadius, tuning, false);
//        vehicle.addWheel(point.set(chassisHalfExtents).scl(-0.9f, wheelOffsetY, -0.55f), direction, axis,
//            restLength, wheelRadius, tuning, false);
//
//
//    }
//
//
//
//    public void update(float deltaTime ){
//
//        if(Gdx.input.isKeyPressed(Input.Keys.R))
//            reset();
//        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
//            createBodies();
//        }
//
//        float delta = MathUtils.clamp(deltaTime, 0, 1f/30f);
//
//        vehicle.setSteeringValue(car.steerAngle * MathUtils.degreesToRadians, 0);
//        vehicle.setSteeringValue(car.steerAngle * MathUtils.degreesToRadians, 1);
//
//        float brakeValue = 0;
//        if(car.braking)
//            brakeValue = 10f;
//
//        vehicle.setBrake(brakeValue, 0);
//        vehicle.setBrake(brakeValue, 1);
//
//        float force = Settings.engineForce *0.01f * car.driveShaftRPM;
//
//        vehicle.applyEngineForce(force, 2);
//        vehicle.applyEngineForce(force, 3);
//
//        bulletWorld.update(delta);
//
//        // update model instance transforms from bullet transforms
//        ballObject.getWorldTransform(ball.transform);
//        looseWheelObject.getWorldTransform(wheel.transform);
//        chassisObject.getWorldTransform(chassis.transform);
//
//        car.transform.set(chassis.transform);
//
//
//        // get wheel transforms from Bullet vehicle
//        for (int i = 0; i < wheels.length; i++) {
//            vehicle.updateWheelTransform(i, true);
//            vehicle.getWheelInfo(i).getWorldTransform().getOpenGLMatrix(wheels[i].transform.val);
////            if(i % 2 == 0)
//            wheels[i].transform.rotate(Vector3.Y, 180); //rotate wheel model to be facing out
//        }
//    }
//
//    private void reset() {
//        chassisObject.setWorldTransform(chassis.transform.setToTranslation(0, 2, 0));
//        chassisObject.setInterpolationWorldTransform(chassis.transform);
//        chassisObject.setLinearVelocity(Vector3.Zero);
//        chassisObject.setAngularVelocity(Vector3.Zero);
//        chassisObject.activate();
//    }
//
//
//
//
//
//    public void render(Camera camera, Environment environment) {
////        batch.begin(camera);
////        batch.render(instances, environment);
////        batch.end();
//        if(showDebug)
//            bulletWorld.render(camera);
//    }
//
//
//
//    public Model loadModel( String fileName ) {
//
//        // Model loader needs a binary json reader to decode
//        UBJsonReader jsonReader = new UBJsonReader();
//        // Create a model loader passing in our json reader
//        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
//        // Now load the model by name
//        Model model = modelLoader.loadModel(Gdx.files.getFileHandle(fileName, Files.FileType.Internal));
//        return model;
//    }
//
//
//
    @Override
    public void dispose() {
//        batch.dispose();
//
//        model.dispose();
//        groundObject.dispose();
//        groundShape.dispose();
//
//        ballObject.dispose();
//        ballShape.dispose();
//
//        bulletWorld.dispose();
    }
}
