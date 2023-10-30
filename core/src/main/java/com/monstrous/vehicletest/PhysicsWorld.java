package com.monstrous.vehicletest;


import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.antzGames.gdx.ode4j.math.DVector3C;
import com.github.antzGames.gdx.ode4j.ode.*;
import com.monstrous.vehicletest.utils.Ode2GdxMathUtils;

import static com.github.antzGames.gdx.ode4j.ode.OdeConstants.*;


// World of rigid body dynamics and collisions
// doesn't know about visuals
// ODE specifics are mostly kept in this class and in GameFactory


public class PhysicsWorld implements Disposable {
    private static final float DISABLE_THRESHOLD = 0.008f;
    private static final float DISABLE_STEPS = 10;

    public DWorld world;
    public DSpace space;
    private DMass massInfo;
    private DJointGroup contactgroup;


    public PhysicsWorld() {

        OdeHelper.initODE2(0);
        world = OdeHelper.createWorld();
        space = OdeHelper.createSapSpace( null, DSapSpace.AXES.XZY );
        massInfo = OdeHelper.createMass();
        contactgroup = OdeHelper.createJointGroup();

        world.setGravity (0,-1.5,0);
        world.setCFM (1e-5);
        world.setERP (0.8);
        world.setQuickStepNumIterations (20);
    }


    public void remove( GameObject gameObject ) {
        gameObject.body.destroy();
        gameObject.geom.destroy();
    }


    public void reset() {

        world.destroy();
        space.destroy();
        world = OdeHelper.createWorld();
        space = OdeHelper.createSapSpace( null, DSapSpace.AXES.XZY );
    }




    public void update(Array<GameObject> gameObjects) {


        space.collide (null,nearCallback);
        world.quickStep (0.05);
        contactgroup.empty ();

        for(int i = 0; i < gameObjects.size; i++){
            GameObject go = gameObjects.get(i);
            DGeom geom = go.geom;
            if(geom == null)        // not a physics object
                continue;
            DBody body = geom.getBody();
            ModelInstance instance = go.instance;

            DVector3C pos  = geom.getPosition();
            float x = (float)pos.get0();
            float y = (float)pos.get1();
            float z = (float)pos.get2();
            Quaternion q = Ode2GdxMathUtils.getGdxQuaternion(geom.getQuaternion());

            if(instance != null ) {
                instance.transform.set(q);
                instance.transform.setTranslation(x, y, z);
            }
            if(go.scene != null && instance != go.scene.modelInstance) {
                go.scene.modelInstance.transform.set(q);
                go.scene.modelInstance.transform.setTranslation(x, y, z);
            }

            // disable bodies that have settled
            if(shouldDisable(go, body, i)) {
                body.disable();
                go.showSleeping();
            }
        }

    }


    // why not use autoDisable?
    // flag body to be disabled after N steps of very small linear or angular velocity
    private boolean shouldDisable( GameObject go, DBody b, int index ){
        boolean disable = true;
        DVector3C lvel = b.getLinearVel();
        double lspeed = lvel.lengthSquared();
        if (lspeed > DISABLE_THRESHOLD)
            disable = false;
        DVector3C avel = b.getAngularVel();
        double aspeed = avel.lengthSquared();
        if (aspeed > DISABLE_THRESHOLD)
            disable = false;
        if(disable)
            go.disableSteps++;
        else
            go.disableSteps = 0;
        return go.disableSteps > DISABLE_STEPS;
    }







    private DGeom.DNearCallback nearCallback = new DGeom.DNearCallback() {
        @Override
        public void call(Object data, DGeom o1, DGeom o2) {
            nearCallback(data, o1, o2);
        }
    };

    private void nearCallback (Object data, DGeom o1, DGeom o2) {
        int i,n;

        DBody b1 = o1.getBody();
        DBody b2 = o2.getBody();
        if (b1!=null && b2!=null && OdeHelper.areConnected(b1, b2))
            return;

        final int N = 4;
        DContactBuffer contacts = new DContactBuffer(N);
        n = OdeHelper.collide (o1,o2,N,contacts.getGeomBuffer());//[0].geom,sizeof(dContact));
        if (n > 0) {
            for (i=0; i<n; i++) {
                DContact contact = contacts.get(i);
                contact.surface.mode = dContactSlip1 | dContactSlip2 | dContactSoftERP | dContactSoftCFM | dContactApprox1;
                if ( o1 instanceof DSphere || o2 instanceof DSphere )
                    contact.surface.mu = 20;
                else
                    contact.surface.mu = 0.5;

                contact.surface.slip1 = 0.0;
                contact.surface.slip2 = 0.0;
                contact.surface.soft_erp = 0.8;
                contact.surface.soft_cfm = 0.01;
                DJoint c = OdeHelper.createContactJoint(world,contactgroup,contact);
                c.attach (o1.getBody(), o2.getBody());
            }
        }
    }




    @Override
    public void dispose() {
        contactgroup.destroy();
        space.destroy();
        world.destroy();
        OdeHelper.closeODE();
    }

}
