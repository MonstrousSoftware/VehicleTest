package com.monstrous.vehicletest;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


// captures car state and behaviour apart from the physics simulation

public class Car {

    public static float WHEEL_BASE = Settings.wheelForward + Settings.wheelBack;
    public static float WHEEL_RADIUS = Settings.wheelRadius;

    public static int MAX_GEAR = 5;
    public static float MAX_RPM = 6000;
    public static float RPM_REV = 2000f;     // rpm increase per second
    public static float SHAFT_LATENCY = 10f;
    public static float MAX_STEER_ANGLE =  45;        // degrees
    public static float MAX_TURN_SPEED =  65f;

    public static float[] gearRatios = { -0.2f, 0, 0.2f, 1, 3, 4, 5 };      // for testing

    public float rpm;
    public int gear; // -1, 0, 1, 2, 3, ... MAX_GEAR
    public float gearRatio;
    public float steerAngle;
    public float driveShaftRPM;
    public boolean braking;
    public Matrix4 transform;           // for centre of mass
    public Vector3 position;        // for convenience, (is redundant with transform)
    private Vector3 tmpVec = new Vector3();

    private CarController carController;

    public Car(CarController carController) {
        this.carController = carController;
        rpm = 0;
        gear = 1;
        steerAngle = 0;
        braking = false;
        transform = new Matrix4();
        position = new Vector3();
    }

    public void update(float deltaTime ){

        // perhaps should add automatic gear shifts....

        int gearShift = carController.getGearShift();
        if(gearShift > 0  && gear < MAX_GEAR)
            gear++;
        else if(gearShift < 0 && gear > -1)
            gear--;
        gearRatio = gearRatios[gear+1];     // +1 because of the reverse gear

        // Accelerator
        braking = carController.backwardPressed;
        if(carController.forwardPressed && rpm < MAX_RPM) {
            rpm += RPM_REV * deltaTime;
        }
        else if(carController.backwardPressed && rpm > 0) {   // braking
            rpm-=4*RPM_REV * deltaTime;
        }
        else if(rpm > 0) {  // coasting
            rpm-=RPM_REV * deltaTime;
        }
        rpm = MathUtils.clamp(rpm, 0, MAX_RPM);


        // have drive shaft rotation lag behind gear shifts so that the car doesn't abruptly stop when shifting to neutral
        float targetDriveshaftRPM = rpm * gearRatio;
        if(targetDriveshaftRPM > driveShaftRPM)
            driveShaftRPM += SHAFT_LATENCY;
        else if (targetDriveshaftRPM < driveShaftRPM)
            driveShaftRPM -= SHAFT_LATENCY;



        // Steering
        if(carController.leftPressed && steerAngle < MAX_STEER_ANGLE){
            steerAngle ++;
        }
        if(carController.rightPressed && steerAngle  > -MAX_STEER_ANGLE){
            steerAngle --;
        }

    }

    /// update position and orientation of car
    // based on driveShaftRPM and steerAngle
    //
    private void moveCar(float deltaTime) {
        float wheelDelta = driveShaftRPM * deltaTime;

        // circumference of the wheel is 2 PI R, so if the wheel turns x degrees, it moves x/360*2PI R forward (assuming no slipping)
        float distance = (float) (wheelDelta * 2f * Math.PI * WHEEL_RADIUS / 360f);

        // update car position (BASIC)
        tmpVec.set(0, 0, distance);       // distance to travel
        tmpVec.rot(transform);                // forward orientation of the car (assuming no drifting)
        transform.trn(tmpVec);               // update position

        // angular rotation of car is function of steer angle and velocity
        // valid at low speeds
        float R = (float) (WHEEL_BASE / Math.sin(steerAngle * MathUtils.degreesToRadians));    // radius of turning circle
        float deltaAngle = MathUtils.radiansToDegrees * distance / R;

        // limit the angular rotation to avoid too tight cornering at high speeds
        // this will appear as slipping (under steer)
        deltaAngle = MathUtils.clamp(deltaAngle, -MAX_TURN_SPEED * deltaTime, MAX_TURN_SPEED * deltaTime);


        transform.rotate(Vector3.Y, deltaAngle);        // rotate car in steering direction

        transform.getTranslation(position);
    }

}
