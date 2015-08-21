package edu.tfai.sate2.signal;

import edu.tfai.sate2.spectra.Spectra;
import edu.tfai.sate2.spectra.SpectraReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static edu.tfai.sate2.signal.ShiftManager.getShift;
import static edu.tfai.sate2.utils.FileNameUtils.getPath;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class ShiftManagerUTest {

    private final Path lambdaSynthetic = getPath("o2_bsyn.xxy");

    private final Path lambdaFile = getPath("HD222107_02.xxy");

    private final Path sunSynthetic = getPath("o2_sun_synth.xxy");

    private final Path sunFile = getPath("sunO2.xxy");

    private final Path camSynthetic = getPath("C2_syn_HD29317.xxy");

    private final Path camFile = getPath("HD29317_C2.xxy");

    private SpectraReader reader = new SpectraReader();

    @Test
    public void testShiftSyntheticSpectraO2() throws Exception {
        Spectra spectra = reader.loadSyntheticSpectra(sunSynthetic, "02").getSubSpectra(100,1100);
        Spectra copy  = spectra.copy();
        copy.shift(-0.3);
        double shiftValue = getShift(null, copy, spectra);
        assertThat(shiftValue, closeTo(0.3, 0.001));

    }

    @Test
    public void testShiftSyntheticSpectra() throws Exception {
        Spectra spectra = reader.loadSyntheticSpectra(lambdaFile, "02");
        Spectra copy  = spectra.copy();
        copy.shift(-0.3);
        double shiftValue = getShift(null, copy, spectra);
        assertThat(copy.getShift(), closeTo(-0.3, 0.001));
        assertThat(shiftValue, closeTo(0.3, 0.001));

    }

    @Test
    public void testShiftSyntheticSpectraSun() throws Exception {
        Spectra spectra = reader.loadSyntheticSpectra(sunFile, "02");
        Spectra copy  = spectra.copy();
        copy.shift(0.3);
        double shiftValue = getShift(null, copy, spectra);
        assertThat(shiftValue, closeTo(-0.3, 0.001));

    }

    @Test
    public void testShiftOriginalSyntheticForSun() throws Exception {
        Spectra synthSp = reader.loadSyntheticSpectra(sunSynthetic, "02").getSubSpectraByWave(6296.0800, 6304.9900);
        Spectra sun = reader.loadSyntheticSpectra(sunFile, "02").getSubSpectraByWave(6296.0800, 6304.9900);
        double shiftValue = getShift(null, sun,synthSp);
        assertThat(sun.getShift(), closeTo(0.0, 0.0001));
        assertThat(shiftValue, closeTo(-0.01, 0.01));

    }


    @Test
    public void testShiftOriginalSyntheticForLambdaAndromeda() throws Exception {
        Spectra synthSp = reader.loadSyntheticSpectra(lambdaSynthetic, "02").getSubSpectraByWave(6290.0800, 6310.0000);
        Spectra star = reader.loadSyntheticSpectra(lambdaFile, "02").getSubSpectraByWave(6290.0800, 6310.0000);
        double shiftValue = getShift(null, star,synthSp);
        assertThat(shiftValue, closeTo(-0.17, 0.001));

    }

    @Test
    public void testShiftOriginalSyntheticC2ForHD29317() throws Exception {
        Spectra synthSp = reader.loadSyntheticSpectra(camSynthetic, "C2").getSubSpectraByWave(5130.700,5140.700);
        Spectra star = reader.loadSyntheticSpectra(camFile, "C2").getSubSpectraByWave(5130.700,5140.700);
        double shiftValue = getShift(null, star,synthSp);
        assertThat(shiftValue, closeTo(0.13, 0.01));

    }

    @Test(expected = FileNotFoundException.class)
    public void testThrowsNotFOund() throws Exception {
        reader.loadSyntheticSpectra(Paths.get("/temp/testFile.xxy"), "C2").getSubSpectraByWave(5130.700, 5140.700);

    }
}