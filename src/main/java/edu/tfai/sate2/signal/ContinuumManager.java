package edu.tfai.sate2.signal;

import com.google.common.primitives.Doubles;
import edu.tfai.sate2.spectra.Spectra;
import edu.tfai.sate2.utils.LeastSquareUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.List;

import static edu.tfai.sate2.signal.Smooth.getSmooth;
import static edu.tfai.sate2.synthetic.batch.BatchConstants.HI_RES_LIMIT;
import static java.lang.String.format;

@Slf4j
public abstract class ContinuumManager {

    public static double fixContinuum(Spectra originalSpectra, Spectra synthetic) {
//        double mult1 = fixUsingPercentile(originalSpectra.copy(), synthetic);
        double mult2 = fixUsingPlateMaxima(originalSpectra.copy(), synthetic);
//
//        if (mult1 < mult2) {
//            log.info(format("Continuum fix1:%.4f", mult1));
//            return mult1;
//        }
        log.info(format("Continuum fix2:%.4f", mult2));
        return mult2;
    }

    private static double fixUsingPercentile(Spectra originalSpectra, Spectra synthetic) {

        log.debug("Using smoothing for continuum: " + 20);

        Spectra contSeries = getSmooth(originalSpectra, 20, 1);

        if (originalSpectra.getResolution() > HI_RES_LIMIT) {
            double yMax = contSeries.getYStats().getMax();
            double yMaxSyn = synthetic.getYStats().getMax();

            double median = contSeries.getYStats().getPercentile(75);
            int size = contSeries.size();

            List<Double> xx = new ArrayList(originalSpectra.size() / 2);
            List<Double> yy = new ArrayList(originalSpectra.size() / 2);

            for (int i = 0; i < size; i++) {
                if (contSeries.getY(i) > median) {
                    xx.add(contSeries.getX(i));
                    yy.add(contSeries.getY(i));
                }
            }

            Spectra sp = new Spectra(Doubles.toArray(xx), Doubles.toArray(yy));
            median = (sp.getYStats().getPercentile(85) + yMax) / 2;

            double mult = yMaxSyn / median;
            log.debug("Range for continuum " + (contSeries.getMaxX() - contSeries.getMinX()));

            return mult;
        }
        throw new IllegalStateException("Low resolution spectra detected on continuum fix: " + originalSpectra.getResolution());

    }

    private static double fixUsingPlateMaxima(Spectra originalSpectra, Spectra synthetic) {

        int points = 3;
        double[] maxPoints1 = getMaxPoints(getSmooth(originalSpectra, 1, 1), points);
        double[] maxPoints2 = getMaxPoints(synthetic, points);

        double multiplier = 0.0d;
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        for (int i = 0; i < points; i++) {
            double value = maxPoints2[i] / maxPoints1[i];
            if(i==1) {
                value = value + value * 10;
            }
            multiplier += value;
//            System.out.println(maxPoints2[i] / maxPoints1[i]);
        }
        multiplier = multiplier / (points + 10);
        return multiplier;
    }

    private static double[] getMaxPoints(Spectra spectra, int points) {

        double levels[] = new double[points];

        int totalPoints = points;//* 2 - 1;
        int midP = (int) Math.floor(spectra.size() / totalPoints) - 1;
        for (int i = 0; i < totalPoints; i += 1) {
            Spectra sp = spectra.getSubSpectra(midP * i, midP * (i + 1));
            levels[i] = sp.getYStats().getMax();
        }
        return levels;
    }


    public static double compareSpectraLevel(Spectra obsSpectra, Spectra synthetic) {
        Spectra smoothedSpectra = getSmooth(obsSpectra, 20, 1);
        double val = LeastSquareUtil.squareDifferenceStDev(smoothedSpectra, synthetic);
        return val;
    }

}