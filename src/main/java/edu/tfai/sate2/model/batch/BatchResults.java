package edu.tfai.sate2.model.batch;

import edu.tfai.sate2.spectra.Spectra;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import static edu.tfai.sate2.utils.RadialVelocityUtil.shiftToRadialVelocity;

@Data
public class BatchResults implements Cloneable {
    @Setter(AccessLevel.NONE)
    protected String lineRange;
    @Setter(AccessLevel.NONE)
    protected Double[] range = new Double[2];
    @Setter(AccessLevel.NONE)
    protected double lineWidth;
    protected Double lineBottom;
    protected Double difference;
    protected double derivedRadialVelocity;
    protected double realRadialVelocity;
    protected float wavelength;
    protected String idenfitication;
    private Spectra spectra;
    private double abundance;
    private double shift;
    private Double estimatedAbundance;
    private double chi2Error;
    private double continuumFix = 1.0;
    private int iterations;
    private int thread;

    private double SnRatio;

    private double eqw1;

    private double eqw2;

    public BatchResults(String identification, float wavelength) {
        this.wavelength = wavelength;
        this.idenfitication = identification;
    }

    public void setShift(double shift) {
        this.shift = shift;
        this.derivedRadialVelocity = shiftToRadialVelocity(wavelength, shift);
    }

    public void setLineRange(double start, double end) {
        this.range[0] = start;
        this.range[1] = end;
        this.lineWidth = end - start;
        this.lineRange = String.format("[%.2f,%.2f]", start, end);
    }

    public void setLineRange(Double[] values) {
        if (null != (values) && values.length > 1) {
            setLineRange(values[0], values[1]);
        }
    }


    protected Object clone() throws CloneNotSupportedException {
        BatchResults results = new BatchResults(idenfitication, wavelength);
        results.setAbundance(abundance);
        results.setChi2Error(chi2Error);
        results.setContinuumFix(continuumFix);
        results.setEstimatedAbundance(estimatedAbundance);
        results.setIterations(iterations);
        results.lineRange = lineRange;
        results.lineWidth = lineWidth;
        results.setShift(shift);
        results.setSpectra(spectra);
        results.setThread(thread);
        results.setSnRatio(SnRatio);
        return results;
    }
}
