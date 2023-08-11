package com.monstrous.vehicletest;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.monstrous.vehicletest.gui.GUI;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;


public class GameScreen extends ScreenAdapter {

    private static final int SHADOW_MAP_SIZE = 2048;

    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private PerspectiveCamera camera;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;
    private CameraInputController camController;
    private UserCarController carController;
    private GUI gui;
    private World world;
    private Vector3 tmpV = new Vector3();

    @Override
    public void show() {
        // load scene asset
        sceneManager = new SceneManager();
        //sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/muscle2.gltf"));
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/ford-mustang-fastback-1968.gltf"));

        // extract some scenery items and add to scene manager
        scene = new Scene(sceneAsset.scene, "road");
        sceneManager.addScene(scene);
        scene = new Scene(sceneAsset.scene, "grassplane");
        sceneManager.addScene(scene);


        // setup camera
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 1f;
        camera.far = 1000f;
        camera.position.set(5, 4, -10);
        //camera.lookAt(0,0,0);
        sceneManager.setCamera(camera);


        carController = new UserCarController();
        world = new World(sceneManager, sceneAsset, carController);
        gui = new GUI(carController, world);


        camController = new CameraInputController(camera);
        // free up the WASD keys
        camController.forwardKey = Input.Keys.F3;
        camController.backwardKey = Input.Keys.F4;
        camController.rotateRightKey = Input.Keys.F5;
        camController.rotateLeftKey = Input.Keys.F6;

        // input multiplexer to input to GUI and to cam controller
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        im.addProcessor(gui.stage);
        im.addProcessor(camController);
        im.addProcessor(carController);



        sceneManager.environment.set(new PBRFloatAttribute(PBRFloatAttribute.ShadowBias, 0.001f));

        // setup light
        light = new DirectionalShadowLight(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE).setViewport(100,100,5,400);

        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(0.1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);


    }

    @Override
    public void render(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.R))
            world.rebuild();

        // animate camera
        camController.update();
        camera.direction.set(world.getFocusPosition()).sub(camera.position).nor();      // aim camera at car
        camera.up.set(Vector3.Y);
        camera.update();



        world.update( deltaTime );

        // render
        Gdx.gl.glClearColor(.2f,.6f,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        sceneManager.update(deltaTime);

        if(!world.showDebug)
            sceneManager.render();

        world.renderDebug(camera, sceneManager.environment);

        gui.render(deltaTime);
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        sceneManager.updateViewport(width, height);
        gui.resize(width, height);
    }


    @Override
    public void dispose() {
        // Destroy screen's assets here.
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
        gui.dispose();
        world.dispose();
    }
}
