package edu.tfai.sate2.spectra;

import edu.tfai.sate2.utils.BinarySearch;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.google.common.primitives.Doubles.toArray;
import static edu.tfai.sate2.utils.RadialVelocityUtil.radialVelocityToShift;
import static java.lang.Math.max;
import static java.lang.String.format;

@Getter
@Slf4j
public class Spectra implements Serializable {

    @Setter
    @Getter
    private boolean cached = false;
    public static final double NOISE_LEVEL = 0.3;
    private DescriptiveStatistics yStats;
    private DescriptiveStatistics xStats;

    private final double[] x;
    private final double[] y;

    private double shift = 0;
    private double continuumLevel = 1.0;

    @Setter
    private String instrument;

    public Spectra(List<Double> x, List<Double> y) {
        this(toArray(x), toArray(y));
    }

    public Spectra(List<Double> x, List<Double> y, double shift, double normalizationLevel) {
        this(toArray(x), toArray(y), shift, normalizationLevel);
    }

    public Spectra(double[] x, double[] y, double shift, double normalizationLevel) {
        this(x, y);
        this.shift = shift;
        this.continuumLevel = normalizationLevel;
    }

    public Spectra(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        if (x == null || y == null || x.length != y.length) {
            throw new IllegalArgumentException(format("Array lengths of spectra must be equal %.3f!=%.3f and not empty.", x.length, y.length));
        }
        recalculateStatistics();
    }

    public void recalculateStatistics() {
        yStats = new DescriptiveStatistics(y);
        xStats = new DescriptiveStatistics(x);
    }

    public Spectra getSubSpectra(int start, int end) {
        if (start >= end || start < 0 || end >= size()) {
            throw new IllegalArgumentException(format("The range [%s,%s] is incorrect for size %s list.", start, end, size()));
        }
        Spectra newSpectra = new Spectra(Arrays.copyOfRange(x, start, Math.min(end + 1, size())), Arrays.copyOfRange(y, start, Math.min(end + 1, size())), shift, continuumLevel);
        newSpectra.setCached(cached);
        return  newSpectra;
    }

    public Spectra copy() {
        double x[] = new double[size()];
        double y[] = new double[size()];
        for (int i = 0; i < size(); i++) {
            x[i] = this.x[i];
            y[i] = this.y[i];
        }
        return new Spectra(x, y, shift, continuumLevel);
    }


    public void applyContinuumLevel(double factor) {
        for (int i = 0; i < size(); i++) {
            y[i] *= factor;
        }
        continuumLevel *= factor;
        recalculateStatistics();
    }

    public void shift(double shift) {
//        if (AdminManager.getInstance().hasNOISE) {
//            log.info("Noise detected in the signal while shift");
//            getNoiseLevel(this.y, NOISE_LEVEL);
//            recalculateStatistics();
//        }

        for (int i = 0; i < size(); i++) {
            x[i] += shift;
        }
        this.shift += shift;
    }

    public int size() {
        return x.length;
    }

    public int findPosition(double xValue) {
        return BinarySearch.search(x, xValue);
    }

    public double getX(int pos) {
        return x[pos];
    }

    public double getY(int pos) {
        return y[pos];
    }

    public double getMinX() {
        return xStats.getMin();
    }

    public double getMaxX() {
        return xStats.getMax();
    }


    public Spectra getSubSpectraByWave(double startWave, double endWave) {
        startWave = max(getMinX(), startWave);
        int startIndex = BinarySearch.search(x, startWave);
        int endIndex = BinarySearch.search(x, endWave);
        return getSubSpectra(startIndex, endIndex);
    }

    public void radialShift(double radial) {
//        if (AdminManager.getInstance().hasNOISE) {
//            log.info("Noise detected in the signal while radial shift");
//            getNoiseLevel(this.y, NOISE_LEVEL);
//            recalculateStatistics();
//        }

        for (int i = 0; i < size(); i++) {
            x[i] += radialVelocityToShift(x[i], radial);
        }
    }

    public String getRange() {
        return format("[%.3f,%.3f]", getMinX(), getMaxX());
    }

    public double getStep() {
        return x[1] - x[0];
    }

    public int getResolution() {
        if (x.length < 2) {
            throw new IllegalArgumentException("Spectra cannot be less than 2 points. Unable to find resolution value.");
        }

        double R = x[0] / getStep();
        return (int) Math.ceil(R);
    }

    public boolean isInRange(double xValue) {
        if (xValue >= getMinX() && xValue <= getMaxX())
            return true;
        return false;
    }

    public void normalize() {
        double multiplier = 1.0d / yStats.getMax();
        for (int i = 0; i < size(); i++) {
            y[i] *= multiplier;
        }
        recalculateStatistics();
    }
}
