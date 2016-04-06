package edu.tfai.sate2.signal;

import edu.tfai.sate2.spectra.Spectra;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import umontreal.iro.lecuyer.functionfit.BSpline;

import static edu.tfai.sate2.synthetic.batch.BatchConstants.HI_RES_LIMIT;
import static java.lang.String.format;

@Slf4j
public class Smooth {

    private static final SplineInterpolator splineInterpolator = new SplineInterpolator();


    /**
     * This smooth to be used only for continuum and shift determinations. NOT TO USE in LINE ABUNDANCE DETERMINATION
     *
     * @param spectra        spectra to smooth
     * @param freedom        freedom points
     * @param knotMultiplier multiplier
     * @return smoothed spectrum
     */
    public static Spectra getSmooth(Spectra spectra, int freedom, int knotMultiplier) {
        if (spectra.size() <= 3) {
            return spectra;
        }

        //FIXME REMOVED THE CACHE HERE

        if (spectra.getResolution() > HI_RES_LIMIT) {
            return getSmoothSpectra(spectra, freedom, knotMultiplier);
        }

        //apply different smooth function
        //FIXME treat different low resolution
        throw new IllegalArgumentException("Very low resolution spectra detected: " + spectra.getResolution());
//        return null;
    }

    private static Spectra getSmoothSpectra(Spectra spectra, int freedom, int knotMultiplier) {

        double newX[] = new double[spectra.size() * knotMultiplier - knotMultiplier];
        double newY[] = new double[spectra.size() * knotMultiplier - knotMultiplier];
        int pos = 0;
//        xySeries.setSmoothFactor(freedom);
        if (knotMultiplier < 1)
            throw new IllegalArgumentException("Knot multiplier must be >=1, was " + knotMultiplier);

        try {
            double x[] = spectra.getX();
            double y[] = spectra.getY();

            BSpline bspline = new BSpline(x, y, Math.min(freedom, spectra.size() - 2));
            for (int i = 0; i < spectra.size() - 1; i++) {
                for (int j = 0; j < knotMultiplier; j++) {
                    double xVal = x[i] + j * (x[i + 1] - x[i]) / knotMultiplier;
                    double yVal = bspline.evaluate(xVal);
                    if (Double.isNaN(yVal))
                        return getSmooth(spectra, freedom + 1, knotMultiplier);
                    newX[pos] = xVal;
                    newY[pos] = yVal;
                    pos++;
                }
            }

        } catch (Exception e) {
            log.error("Error smoothing spectra", e);
            return spectra;
        }
        log.debug(format("Smoothed spectra degrees of freedom=%s, shift=%2.4f, level=%2.4f, mult=%s",
                freedom, spectra.getShift(), spectra.getContinuumLevel(),
                Math.round((double) spectra.size() / (double) spectra.size())));

       // log.info(format("Spectra smooth %s", freedom));
        return new Spectra(newX, newY, spectra.getShift(), spectra.getContinuumLevel());
    }

    public static PolynomialSplineFunction getSplineSmoothFunction(Spectra spectra) {
        PolynomialSplineFunction function = splineInterpolator.interpolate(spectra.getX(), spectra.getY());
        return function;
    }

    /**
     * This smooth to use in LINE ABUNDANCE determination
     *
     * @param spectra spectrum to be smoothed
     * @return smoothed spectra
     */
    public static Spectra getSplineSmooth(Spectra spectra) {
        PolynomialSplineFunction splineFunction = getSplineSmoothFunction(spectra);
        double x[] = new double[spectra.size()];
        double y[] = new double[spectra.size()];
        for (int i = 0; i < spectra.size(); i++) {
            x[i] = spectra.getX(i);
            y[i] = splineFunction.value(spectra.getX(i));
        }
        log.info("Spectra spline smooth");
        return new Spectra(x, y, spectra.getShift(), spectra.getContinuumLevel());
    }
}
