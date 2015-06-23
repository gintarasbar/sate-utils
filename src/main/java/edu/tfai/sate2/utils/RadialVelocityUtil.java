package edu.tfai.sate2.utils;


public class RadialVelocityUtil {

    // speed of light in vacuum in km/s
    public final static double SPEED_OF_LIGHT = 299792.458d;

    public static double radialVelocityToShift(double wave, double radialVelocity) {
        double mult = radialVelocity / SPEED_OF_LIGHT;
        double delta = wave * mult;
        return -delta;
    }

    public static double shiftToRadialVelocity(double wave, double shift) {
        double mult = -shift / wave;
        double radV = SPEED_OF_LIGHT * mult;
        return radV;
    }

}
