package edu.tfai.sate2.spectra;

import edu.tfai.sate2.utils.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import static java.lang.String.format;


@Slf4j
public class FWMHCalculator {

    public static double getFWMH(Spectra spectra, double centerWave, double centerIntensity, String lineId) {
        double[] parameters = fitGaussian(spectra, centerWave, lineId);
//        System.out.println(Arrays.toString(parameters));
        double height = 1 - centerIntensity;
        double area = calculateArea(parameters, height);
        return area* 1000;
        // results.setEqw1(Math.abs(area) * 1000);
    }

    private static double calculateArea(double [] parameters, double height){
        double sigma = parameters[2];
        // double fwhm = 2 * Math.sqrt(2 * Math.myLogger(2)) * d;
        //TODO heigh here can be as well parameters[2]
        return sigma * height * Math.sqrt(Math.PI * 2);
    }

    private static double[] fitGaussian(Spectra spectra, double centerWave, String lineId) {

        try {
            Stopwatch instance = Stopwatch.getInstance();

            GaussianCurveFitter fitter = GaussianCurveFitter.create();
            WeightedObservedPoints obs = new WeightedObservedPoints();
            for (int index = 0; index < spectra.size(); index++) {
                obs.add(spectra.getX(index), 1- spectra.getY(index));
            }
            double[] parameters = fitter.fit(obs.toList());
            //A, B, C, and D which are, respectively, y offset, amplitude, centroid position, and sigma.
            instance.logTimeDebug("Gauss calculation");
            return parameters;
        } catch (Exception e) {
            log.error("gauss error", e);
            log.warn(format("Error fitting gauss for line %s %.3f: %s", lineId, centerWave, e.getMessage()));
        }
        return new double[]{0, 0, 0, 0};

    }
}
