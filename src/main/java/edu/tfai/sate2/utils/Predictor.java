package edu.tfai.sate2.utils;

import com.google.common.base.Optional;
import com.google.common.primitives.Doubles;
import edu.tfai.sate2.utils.math.PolynomialFitter;

import javax.swing.text.html.Option;
import java.util.List;

public class Predictor<T extends Number, A extends Number> {

    private PolynomialFitter.Polynomial pol;

    private boolean hasPrediction = true;

    public Predictor(List<T> x, List<A> y, int freedom) {
        if (x.size() > freedom) {
            PolynomialFitter polynomialFitter = new PolynomialFitter(freedom);
            for (int i = 0; i < x.size(); i++) {
                polynomialFitter.addPoint(x.get(i).doubleValue(), y.get(i).doubleValue());
            }
            pol = polynomialFitter.getBestFit();
        } else {
            hasPrediction = false;
        }
    }

    public Optional<Double> predict(double value, int decimalPlaces) {
        if (hasPrediction) {
            long precision = (long) Math.pow(10, decimalPlaces);
            double estimated = Math.round(pol.getY(value) * precision) / (double) precision;
            return Optional.of(estimated);
        } else {
            return Optional.absent();
        }

    }
}
