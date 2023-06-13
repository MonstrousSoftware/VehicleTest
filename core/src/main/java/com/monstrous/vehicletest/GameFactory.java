package com.monstrous.vehicletest;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.*;

// creates GameObjects

// instances are added to SceneManager, useful? It ensures we have shadows.

public class GameFactory implements Disposable {

    private ModelBuilder modelBuilder;
    private DWorld world;                   // rigid body world
    private DSpace space;                   // collision space
    private DMass massInfo;
    private Array<Disposable> disposables;
    private SceneManager sceneManager;

    public GameFactory(PhysicsWorld physicsWorld, SceneManager sceneManager) {
        this.world = physicsWorld.world;
        this.space = physicsWorld.space;
        this.sceneManager = sceneManager;

        massInfo = OdeHelper.createMass();

        modelBuilder = new ModelBuilder();
        disposables = new Array<>();

    }

    public GameObject makeSphere(float mass, float size, float x, float y, float z) {
        float radius = size / 2f;
        DBody body = OdeHelper.createBody(world);
        body.setPosition(x, y, z);
        massInfo.setSphere(1, radius);
        massInfo.adjust(mass);    // mass
        body.setMass(massInfo);

        DSphere sphere = OdeHelper.createSphere(space, radius);
        sphere.setBody(body);

        Model modelBall = modelBuilder.createSphere(size, size, size, 12, 12,
            new Material(ColorAttribute.createDiffuse(Color.BLUE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        disposables.add(modelBall);

        ModelInstance instance = new ModelInstance(modelBall, x, y, z);

        sceneManager.addScene(new Scene(instance));
        GameObject go = new GameObject(instance, body, sphere);
        return go;
    }


    public GameObject makeCube(float mass, float size, float x, float y, float z) {
        return makeBox(mass, size, size, size, x, y, z);
    }


    public GameObject makeBox(float mass, float w, float h, float d, float x, float y, float z) {
        GameObject go = buildBox(mass, w, h, d, x, y, z);
        sceneManager.addScene(new Scene(go.instance));
        return go;
    }

    private GameObject buildBox(float mass, float w, float h, float d, float x, float y, float z) {
        DBody body = OdeHelper.createBody(world);
        body.setPosition(x, y, z);
        massInfo.setBox(1, w, h, d);
        massInfo.adjust(mass);    // mass
        body.setMass(massInfo);
        body.setAutoDisableDefaults();

        DBox box = OdeHelper.createBox(space, w, h, d);
        box.setBody(body);


        Model modelBox = modelBuilder.createBox(w, h, d,
            new Material(ColorAttribute.createDiffuse(Color.BLUE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        disposables.add(modelBox);

        ModelInstance instance = new ModelInstance(modelBox, x, y, z);
        GameObject go = new GameObject(instance, body, box);
        return go;
    }


    public GameObject makeCylinder(float mass, float radius, float height, float x, float y, float z) {
        GameObject go = buildCylinder(mass, radius, height, x, y, z);
        sceneManager.addScene(new Scene(go.instance));
        return go;
    }

    private GameObject buildCylinder(float mass, float radius, float height, float x, float y, float z) {

        DBody body = OdeHelper.createBody(world);
        body.setPosition(x, y, z);
        massInfo.setCylinder(1, 1, radius, height); // dir 1 == x
        massInfo.adjust(mass);    // mass
        body.setMass(massInfo);

        DCylinder cylinder = OdeHelper.createCylinder(space, radius, height);
        cylinder.setBody(body);

        Model model = modelBuilder.createCylinder(radius * 2, height, radius * 2, 8,
            new Material(ColorAttribute.createDiffuse(Color.BLUE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        cylinderTurn(model);

        disposables.add(model);

        ModelInstance instance = new ModelInstance(model, x, y, z);
        GameObject go = new GameObject(instance, body, cylinder);
        return go;
    }

    // LibGDX cylinder is oriented along Y axis,  ODE4j on Z axis
    // rotate mesh to match ODE definition of a cylinder with the main axis on Z instead of Y
    // this hard-codes the rotation into the mesh so that we can later use transforms as normal.
    private void cylinderTurn(Model model){
        Vector3 v = new Vector3();
        Mesh mesh = model.meshes.first();
        int n = mesh.getNumVertices();
        int stride = mesh.getVertexSize() / 4;  // size of vertex in number of floats
        float [] vertices = new float[stride*n];
        mesh.getVertices(vertices);
        for(int i = 0 ; i < n; i++) {
            v.set(vertices[i*stride], vertices[i*stride+1], vertices[i*stride+2]);
            v.rotate(Vector3.X, 90);
            vertices[i*stride] = v.x;
            vertices[i*stride+1] = v.y;
            vertices[i*stride+2] = v.z;
        }
        mesh.setVertices(vertices);
    }

    public GameObject makeArrows() {
        Model model = modelBuilder.createXYZCoordinates(10f, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked);
        disposables.add(model);
        ModelInstance instance = new ModelInstance(model, new Vector3(0, 0, 0));
        //sceneManager.addScene(new Scene(instance));
        GameObject go = new GameObject(instance, null, null);
        return go;
    }

    public GameObject makeGroundPlane() {

        OdeHelper.createPlane(space, 0, 1, 0, 0);

        Model modelFlat = modelBuilder.createBox(40f, 1, 40f,
            new Material(ColorAttribute.createDiffuse(Color.OLIVE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        disposables.add(modelFlat);
        ModelInstance instance = new ModelInstance(modelFlat, 0, -1, 0);    // 'table top' surface
        sceneManager.addScene(new Scene(instance));
        GameObject go = new GameObject(instance, null, null);
        return go;
    }




    private static float MAX_STEER_ANGLE = (float) (0.2f*Math.PI);

    public  GameObject makeChassis(float x, float y, float z) {
        // car pointing into z direction
        return buildBox(50, 2.25f, 1.5f, 6, x, y, z);
    }


    // index: 0=front left, 1=front right, 2 =rear left, 3 = rear right
    public  GameObject makeWheel(GameObject chassis, int index) {
        Vector3 chassisPos = new Vector3();
        chassis.instance.transform.getTranslation(chassisPos);
        float dx = 1.3f;    // side
        float dz = 1.7f;    // forward/back
        float dy = -0.75f;   // down

        if(index == 1 ||index == 3) // right
            dx = -dx;
        if(index == 2 ||index == 3) // rear
            dz = -dz;
        GameObject wheel = buildCylinder(1, .5f, .2f, chassisPos.x+dx, chassisPos.y+dy, chassisPos.z+dz);

        // turn cylinder axis from Z to X axis, as the car is oriented towards Z, and cylinder by default points to Z
        DQuaternion dq = new DQuaternion();
        dq.set( DQuaternion.fromEulerDegrees(0, 90, 0));
        wheel.body.setQuaternion(dq);

        // hinge2joints for front wheels
        DHinge2Joint joint = OdeHelper.createHinge2Joint(world);    // add joint to the world
        DVector3C anchor = wheel.body.getPosition();
        joint.attach(chassis.body, wheel.body);

        joint.setAnchor(anchor);
        joint.setAxis1(0,1,0);      // up axis for steering
        joint.setAxis2(-1, 0, 0);    // roll axis for rolling
        joint.setParamVel2(0);
        joint.setParamFMax2(25f);
        joint.setParamFMax(25f);
        joint.setParamSuspensionERP(0.99f);
        joint.setParamSuspensionCFM(1e-7f);
        float maxSteer = MAX_STEER_ANGLE;
        if(index >= 2)  // rear wheel?
            maxSteer = 0f;
        joint.setParam(DJoint.PARAM_N.dParamLoStop1, -maxSteer);            // put a stop at max steering angle
        joint.setParam(DJoint.PARAM_N.dParamHiStop1, maxSteer);             // idem

        wheel.joint = joint;        // bit hacky to store this in game object

        return wheel;
    }


    @Override
    public void dispose() {
        for(Disposable d : disposables )
            d.dispose();
    }
}