package edu.tfai.sate2.signal;


import com.google.common.base.Stopwatch;
import edu.tfai.sate.model.LineData;
import edu.tfai.sate2.exceptions.IllegalShiftDetected;
import edu.tfai.sate2.model.batch.BatchParameters;
import edu.tfai.sate2.model.batch.BatchResults;
import edu.tfai.sate2.spectra.Profile;
import edu.tfai.sate2.spectra.Spectra;
import edu.tfai.sate2.utils.LeastSquareUtil;
import edu.tfai.sate2.utils.RadialVelocityUtil;
import lombok.extern.slf4j.Slf4j;

import static edu.tfai.sate.model.Element.getIdentification;
import static edu.tfai.sate2.signal.Smooth.getSmooth;
import static edu.tfai.sate2.spectra.SpectralUtils.removeNegativePoints;
import static edu.tfai.sate2.utils.TimeUtils.formatTime;
import static java.awt.geom.Point2D.distance;
import static java.lang.Math.round;
import static java.lang.Math.signum;
import static java.lang.String.format;

@Slf4j
public abstract class ShiftManager {

    public static final double SHIFT_START = -4d;
    public static final double SHIFT_END = 4d;

    public static double getShift(LineData line, Spectra obsSpectra, Spectra synthSpectra) {

        Stopwatch stopwatch = Stopwatch.createStarted();

        double step = BatchParameters.SHIFT_STEP;
        double shiftStart = SHIFT_START;
        double shiftEnd = SHIFT_END;

        obsSpectra = removeNegativePoints(obsSpectra.copy());
        synthSpectra = removeNegativePoints(synthSpectra);

        Spectra smoothedSpectra = getSmooth(obsSpectra, 20, 1);

        double primaryShift = determineShift(synthSpectra, step, shiftStart, shiftEnd, smoothedSpectra);

        if (primaryShift <= shiftStart || primaryShift >= shiftEnd) {
            throw new IllegalShiftDetected(line.getWavelength(), primaryShift, RadialVelocityUtil.shiftToRadialVelocity(line.getWavelength(), primaryShift), new BatchResults(getIdentification(line), line.getWavelength()));
        }

        double microShift = 0;
//        if (obsSpectra.getStep() < step) {
//            smoothedSpectra.shift(-shiftEnd + primaryShift);
////            microShift = determineShift(obsSpectra, synthSpectra, obsSpectra.getStep() / 2.0d, -step * 2, step * 2, smoothedSpectra);
//            try {
//                microShift = smallShiftCorrection(line, obsSpectra, synthSpectra);
//            } catch (Exception e) {
//                log.error("Microshift failed {}", e);
//                microShift = 0;
//            }
//        }
//        try {
//            microShift = extraShift(line, obsSpectra, synthSpectra);
//        } catch (Exception e) {
//
//        }
//        log.debug("Shift determination time=%s", formatTime(stopwatch));


        double totalShift = primaryShift + microShift;
        log.debug(format("Total shift found %.4f", totalShift));
        return totalShift;
    }

    private static double determineShift(Spectra synthSpectra, double step, double shiftStart, double shiftEnd, Spectra smoothedSpectra) {
        double maxCovShift = 0;
        double minimum = Double.MAX_VALUE;

        for (double shift = shiftStart; shift < shiftEnd; shift += step) {
            // applying shift for spectra
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
        }

        log.debug(format("Found shift=%.4f, chi^2=%.5f, range=%s", maxCovShift, minimum, smoothedSpectra.getRange()));
        return maxCovShift;
    }

    private static double smallShiftCorrection(LineData line, Spectra observed, Spectra synthetic) throws Exception {
        double shift = 0.0d;
        if (line.isUseExtraShift()) {

            try {
                double center2 = Profile.getCenterWavelength(line, synthetic);
                double center = Profile.getCenterWavelength(line, observed);
                observed = Profile.extractProfile(line, observed);
                shift = center2 - center;
                log.debug(format("Extra shift1:%.5f, range=%s, obsCenter=%.3f, synthCenter=%.3f", shift,
                        observed.getRange(), center, center2));
                if (Math.abs(shift) > 0.15d) {
                    log.info(format("Found extraShift exceeded=%.4f", shift));
                    shift = 0.0d;
                }
            } catch (Exception e) {
                log.warn("Warning: "
                        + String.format("Check line instrumentalProfile in star:%s, line:%s=%.2f", BatchParameters.STAR_NAME,
                        line.getElementReference().getIdentification(), line.getWavelength()));
                shift = 0;
            }
        }

        // log.info(format("Found extraShift=%.4f", shift));
        return shift;
    }


    private static double extraShift(LineData line, Spectra observed, Spectra synthetic) throws Exception {
        double shift = 0.0d;
        if (line.isUseExtraShift()) {

            try {
                double synCenter = Profile.getCenterWavelength(line, synthetic);
                double obsCenter = Profile.getCenterWavelength(line, observed);

                double[] waveSyn = Profile.getWaveRangeForLine(line, synthetic);
                double[] waveObs = Profile.getWaveRangeForLine(line, observed);

                observed = Profile.extractSpectra(observed, waveObs);
                synthetic = Profile.extractSpectra(synthetic, waveSyn);

                int pos1 = (int) round(observed.size() / 4.0) + 1;
                int pos2 = (int) round(observed.size() * 0.75) - 1;

                int posSyn1 = synthetic.findClosestPoint(observed.getX(pos1), observed.getY(pos1));
                int posSyn2 = synthetic.findClosestPoint(observed.getX(pos2), observed.getY(pos2));

                double dist1 = synthetic.getX(posSyn1) - observed.getX(pos1);
                double dist2 = synthetic.getX(posSyn2) - observed.getX(pos2);

                double dist3 = synCenter - obsCenter;
                shift = 0;
                log.info("{} dist1={}, dist2={}, dist3={}", line.getWavelength(), dist1, dist2, dist3);
                log.info("{} sx1={}, sx2={}, sy1={}, sy2={}", line.getWavelength(), synthetic.getX(posSyn1), synthetic.getX(posSyn2), synthetic.getY(posSyn1), synthetic.getY(posSyn2));
//                shift = (dist1 + dist2 + dist3) / 3.0;
//                if (Math.abs(shift) > 0.2d) {
//                    log.info(format("Found extraShift exceeded=%.4f", shift));
//                    shift = 0.0d;
//                }
            } catch (Exception e) {
                log.warn("Warning: "
                        + String.format("Check line instrumentalProfile in star:%s, line:%s=%.2f", BatchParameters.STAR_NAME,
                        line.getElementReference().getIdentification(), line.getWavelength()));
                shift = 0;
            }
        }

        // log.info(format("Found extraShift=%.4f", shift));
        return shift;
    }
}
