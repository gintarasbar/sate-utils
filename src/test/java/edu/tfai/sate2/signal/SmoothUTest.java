package edu.tfai.sate2.signal;

import edu.tfai.sate2.spectra.Spectra;
import edu.tfai.sate2.spectra.SpectraReader;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static edu.tfai.sate2.signal.Smooth.*;
import static edu.tfai.sate2.utils.FileNameUtils.getPath;
import static edu.tfai.sate2.utils.NoiseUtil.addNoise;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SmoothUTest {
    private final Path file = getPath("HD222107_02.xxy");


    SpectraReader spectraReader = new SpectraReader();

    private Spectra spectra;

    @Before
    public void setUp() {
        spectra = new Spectra(new double[]{6000.01, 6000.02, 6000.03, 6000.04, 6000.05, 6000.06}
                , new double[]{1, 0, 0.75, 1.5, 1.01, 1.0});
    }

    @Test
    public void testSmooth() {
        Spectra smooth = getSmooth(spectra, 20, 1);
        assertThat(smooth.size(), is(5));
        assertThat(smooth.getY(0), closeTo(1, 0.01));
        assertThat(smooth.getY(1), closeTo(0.40, 0.01));
        assertThat(smooth.getY(2), closeTo(0.70, 0.01));
        assertThat(smooth.getY(3), closeTo(1.12, 0.01));
        assertThat(smooth.getY(4), closeTo(1.08, 0.01));

        smooth = getSmooth(spectra, 1, 1);
        assertThat(smooth.size(), is(5));
        assertThat(smooth.getY(0), closeTo(1, 0.01));
        assertThat(smooth.getY(1), closeTo(0, 0.01));
        assertThat(smooth.getY(2), closeTo(0.75, 0.01));
        assertThat(smooth.getY(3), closeTo(1.5, 0.01));
        assertThat(smooth.getY(4), closeTo(1.00, 0.01));

    }

    @Test
    public void testSplineSmooth() {
        Spectra smooth = getSplineSmooth(spectra);
        assertThat(smooth.size(), is(6));
        assertThat(smooth.getY(0), closeTo(1, 0.01));
        assertThat(smooth.getY(1), closeTo(0.0, 0.01));
        assertThat(smooth.getY(2), closeTo(0.75, 0.01));
        assertThat(smooth.getY(3), closeTo(1.5, 0.01));
        assertThat(smooth.getY(4), closeTo(1.01, 0.01));
        assertThat(smooth.getY(5), closeTo(1.0, 0.01));

    }

    @Test
    public void testSplineSmoothFunction() {
        PolynomialSplineFunction splineSmoothFunction = getSplineSmoothFunction(spectra);
        assertThat(splineSmoothFunction.value(6000.015), closeTo(0.33, 0.01));
        assertThat(splineSmoothFunction.value(6000.025), closeTo(0.21, 0.01));
        assertThat(splineSmoothFunction.value(6000.037), closeTo(1.40, 0.01));

    }

    @Test
    public void testSplineSmoothFromFile() throws Exception {
        Spectra o2 = spectraReader.loadSyntheticSpectra(file, "O2");
        Spectra smoothedSp =getSmooth(o2, 20, 3);
        assertThat(smoothedSp.size(), is(o2.size() * 3-3));

        Spectra normalSmooth = getSplineSmooth(o2);
        assertThat(normalSmooth.size(), is(o2.size()));

        Spectra o2Noise = addNoise(o2, 0.05);
        Spectra o2NoiseSmoothed = getSmooth(o2, 20, 1);
        assertThat(o2NoiseSmoothed.getMaxX(),not(closeTo(0,0.1)));
//        spectraReader.writeSpectra(Paths.get("./testSp1.txt"), o2);
//        spectraReader.writeSpectra(Paths.get("./testSp2.txt"), smoothedSp);
//
//        spectraReader.writeSpectra(Paths.get("./testSp3.txt"), normalSmooth);
//        spectraReader.writeSpectra(Paths.get("./testSp4.txt"), o2Noise);
//
//        spectraReader.writeSpectra(Paths.get("./testSp5.txt"), o2NoiseSmoothed);
    }
}