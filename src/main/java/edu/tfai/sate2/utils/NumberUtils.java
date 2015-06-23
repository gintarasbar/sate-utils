package edu.tfai.sate2.utils;

public class NumberUtils {

    public static boolean stringIsNan(String string) {
        if (string.equalsIgnoreCase("nan"))
            return true;
        double wave = Double.parseDouble(string);
        if (Double.isNaN(wave)) {
            return true;
        }
        return false;
    }
}
