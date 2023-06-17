package com.monstrous.vehicletest;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import org.ode4j.ode.DHinge2Joint;


// captures car state and behaviour
// assumes there is only one car type

public class Car {

    public static int MAX_GEAR = 5;

    public static float MAX_RPM = 6000;
    public static float RPM_REV = 2000f;     // rpm increase per second
    public static float SHAFT_LATENCY = 10f;


    public static float[] gearRatios = { -0.2f, 0, 0.2f, 1, 3, 4, 5 };      // for testing, to tune

    public float gearRatio;
    public float driveShaftRPM;
    private CarController carController;

    public DHinge2Joint[] joints;      // 4 for 4 wheels
    public GameObject chassisObject;

    public Car(CarController carController) {
        this.carController = carController;
    }

    public void update(float deltaTime ){

        // perhaps should add automatic gear shifts....
        carController.update(deltaTime);

        gearRatio = gearRatios[carController.gear+1];     // +1 because of the reverse gear

        // have drive shaft rotation lag behind gear shifts so that the car doesn't abruptly stop when shifting to neutral
        float targetDriveshaftRPM = carController.rpm * gearRatio;
        if(targetDriveshaftRPM > driveShaftRPM)
            driveShaftRPM += SHAFT_LATENCY;
        else if (targetDriveshaftRPM < driveShaftRPM)
            driveShaftRPM -= SHAFT_LATENCY;

        updateJoints(-carController.steerAngle, 0.01f* driveShaftRPM);
    }

    private void updateJoints(float steerAngle, float wheelAngularVelocity) {
        // joints chassis-wheel
        for(int i = 0; i < 4; i ++ ) {
            DHinge2Joint j2 = joints[i];

            if( i < 2) {
                double curturn = j2.getAngle1();
                j2.setParamVel((Math.toRadians(steerAngle) - curturn) * 1.0f);      // ignored for non-steering wheels which are locked
            }
            j2.setParamVel2(wheelAngularVelocity);

            j2.getBody(0).enable();
            j2.getBody(1).enable();
        }
    }




}
