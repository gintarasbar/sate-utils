package edu.tfai.sate2.utils;

import edu.tfai.sate2.signal.Smooth;
import edu.tfai.sate2.spectra.Spectra;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class LeastSquare {

    public static double residuals(Spectra obsSpectra, Spectra syntheticSpectra) {

        PolynomialSplineFunction function = Smooth.getSplineSmoothFunction(syntheticSpectra);
        List<Double> xList = newArrayList();
        List<Double> newSpectraList = newArrayList();
        List<Double> observedSpectraList = newArrayList();


        for (int i = 0; i < obsSpectra.size(); i++) {
            double x = obsSpectra.getX(i);
            if (syntheticSpectra.isInRange(x)) {
                xList.add(x);
                newSpectraList.add(function.value(x));
                observedSpectraList.add(obsSpectra.getY(i));
            }
        }

        double sum = 0;
        for (int i = 0; i < observedSpectraList.size(); i++) {
            double y = newSpectraList.get(i);
            double yObs = observedSpectraList.get(i);

            sum += Math.pow(yObs - y, 2);
        }

        //TODO we could use https://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/stat/correlation/PearsonsCorrelation.html
        //http://alvinalexander.com/java/jwarehouse/commons-math/src/test/java/org/apache/commons/math/stat/correlation/PearsonsCorrelationTest.java.shtml
        //https://commons.apache.org/proper/commons-math/javadocs/api-3.4/org/apache/commons/math3/stat/inference/ChiSquareTest.html
        return sum;
    }
}
