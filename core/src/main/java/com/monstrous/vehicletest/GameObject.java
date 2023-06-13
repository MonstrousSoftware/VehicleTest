package com.monstrous.vehicletest;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DJoint;

// game object has both a visual aspect (ModelInstance) and a physical aspect (DBody and DGeom)
public class GameObject {
    public ModelInstance instance;
    public DBody body;
    public DJoint joint;
    public int disableSteps;    // counting steps of no movement before disabling this body
    public DGeom geom;
    private static Material restingMaterial = null;   // material to show disabled bodies

    public GameObject(ModelInstance instance, DBody body, DGeom geom) {
        this.instance = instance;
        this.body = body;
        this.geom = geom;
        disableSteps = 0;
        joint = null;

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
