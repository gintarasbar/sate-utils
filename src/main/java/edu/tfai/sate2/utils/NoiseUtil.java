package edu.tfai.sate2.utils;


import com.google.inject.Singleton;
import edu.tfai.sate2.spectra.Spectra;

import java.util.Random;

@Singleton
public class NoiseUtil {

    public static final int NOISE_THRESHOLD = 10;

    public static Spectra addNoise(Spectra spectra, double noiseLevel) {
        double y[] = spectra.getY();
        addNoise(y, noiseLevel);
        spectra.recalculateStatistics();
        return spectra;
    }

    public static void addNoise(double[] y, double noiseLevel) {
        for (int i = 0; i < y.length; i++) {
            y[i] += (0.5 - Math.random()) * noiseLevel;
        }
    }

    private static int randomSign(Random random) {
        int randomNumber = (random.nextInt(2) - 1);
        return randomNumber == 0 ? 1 : randomNumber;
    }

    public static double noiseCheck(Spectra spectra) {
        double snRatio = spectra.getYStats().getStandardDeviation() * 2000.0;
        return snRatio;

//        results.setSnRatio(snRatio);
//        if (snRatio < NOISE_THRESHOLD)
//            throw new NoiseException(line.getWavelength(), line, snRatio);
    }
}
