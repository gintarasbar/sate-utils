package edu.tfai.sate2.spectra;

import edu.tfai.sate2.utils.FileTestUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static edu.tfai.sate2.utils.FileTestUtil.getPath;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

@RunWith(MockitoJUnitRunner.class)
public class SpectraUTest {



    private final Path file = FileTestUtil.getPath("o2_sun_synth.xxy");
    private final Path sunFile = FileTestUtil.getPath("sunO2.txt");
    private final Path starFile = FileTestUtil.getPath("HD222107_02.txt");

    @InjectMocks
    SpectraReader spectraReader;

    private Spectra spectra;

    @Before
    public void setUp() {
        spectra = new Spectra(new double[]{1, 2, 3, 4, 5}, new double[]{1, 0, 0.9, 1.2, 1.1});
    }

    @Test
    public void testGetSubSpectra() throws Exception {
        Spectra subSpectra = spectra.getSubSpectra(1, 3);
        assertThat(subSpectra.getX(0),closeTo(2.0d, 0.01));
        assertThat(subSpectra.getX(1),closeTo(3.0d, 0.01));
        assertThat(subSpectra.getX(2),closeTo(4.0d, 0.01));
        assertThat(subSpectra.getY(0),closeTo(0.0d, 0.01));
        assertThat(subSpectra.getY(1),closeTo(0.9d, 0.01));
        assertThat(subSpectra.getY(2),closeTo(1.2d, 0.01));
    }


    @Test
    public void testNormalize() throws Exception {
        spectra.applyContinuumLevel(2.0);
        assertThat(spectra.getY(0),closeTo(2.0d, 0.01));
        assertThat(spectra.getY(1),closeTo(0.0d, 0.01));
        assertThat(spectra.getY(2),closeTo(1.8d, 0.01));
        assertThat(spectra.getY(3),closeTo(2.4d, 0.01));
        assertThat(spectra.getY(4),closeTo(2.2d, 0.01));
        spectra.applyContinuumLevel(0);
        assertThat(spectra.getY(0),closeTo(0.0d, 0.01));
    }

    @Test
    public void testShift() throws Exception {
        spectra.shift(2.2);
        assertThat(spectra.getX(0),closeTo(3.20d, 0.01));
        assertThat(spectra.getX(1),closeTo(4.20d, 0.01));
        assertThat(spectra.getX(2),closeTo(5.20d, 0.01));
        assertThat(spectra.getX(3),closeTo(6.20d, 0.01));
        assertThat(spectra.getX(4),closeTo(7.20d, 0.01));
    }

    @Test
    public void testSize() throws Exception {
        int size = spectra.size();
        assertThat(size, is(5));
    }

    @Test
    public void testGetMinX() throws Exception {
        double value = spectra.getMinX();
        assertThat(value,closeTo(1, 0.1));
    }

    @Test
    public void testGetMaxX() throws Exception {
        double value = spectra.getMaxX();
        assertThat(value,closeTo(5, 0.1));
    }

    @Test
    public void testGetSubspectraWave() throws Exception {
        Spectra subSpectra = spectra.getSubSpectraByWave(1.7, 4.4);
        assertThat(subSpectra.size(), is(3));
        assertThat(subSpectra.getMinX(),closeTo(2.00d, 0.01));
        assertThat(subSpectra.getMaxX(),closeTo(4.00d, 0.01));
    }


    @Test
    public void testFindPosition() throws Exception {
        int pos = spectra.findPosition(1.7);
        assertThat(pos, is(1));

        pos = spectra.findPosition(2);
        assertThat(pos, is(1));

        pos = spectra.findPosition(3.1);
        assertThat(pos, is(2));

        pos = spectra.findPosition(5);
        assertThat(pos, is(4));
    }

    @Test
    public void testGetResolution() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(file, "O2");
        assertThat(spectra.getResolution(), is(629500));

    }

    @Test
    public void testGetResolutionSun() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(sunFile, "O2");
        assertThat(spectra.getResolution(), is(1284694));

    }

    @Test
    public void testGetResolutionStar() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(starFile, "O2");
        assertThat(spectra.getResolution(), is(157922));

    }

    @Test
    public void testIsInRange() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(starFile, "O2");
        assertThat(spectra.isInRange(6300), is(true));
        assertThat(spectra.isInRange(6315), is(false));
        assertThat(spectra.isInRange(6100), is(false));

    }

    @Test
    public void testStatistics() throws Exception {
        assertThat(spectra.getYStats().getMin(),closeTo(0, 0.01));
        assertThat(spectra.getYStats().getMax(),closeTo(1.2, 0.01));
        assertThat(spectra.getYStats().getMean(),closeTo(0.84, 0.01));

    }
}