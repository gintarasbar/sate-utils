package edu.tfai.sate2.spectra;

import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FWMHCalculatorUTest {

    private final Path file = Paths.get(Resources.getResource("o2_sun_synth.xxy").getFile());

    @InjectMocks
    FWMHCalculator fwmhCalculator;

    @InjectMocks
    SpectraReader spectraReader;

    @Test
    public void testO2Line() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(file, "O2", 6300.277, 6300.452);
//        Spectra spectra = spectraReader.loadSpectra(new File(file.toURI()).toString(), "O2",6300.10, 6300.50, 0, false, 0, 1);
        int pos = spectra.findPosition(6300.317);
        double x = spectra.getX(pos);
        double y = spectra.getY(pos);
        double fwmh = fwmhCalculator.getFWMH(spectra,x,y,"O2");
        assertThat(fwmh,closeTo(4.3,1.5));

    }

    @Test
    public void testScIILine() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(file, "Sc2", 6300.577, 6300.777);
        int pos = spectra.findPosition(6300.691);
        double x = spectra.getX(pos);
        double y = spectra.getY(pos);
        double fwmh = fwmhCalculator.getFWMH(spectra,x,y,"Sc2");
        assertThat(fwmh,closeTo(5.6,0.6));

    }


    @Test
    public void testFeILine() throws Exception {
        Spectra spectra = spectraReader.loadSyntheticSpectra(file, "Fe1", 6301.220, 6301.722);
        int pos = spectra.findPosition(6301.507);
        double x = spectra.getX(pos);
        double y = spectra.getY(pos);
        double fwmh = fwmhCalculator.getFWMH(spectra,x,y,"Fe1");
        assertThat(fwmh,closeTo(136.765,10));

        spectra = spectraReader.loadSyntheticSpectra(file, "Fe1", 6302.307, 6302.660);
        pos = spectra.findPosition(6302.502);
        x = spectra.getX(pos);
        y = spectra.getY(pos);
        fwmh = fwmhCalculator.getFWMH(spectra,x,y,"Fe1");
        assertThat(fwmh,closeTo(90.819,6.5));

    }



}
