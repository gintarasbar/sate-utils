package edu.tfai.sate2.utils;

import edu.tfai.sate2.signal.Smooth;
import edu.tfai.sate2.spectra.Spectra;
import lombok.Data;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Math.abs;

public class LeastSquareUtil {

    public static double residuals(Spectra obsSpectra, Spectra syntheticSpectra) {

        SmoothedSpectra smoothedSpectra = new SmoothedSpectra(obsSpectra, syntheticSpectra).invoke();
        List<Double> newObservedY = smoothedSpectra.getNewObservedSpectraYValues();
        List<Double> newSyntheticY = smoothedSpectra.getNewSynthSpectraYValues();


        double sum = 0;
        for (int i = 0; i < newObservedY.size(); i++) {
            sum += Math.pow(newObservedY.get(i) - newSyntheticY.get(i), 2);
        }

        //TODO we could use https://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/stat/correlation/PearsonsCorrelation.html
        //http://alvinalexander.com/java/jwarehouse/commons-math/src/test/java/org/apache/commons/math/stat/correlation/PearsonsCorrelationTest.java.shtml
        //https://commons.apache.org/proper/commons-math/javadocs/api-3.4/org/apache/commons/math3/stat/inference/ChiSquareTest.html
        return sum;
    }

    public static double squareDifferenceStDev(Spectra obsSpectra, Spectra syntheticSpectra) {

        SmoothedSpectra smoothedSpectra = new SmoothedSpectra(obsSpectra, syntheticSpectra).invoke();
        List<Double> newObservedY = smoothedSpectra.getNewObservedSpectraYValues();
        List<Double> newSyntheticY = smoothedSpectra.getNewSynthSpectraYValues();

        SummaryStatistics stat = new SummaryStatistics();
        for (int i = 0; i < newObservedY.size(); i++) {
            stat.addValue(Math.pow(newSyntheticY.get(i) - newObservedY.get(i), 2));
        }

        return abs(stat.getStandardDeviation());
    }

    @Data
    private static class SmoothedSpectra {
        private final Spectra obsSpectra;
        private final Spectra syntheticSpectra;
        private List<Double> newSynthSpectraYValues;
        private List<Double> newObservedSpectraYValues;
        private List<Double> xValues;

        public SmoothedSpectra invoke() {
            if (obsSpectra.size() < 3 && syntheticSpectra.size() < 3) {
                throw new IllegalArgumentException("Spectrum must have more than 2 points");
            }

            PolynomialSplineFunction function = Smooth.getSplineSmoothFunction(syntheticSpectra);
            xValues = newArrayList();
            newSynthSpectraYValues = newArrayList();
            newObservedSpectraYValues = newArrayList();


            for (int i = 0; i < obsSpectra.size(); i++) {
                double x = obsSpectra.getX(i);
                double y = obsSpectra.getY(i);
                if (syntheticSpectra.isInRange(x) && y > 0.0001) {
                    xValues.add(x);
                    newSynthSpectraYValues.add(function.value(x));
                    newObservedSpectraYValues.add(obsSpectra.getY(i));
                }
            }
            return this;
        }
    }
}
