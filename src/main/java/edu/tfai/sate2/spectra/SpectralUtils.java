package edu.tfai.sate2.spectra;

import com.google.common.primitives.Doubles;
import edu.tfai.sate.eqwidth.ElementOutput;
import edu.tfai.sate.eqwidth.LineDataOutput;
import edu.tfai.sate2.model.batch.BatchParameters;
import edu.tfai.sate2.model.batch.BatchResults;
import edu.tfai.sate2.utils.LeastSquareUtil;
import edu.tfai.sate2.utils.NumberUtil;
import edu.tfai.sate2.signal.Smooth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.util.MathUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.primitives.Doubles.asList;
import static edu.tfai.sate2.model.batch.BatchParameters.DEFAULT_SMOOTH_FACTOR;
import static edu.tfai.sate2.signal.Smooth.getSmooth;
import static java.lang.String.format;

@Slf4j
public class SpectralUtils {


    public static double compareProfiles(Spectra obsSpectra, Spectra synthSpectra,
                                         BatchResults results) throws Exception {
        PolynomialSplineFunction function = Smooth.getSplineSmoothFunction(synthSpectra);
        List<Double> xList = newArrayList();
        List<Double> yNewSpectra = newArrayList();
        List<Double> yObservedSpectra = newArrayList();

        for (int i = 0; i < obsSpectra.size(); i++) {
            double x = obsSpectra.getX(i);
            if (synthSpectra.isInRange(x)) {
                xList.add(x);
                yNewSpectra.add(function.value(x));
                yObservedSpectra.add(obsSpectra.getY(i));
            }
        }

        Spectra newSpectra = new Spectra(xList, yNewSpectra);
        Spectra observedSpectra = new Spectra(xList, yObservedSpectra);

        results.setLineBottom(observedSpectra.getYStats().getMin());
        double val = observedSpectra.getYStats().getMin() - newSpectra.getYStats().getMin();
        results.setDifference(val);

        double chiSquare = LeastSquareUtil.squareDifferenceStDevNoSmoothing(asList(newSpectra.getY()), asList(observedSpectra.getY()));
        results.setChi2Error(chiSquare);
        log.debug("Chi2=" + NumberUtil.format(chiSquare, 6));
        return chiSquare;
    }

    public static void saveSpectra(Spectra spectra, String file) throws Exception {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < spectra.size(); i++) {
                bw.write(format("%.4f\t%.4f\n", spectra.getX(i), spectra.getY(i)));
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.error("Error saving spectra", e);
            throw new Exception("Error while saving spectra " + file + ". " + e.getMessage());
        }
    }


    public static Spectra removeNegativePoints(Spectra spectra) {
        double y[] = spectra.getY();
        double level = spectra.getYStats().getMax();
        for (int i = 0; i < spectra.size(); i++) {
            if (y[i] <= 0.001d) {
                y[i] = level;
            }
        }
        return spectra;
    }


    public static Spectra getSeriesFromData(ElementOutput element, int xIndex, int yIndex) {
        ArrayList<Double> xVal = new ArrayList<Double>();
        ArrayList<Double> yVal = new ArrayList<Double>();
        for (int j = 0; j < element.getLineCount(); j++) {
            LineDataOutput line = element.getLines().get(j);

            xVal.add(((Number) line.getDataArray()[xIndex]).doubleValue());

            yVal.add(((Number) line.getDataArray()[yIndex]).doubleValue());

        }
        Spectra series = new Spectra(xVal, yVal);
        return series;
    }

    public static Spectra prepareSpectra(Spectra originalSpectra) {
        if (BatchParameters.SMOOTH_ENABLED) {
            return getSmooth(originalSpectra, DEFAULT_SMOOTH_FACTOR, 1);
        } else {
            return originalSpectra.copy();
        }
    }

}
