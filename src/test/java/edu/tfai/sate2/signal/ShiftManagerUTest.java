package edu.tfai.sate2.signal;

import edu.tfai.sate2.spectra.Spectra;
import edu.tfai.sate2.spectra.SpectraReader;
import org.junit.Test;

import java.nio.file.Path;

import static edu.tfai.sate2.signal.ShiftManager.getShift;
import static edu.tfai.sate2.utils.FileNameUtils.getPath;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class ShiftManagerUTest {

    private final Path lambdaSynthetic = getPath("o2_bsyn.txt");

    private final Path lambdaFile = getPath("HD222107_02.txt");

    private final Path sunSynthetic = getPath("o2_sun_synth.xxy");

    private final Path sunFile = getPath("sunO2.txt");

    private final Path camSynthetic = getPath("C2_syn_HD29317.txt");

    private final Path camFile = getPath("HD29317_C2.txt");

    private SpectraReader reader = new SpectraReader();

    @Test
    public void testShiftSyntheticSpectraO2() {
        Spectra spectra = reader.loadSyntheticSpectra(sunSynthetic, "02").getSubSpectra(100,1100);
        Spectra copy  = spectra.copy();
        copy.shift(-0.3);
        double shiftValue = getShift(null, copy, spectra);
        assertThat(shiftValue, closeTo(0.3, 0.001));

    }

    @Test
    public void testShiftSyntheticSpectra() {
        Spectra spectra = reader.loadSyntheticSpectra(lambdaFile, "02");
        Spectra copy  = spectra.copy();
        copy.shift(-0.3);
        double shiftValue = getShift(null, copy, spectra);
        assertThat(copy.getShift(), closeTo(-0.3, 0.001));
        assertThat(shiftValue, closeTo(0.3, 0.001));

    }

    @Test
    public void testShiftSyntheticSpectraSun() {
        Spectra spectra = reader.loadSyntheticSpectra(sunFile, "02");
        Spectra copy  = spectra.copy();
        copy.shift(0.3);
        double shiftValue = getShift(null, copy, spectra);
        assertThat(shiftValue, closeTo(-0.3, 0.001));

    }

    @Test
    public void testShiftOriginalSyntheticForSun() {
        Spectra synthSp = reader.loadSyntheticSpectra(sunSynthetic, "02").getSubSpectraByWave(6296.0800, 6304.9900);
        Spectra sun = reader.loadSyntheticSpectra(sunFile, "02").getSubSpectraByWave(6296.0800, 6304.9900);
        double shiftValue = getShift(null, sun,synthSp);
        assertThat(sun.getShift(), closeTo(0.0, 0.0001));
        assertThat(shiftValue, closeTo(-0.008, 0.001));

    }


    @Test
    public void testShiftOriginalSyntheticForLambdaAndromeda() {
        Spectra synthSp = reader.loadSyntheticSpectra(lambdaSynthetic, "02").getSubSpectraByWave(6290.0800, 6310.0000);
        Spectra star = reader.loadSyntheticSpectra(lambdaFile, "02").getSubSpectraByWave(6290.0800, 6310.0000);
        double shiftValue = getShift(null, star,synthSp);
        assertThat(shiftValue, closeTo(-0.17, 0.001));

    }

    @Test
    public void testShiftOriginalSyntheticC2ForHD29317() {
        Spectra synthSp = reader.loadSyntheticSpectra(camSynthetic, "C2").getSubSpectraByWave(5130.700,5140.700);
        Spectra star = reader.loadSyntheticSpectra(camFile, "C2").getSubSpectraByWave(5130.700,5140.700);
        double shiftValue = getShift(null, star,synthSp);
        assertThat(shiftValue, closeTo(0.13, 0.01));

    }

}