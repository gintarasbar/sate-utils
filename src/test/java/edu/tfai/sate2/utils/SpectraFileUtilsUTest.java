package edu.tfai.sate2.utils;

import edu.tfai.sate2.synthetic.file.SpectraFileUtils;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class SpectraFileUtilsUTest {


    private final Path file = FileNameUtils.getPath("o2_sun_synth.xxy");
    @Test
    public void testLastXValue() throws Exception {
        double value = SpectraFileUtils.getLastXValue(file);
        assertEquals(6306.9900, value, 0.0001);
    }

    @Test
    public void testFirstXValue() throws Exception {
        double value = SpectraFileUtils.getFirstXValue(file);
        assertEquals(6295.0000, value, 0.0001);
    }

    @Test
    public void testGetSpectraRange() throws Exception {
        double[] spectraRange = SpectraFileUtils.getSpectraRange(file);
        assertEquals(6295.0000, spectraRange[0], 0.0001);
        assertEquals(6306.9900, spectraRange[1], 0.0001);

    }


    @Test
    public void testCountLines() throws Exception {
        int lines = SpectraFileUtils.countLines(file);
        assertThat(lines, is(1201));

    }

    @Test
    public void testCountLinesRange() throws Exception {
        int lines = SpectraFileUtils.countLines(file, 6295.0100, 6295.1100);
        assertThat(lines, is(10));

    }

}