package edu.tfai.sate2.signal;


import edu.tfai.sate.objects.LineData;
import edu.tfai.sate2.exceptions.IllegalShiftDetected;
import edu.tfai.sate2.model.batch.BatchParameters;
import edu.tfai.sate2.model.batch.BatchResults;
import edu.tfai.sate2.spectra.Profile;
import edu.tfai.sate2.spectra.Spectra;
import edu.tfai.sate2.spectra.SpectralUtils;
import edu.tfai.sate2.utils.LeastSquareUtil;
import edu.tfai.sate2.utils.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.Line;

import static edu.tfai.sate.objects.Element.getIdentification;
import static edu.tfai.sate2.signal.Smooth.getSmooth;
import static edu.tfai.sate2.spectra.SpectralUtils.removeNegativePoints;
import static java.lang.String.format;

@Slf4j
public abstract class ShiftManager {

    public static double getShift(LineData line, Spectra obsSpectra, Spectra synthSpectra) {

        Stopwatch stopwatch = Stopwatch.getInstance();

        double step = BatchParameters.SHIFT_STEP;
        double shiftStart = -4d;
        double shiftEnd = 4d;

        obsSpectra = removeNegativePoints(obsSpectra);
        synthSpectra = removeNegativePoints(synthSpectra);

        Spectra smoothedSpectra = getSmooth(obsSpectra, 20, 1);

        double primaryShift = determineShift(obsSpectra, synthSpectra, step, shiftStart, shiftEnd, smoothedSpectra);
        //TODO store star and shift for each line in cache, key spectraFile+Element+line
        if (primaryShift <= shiftStart || primaryShift >= shiftEnd) {
            //FIXME mean velocity should go here instead of zero
            throw new IllegalShiftDetected(line.getWavelength(), primaryShift, 0, new BatchResults(getIdentification(line), line.getWavelength()));
        }

        double microShift = 0;
        if (obsSpectra.getStep() < step) {
            smoothedSpectra.shift(-shiftEnd + primaryShift);
            microShift = determineShift(obsSpectra, synthSpectra, obsSpectra.getStep() / 2.0d, -step * 2, step * 2, smoothedSpectra);
        }
        stopwatch.logTimeDebug("Shift determination");

        double totalShift = primaryShift + microShift;
        log.info(format("Total shift found %.4f", totalShift));
        return totalShift;
    }

    private static double determineShift(Spectra obsSpectra, Spectra synthSpectra, double step, double shiftStart, double shiftEnd, Spectra smoothedSpectra) {
        double maxCovShift = 0;
        double minimum = Double.MAX_VALUE;
        for (double shift = shiftStart; shift < shiftEnd; shift += step) {
            // apply shift for spectra
            if (shift == shiftStart) {
                smoothedSpectra.shift(shift);
            } else {
                smoothedSpectra.shift(step);
            }

            double val = LeastSquareUtil.squareDifferenceStDev(smoothedSpectra, synthSpectra);
            if (minimum >= val && val > 0) {
                minimum = val;
                maxCovShift = shift;
            }
//            System.out.println(smoothedSpectra.getShift() + "  " + val);
        }

        log.info(format("Found shift=%.4f, chi^2=%.5f, range=%s", maxCovShift, minimum, obsSpectra.getRange()));
        return maxCovShift;
    }


    public static double smallShiftCorrection(LineData line, Spectra observed, Spectra synthetic2,
                                              double[] waves) throws Exception {
        double shift = 0.0d;
        if (line.isUseExtraShift()) {

            try {
                Spectra obsSmooth = Smooth.getSmooth(observed, 20, 1);
                obsSmooth = Profile.extractSpectra(waves[0], waves[1], obsSmooth);
                double center = Profile.getCenterWavelength(line, obsSmooth);
                double center2 = Profile.getCenterWavelength(line, synthetic2);
                shift = center2 - center;
                log.debug(format("Extra shift1:%.5f, range=%s, obsCenter=%.3f, synthCenter=%.3f", shift,
                        obsSmooth.getRange(), center, center2));
                if (Math.abs(shift) <= 0.001d || Math.abs(shift) > 0.15d) {
                    shift = 0.0d;
                }
            } catch (Exception e) {
                log.warn("Warning: "
                        + String.format("Check line instrumentalProfile in star:%s, line:%s=%.2f", BatchParameters.STAR_NAME,
                        line.getElementReference().getIdentification(), line.getWavelength()));
                shift = 0;
            }
        }

        //log.info(format("Found extraShift=%.4f", shift));
        return shift;
    }
}
