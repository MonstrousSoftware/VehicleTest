package com.monstrous.vehicletest.utils;

import com.badlogic.gdx.math.Quaternion;
import com.github.antzGames.gdx.ode4j.math.DMatrix3C;
import com.github.antzGames.gdx.ode4j.math.DQuaternionC;


public class Ode2GdxMathUtils {

    //          0, 1, 2, 3
    // ODE      w, x, y ,z
    // libGDX   x, y, z, w
    public static Quaternion getGdxQuaternion(DQuaternionC odeQ){
        Quaternion dgxQ = new Quaternion();
        return dgxQ.set((float)odeQ.get1(), (float)odeQ.get2(), (float)odeQ.get3(), (float) odeQ.get0());
    }

    // From https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
    public static Quaternion getGdxQuaternion(DMatrix3C odeMat3) {

        float m00 = (float) odeMat3.get00();
        float m01 = (float) odeMat3.get01();
        float m02 = (float) odeMat3.get02();
        float m10 = (float) odeMat3.get10();
        float m11 = (float) odeMat3.get11();
        float m12 = (float) odeMat3.get12();
        float m20 = (float) odeMat3.get20();
        float m21 = (float) odeMat3.get21();
        float m22 = (float) odeMat3.get22();

        float tr = m00 + m11 + m22;
        float qw, qx, qy, qz;

        if (tr > 0) {
            float S = (float)Math.sqrt(tr + 1.0) * 2; // S=4*qw
            qw = 0.25f * S;
            qx = (m21 - m12) / S;
            qy = (m02 - m20) / S;
            qz = (m10 - m01) / S;
        } else if ((m00 > m11) & (m00 > m22)) {
            float S = (float)Math.sqrt(1.0 + m00 - m11 - m22) * 2; // S=4*qx
            qw = (m21 - m12) / S;
            qx = 0.25f * S;
            qy = (m01 + m10) / S;
            qz = (m02 + m20) / S;
        } else if (m11 > m22) {
            float S = (float)Math.sqrt(1.0 + m11 - m00 - m22) * 2; // S=4*qy
            qw = (m02 - m20) / S;
            qx = (m01 + m10) / S;
            qy = 0.25f * S;
            qz = (m12 + m21) / S;
        } else {
            float S = (float)Math.sqrt(1.0 + m22 - m00 - m11) * 2; // S=4*qz
            qw = (m10 - m01) / S;
            qx = (m02 + m20) / S;
            qy = (m12 + m21) / S;
            qz = 0.25f * S;
        }
        return new Quaternion(qx, qy, qz, qw);
    }
}
