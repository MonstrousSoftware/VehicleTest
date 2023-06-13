package com.monstrous.vehicletest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;


// used to update the car visual representation to the car data

public class CarView {

    private Scene scene;
    private int body;
    private int lfWheel, rfWheel;
    private int lrWheel, rrWheel;
    private Vector3 tmpVec;
    private Matrix4 [] originalTransforms;
    private Matrix4 tmpMat = new Matrix4();
    private Matrix4 chassisTransform = new Matrix4();
    private Matrix4 transforms[];   // 5: chassis + 4 wheels

    // pass a scene corresponding the car. This should be added to the SceneManager.
    // CarView will modify the transforms of the car scene nodes (assuming specific names) based on Car.transform
    public CarView(Scene scene) {
        this.scene = scene;
        tmpVec = new Vector3();

        // lookup nodes for front wheels
        lfWheel = -1;
        rfWheel = -1;
        for(int i = 0; i < scene.modelInstance.nodes.size; i++){
            Node node = scene.modelInstance.nodes.get(i);
            if(node.id.contentEquals("Muscle 2"))
                body = i;
            else if(node.id.contentEquals("Muscle 2 wheel front left"))
                lfWheel = i;
            else if(node.id.contentEquals("Muscle 2 wheel front right"))
                rfWheel = i;
            else if(node.id.contentEquals("Muscle 2 wheel rear left"))
                lrWheel = i;
            else if(node.id.contentEquals("Muscle 2 wheel rear right"))
                rrWheel = i;
        }
        if(lfWheel == -1 || rfWheel == -1 || lrWheel == -1 || rrWheel == -1 || body == -1)
            Gdx.app.error("Car View", "expected nodes not found in gltf file");

        // store original transforms for all the component parts
        originalTransforms = new Matrix4[5];
        transforms = new Matrix4[5];

        originalTransforms[0] = new Matrix4( scene.modelInstance.nodes.get(body).localTransform);
        originalTransforms[1] = new Matrix4( scene.modelInstance.nodes.get(lfWheel).localTransform);
        originalTransforms[2] = new Matrix4( scene.modelInstance.nodes.get(rfWheel).localTransform);
        originalTransforms[3] = new Matrix4( scene.modelInstance.nodes.get(lrWheel).localTransform);
        originalTransforms[4] = new Matrix4( scene.modelInstance.nodes.get(rrWheel).localTransform);

    }

    public Vector3 getPosition() {
        transforms[0].getTranslation(tmpVec);
        return tmpVec;
    }

    public void setTransforms( Matrix4 chassis, Matrix4 w0,Matrix4 w1,Matrix4 w2,Matrix4 w3 ){
        transforms[0] = chassis;
        transforms[1] = w0;
        transforms[2] = w1;
        transforms[3] = w2;
        transforms[4] = w3;

    }

    public void update( Car car ) {
        chassisTransform.set(originalTransforms[0]).inv();     // compensate for blender model not being centred on origin
        chassisTransform.mulLeft(transforms[0]);
        moveNode(body, chassisTransform);


        moveWheel(lfWheel, transforms[1]);
        moveWheel(rfWheel, transforms[2]);
        moveWheel(lrWheel, transforms[3]);
        moveWheel(rrWheel, transforms[4]);
    }

    private Matrix4  makeTransform( int index, Matrix4 transform ){
        //transform.set(originalTransforms[index]).inv();     // compensate for blender model not being centred on origin
        transform.set(transforms[index]);
        return transform;
    }

    private void moveWheel(int nodeID, Matrix4 transform){
        Node node = scene.modelInstance.nodes.get(nodeID);
        tmpMat.set(transform);
        tmpMat.rotate(Vector3.Y, -90);
        node.globalTransform.set(tmpMat);
    }

    // use the following to copy transforms from Bullet world
//    public void fromWorld(BWorld BWorld){
//        car.transform.set(originalTransforms[0]).inv();     // compensate for blender model not being centred on origin
//        BWorld.chassisObject.getWorldTransform(tmpMat);
//        car.transform.mulLeft(tmpMat);
//
//        moveNode(body, car.transform);
//        moveNode(lfWheel, BWorld.wheels[0].transform);
//        moveNode(rfWheel, BWorld.wheels[1].transform);
//        moveNode(lrWheel, BWorld.wheels[2].transform);
//        moveNode(rrWheel, BWorld.wheels[3].transform);
//    }

    private void moveNode(int nodeID, Matrix4 transform){
        Node node = scene.modelInstance.nodes.get(nodeID);
        node.globalTransform.set(transform);
    }

    // Below: use steerAngle and wheelTurnAngle to transform wheels


    // use the following to calculate transforms from Car class
    public void updateOld(Car car) {
        moveBody(car, body, originalTransforms[0]);
        steerWheel(car, lfWheel, originalTransforms[1]);
        steerWheel(car, rfWheel, originalTransforms[2]);
        turnWheel(car, lrWheel, originalTransforms[3]);
        turnWheel(car, rrWheel, originalTransforms[4]);
    }

    private void moveBody(Car car, int nodeID, Matrix4 orig) {
        Node node = scene.modelInstance.nodes.get(nodeID);
        node.localTransform.set(orig);
        node.localTransform.mulLeft(car.transform);
        node.calculateWorldTransform();
    }

    private void steerWheel(Car car, int nodeID, Matrix4 orig) {
        // rotate wheel node for steering
        Node node = scene.modelInstance.nodes.get(nodeID);
        node.localTransform.set(orig);
        node.localTransform.trn(0,-1.0f,0);
        //node.localTransform.setToTranslation(0,-0.5f,0);
        node.localTransform.rotate(Vector3.Y, car.steerAngle);
        node.localTransform.rotate(Vector3.X, car.wheelTurnAngle);              // also roll wheel around axle
        node.localTransform.mulLeft(car.transform);
        node.calculateWorldTransform();
    }

    private void turnWheel(Car car, int nodeID, Matrix4 orig) {
        // rotate wheel around axle
        Node node = scene.modelInstance.nodes.get(nodeID);
        node.localTransform.set(orig);
        //node.localTransform.setToTranslation(0,-0.5f,0);
        node.localTransform.rotate(Vector3.X, car.wheelTurnAngle);  // degrees
        node.localTransform.mulLeft(car.transform);
        node.calculateWorldTransform();
    }
}
