package com.monstrous.vehicletest;

public class Settings {

    public static boolean debugView = true;


    public static float chassisMass = 10f;
    public static float wheelMass = 1f;

    // geometry
    public static float chassisWidth = 2.25f;
    public static float chassisHeight = 1.5f;
    public static float chassisLength = 6f;
    // positioning of wheels
    public static float wheelSide = 1.1f;
    public static float wheelForward = 1.6f;    // as measured in Blender
    public static float wheelBack = 1.6f;   // as measured in Blender
    public static float wheelDown = -0.85f;
    // wheel dimensions
    public static float wheelWidth = 0.2f;
    public static float wheelRadius = 0.4f; // as measured in Blender

    public static float suspensionCFM = 0.14f;
    public static float suspensionERP = 0.8f;


//    public static float suspensionRestLength = 1.0f;
//    public static float suspensionStiffness = 400f;
//    public static float suspensionCompression = 6f;
//    public static float suspensionDamping = 7f;
    public static float engineForce = 10f;      // fudge factor
}
