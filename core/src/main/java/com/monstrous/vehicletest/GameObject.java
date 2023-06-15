package com.monstrous.vehicletest;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DJoint;

// game object has both a visual aspect (Scene and ModelInstance) and a physical aspect (DBody and DGeom)
public class GameObject {
    public ModelInstance instance;      // model instance for debug view of geom
    public Scene scene;                 // = GLTF 'model instance'
    public DBody body;                  // rigid body
    public DGeom geom;                  // collision geometry
    public int disableSteps;            // number of steps of no movement, for disabling this body

    private static Material restingMaterial = null;   // material to show disabled bodies (debug)

    public GameObject(ModelInstance instance, DBody body, DGeom geom) {
        this.instance = instance;
        this.body = body;
        this.geom = geom;
        disableSteps = 0;
    }

    // debug option: change colour to show object is asleep
    public void showSleeping() {
        if(instance == null)
            return;
        if(restingMaterial == null) // lazy init
            restingMaterial = new Material(ColorAttribute.createDiffuse(Color.PURPLE));

        instance.materials.first().set(restingMaterial);
    }
}
