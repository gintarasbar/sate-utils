package edu.tfai.sate2.spectra;

import edu.tfai.sate.eqwidth.ElementOutput;
import edu.tfai.sate.eqwidth.LineDataOutput;
import edu.tfai.sate2.model.batch.BatchResults;
import edu.tfai.sate2.utils.NumberUtil;
import edu.tfai.sate2.signal.Smooth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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
        double val = 0;
//        if (line.isUseBottomFitting()) {
        results.setLineBottom(observedSpectra.getYStats().getMin());
        val = observedSpectra.getYStats().getMin() - newSpectra.getYStats().getMin();
        results.setDifference(val);
//        } else {
//            results.setLineBottom(observedSpectra.getMinPoint().getY());
//            Double difference = observedSpectra.getMinPoint().getY() - newSpectra.getMinPoint().getY();
//            results.setDifference(difference);
//            val = MathUtil.chiSquare(newSpectra.toArray()[1], observedSpectra.toArray()[1]);
//        }
        log.debug("Chi2=" + NumberUtil.format(val, 6));
        return val;
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


}
