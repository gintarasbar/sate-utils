package edu.tfai.sate2.spectra;

import edu.tfai.sate2.exceptions.SpectraOutOfRange;
import edu.tfai.sate.model.LineData;
import lombok.extern.slf4j.Slf4j;

import static edu.tfai.sate.model.Element.getIdentification;
import static edu.tfai.sate2.utils.BinarySearch.search;
import static java.lang.String.format;

@Slf4j
public class Profile {

    /**
     * Returns wavelength index
     */
    public static int getCenter(LineData line, Spectra spectra) throws Exception {
        int i = search(spectra.getX(), line.getWavelength());
        int size = spectra.size();
        if (i == 0 || i + 1 == size)
            throw new SpectraOutOfRange(line.getWavelength(), spectra.getMinX(), spectra.getMaxX(), getIdentification(line));

        double intense = spectra.getY(i);
        while (i - 1 >= 0 && spectra.getY(i - 1) < intense) {
            i--;
            intense = spectra.getY(i);
        }

        while (i + 1 < size && spectra.getY(i + 1) < intense) {
            i++;
            intense = spectra.getY(i);
        }

        return i;
    }

    public static double getCenterWavelength(LineData line, Spectra spectra) throws Exception {
        return spectra.getX(getCenter(line, spectra));
    }

    /**
     * Extracts spectra according to wavelengths, cutting off the lowered left
     * part and cuts the double line in the end if there is any
     */
    public static Spectra extractSpectra(double startWave, double endWave, Spectra spectra)
            throws Exception {
        int start = search(spectra.getX(), startWave);
        int end = search(spectra.getX(), endWave);
        //FIXME when extracting apply gaussian

        Spectra sp = spectra.getSubSpectra(start, end);
        //TODO removed setting of smoothing
        return sp;
    }


    /**
     * Evaluates the ranges for the line
     */
    public static double[] getWaveRangeForLine(LineData line, Spectra spectra3)
            throws Exception {
        Spectra profile = getLineProfile(line, spectra3);
        double range = profile.getMaxX() - profile.getMinX();
        double percent = 0.15;
        return new double[]{profile.getMinX() + range * percent, profile.getMaxX() - range * percent};
    }

    /**
     * Changes original spectra and returns minimum value
     */
    private static Spectra getLineProfile(LineData line, Spectra spectra)
            throws Exception {
        int center = getCenter(line, spectra);
        double centerX = spectra.getX(center);

        int i = center;
        // X axis cut-off
        while (i - 1 >= 0 && spectra.getY(i - 1) >= spectra.getY(i)) {
            double currentX = spectra.getX(i - 1);
            if (Math.abs(currentX - centerX) > 0.2)
                break;
            i--;
        }
        int start = i;
        i = center;
        while (i + 1 < spectra.size() && spectra.getY(i + 1) >= spectra.getY(i)) {
            double currentX = spectra.getX(i + 1);
            if (Math.abs(currentX - centerX) > 0.2)
                break;
            i++;
        }
        int end = i;
        Spectra newProfile = spectra.getSubSpectra(start, end);

        log.debug(format("Center of line=%2.4f, shift=%2.4f, level=%2.4f, range=%s", centerX,
                newProfile.getShift(), newProfile.getContinuumLevel(), newProfile.getRange()));
        return newProfile;
    }

    public static Spectra extractProfile(LineData line, Spectra originalSpectra) throws Exception {
        double[] waveRangeForLine = Profile.getWaveRangeForLine(line, originalSpectra);
        return Profile.extractSpectra(waveRangeForLine[0], waveRangeForLine[1], originalSpectra);
    }

    public static double[] getRange(Spectra spectra) {
        return new double[]{spectra.getMinX(), spectra.getMaxX()};
    }
}