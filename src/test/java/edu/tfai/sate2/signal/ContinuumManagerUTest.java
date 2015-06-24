package edu.tfai.sate2.signal;

import edu.tfai.sate2.spectra.SpectraReader;

import java.nio.file.Path;

import static edu.tfai.sate2.utils.FileNameUtils.getPath;

public class ContinuumManagerUTest {
    private final Path sunSynthetic = getPath("o2_sun_synth.xxy");

    private final Path starSynthetic = getPath("o2_bsyn.txt");

    private final Path sunFile = getPath("sunO2.txt");

    private final Path starFile = getPath("HD222107_02.txt");

    private final Path camSynthetic = getPath("C2_syn_HD29317.txt");

    private final Path camFile = getPath("HD29317_C2.txt");

    private SpectraReader reader = new SpectraReader();

//    @Test
//    public void testSunContinuum() {
//        Spectra spectra = reader.loadSyntheticSpectra(sunFile, "02");
//        Spectra copy = spectra.copy();
//        copy.applyContinuumLevel(0.63);
//        Spectra newCopy = new Spectra(copy.getX(),copy.getY());
//        Spectra continuum = fixContinuum(newCopy, spectra);
//        assertThat(continuum.getContinuumLevel(),closeTo(1.589, 0.001));
//
//        assertThat(continuum.getContinuumLevel()*0.63,closeTo(1.0, 0.002));
//    }
//
//    @Test
//    public void testLambdaAndromedaContinuum() {
//        Spectra star = reader.loadSyntheticSpectra(starFile, "02");
//        Spectra bsyn = reader.loadSyntheticSpectra(starSynthetic, "02");
//        double shift = getShift(null,star, bsyn);
//        star.shift(shift);
//        Spectra continuum = fixContinuum(star, bsyn);
//        assertThat(continuum.getContinuumLevel(),closeTo(0.994, 0.001));
//    }
//
//
//    @Test
//    public void testCamC2Continuum() {
//        Spectra bsyn = reader.loadSyntheticSpectra(camSynthetic, "C2") ;
//        Spectra star = reader.loadSyntheticSpectra(camFile, "C2");
//        double shift = getShift(null,star, bsyn);
//        star.shift(shift);
//        Spectra continuum = fixContinuum(star, bsyn);
//        assertThat(continuum.getContinuumLevel(),closeTo(0.982, 0.001));
//
//
//    }

    //Maybe I can adjust smooth factor by iterating spline and calculating the residuals to the spectra smooth and original and calculating standar deviation

}