package edu.tfai.sate2.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    public static String string(int length) {
        return new BigInteger(5 * length, random).toString(32);
    }

    public static String string() {
        return string(255);
    }
}
