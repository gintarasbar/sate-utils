package edu.tfai.sate2.spectra;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.LinkOption;
import java.nio.file.Path;

import static edu.tfai.sate2.utils.FileNameUtils.getPath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SpectraReaderUTest {

    private final Path file = getPath("o2_sun_synth.xxy");

    private final Path spFile = getPath("spectra.txt");

    private final Path synthSpFile = getPath("spectra.xxy");

    SpectraReader spectraReader;

    @Before
    public void setUp() {
        spectraReader = new SpectraReader();
    }


    @After
    public void after() {
        spectraReader.dispose();
        spectraReader = null;
    }


    @Test
    public void testReadSpectraRange() throws Exception {
        Spectra spectra = spectraReader.loadSpectraFileRange(file, 6295.0800, 6295.1700);
        assertThat(spectra.size(), is(10));
        assertThat(spectra.getX(0), closeTo(6295.0800, 0.001));
        assertThat(spectra.getY(0), closeTo(1.100, 0.001));
        assertThat(spectra.getX(9), closeTo(6295.1700, 0.001));
        assertThat(spectra.getY(9), closeTo(1.200, 0.001));
        assertThat(spectra.isCached(), is(false));
        assertThat(spectra.getSpectraName(),is(not(file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString())));
        assertThat(spectra.getSpectraName().length(),is(255));
    }

    @Test
    public void testReadIRAFSpectra() throws Exception {
        Spectra spectra = spectraReader.loadSpectra(spFile, "All", 3636.569, 3637.00);
        assertThat(spectra.size(), is(19));
        assertThat(spectra.getInstrument(), is("FIES"));
        assertThat(spectra.isCached(), is(false));
        assertThat(spectra.getSpectraName(),is(spFile.toRealPath(LinkOption.NOFOLLOW_LINKS).toString()));
        spectra =  spectraReader.loadSpectra(spFile, "All", 3636.569, 3637.00);
        assertThat(spectra.isCached(), is(true));
        assertThat(spectra.getSpectraName(),is(spFile.toRealPath(LinkOption.NOFOLLOW_LINKS).toString()));
    }


    @Test
    public void testReadSpectraRangeWithWavelengths() throws Exception {
        spectraReader = new SpectraReader();
        Spectra spectra = spectraReader.loadSyntheticSpectra(file, "TestLine", 6295.0800, 6298.1700);
        assertThat(spectra.size(), is(310));
        spectra.shift(0.5);
        spectra.applyContinuumLevel(2);
        assertThat(spectra.getX(0), closeTo(6295.5800, 0.001));
        assertThat(spectra.getY(0), closeTo(2.200, 0.001));
        assertThat(spectra.isCached(), is(false));
        spectra = spectraReader.loadSyntheticSpectra(file, "TestLine", 6295.0800, 6298.1700);
        assertThat(spectra.isCached(), is(false));
    }

    @Test
    public void testReadAllSpectra() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(file, "TestLine");
        assertThat(spectra.size(), is(1199));
        assertThat(spectra.isCached(), is(false));
    }

    @Test
    public void testSyntheticSpReadAll() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(synthSpFile, "TestLine");
        assertThat(spectra.size(), is(78));
        assertThat(spectra.isCached(), is(false));
        spectra = spectraReader.loadSyntheticSpectra(synthSpFile, "TestLine");
        assertThat(spectra.isCached(), is(false));
    }

    @Test
    public void testSyntheticSpReadRange() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(synthSpFile, "TestLine", 3636.084, 3636.708);
        assertThat(spectra.size(), is(27));
        assertThat(spectra.isCached(), is(false));
        spectra = spectraReader.loadSyntheticSpectra(synthSpFile, "TestLine");
        assertThat(spectra.isCached(), is(false));
    }
}