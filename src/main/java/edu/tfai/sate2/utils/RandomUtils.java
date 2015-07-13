package edu.tfai.sate2.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    public static String string(){
        return new BigInteger(130, random).toString(32);
    }
}
