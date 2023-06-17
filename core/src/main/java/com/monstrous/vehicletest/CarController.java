package com.monstrous.vehicletest;

public class CarController {

    public int gear; // -1, 0, 1, 2, 3, ... MAX_GEAR
    public float steerAngle;
    public float rpm;

    public CarController() {
        gear = 1;
        steerAngle = 15f;
        rpm = 1000;
    }

    public void update( float deltaTime ){

    }
}
