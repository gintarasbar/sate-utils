package edu.tfai.sate2.model.batch;

import edu.tfai.sate2.spectra.Spectra;
import lombok.Data;

import static edu.tfai.sate2.utils.RadialVelocityUtil.radialVelocityToShift;

@Data
public class BatchResults implements Cloneable {
    private Spectra spectra;

    private double abundance;

    private double shift;

    private double estimatedAbundance;

    private double chi2Error;

    private double continuumFix = 1.0;

    private int iterations;

    protected String lineRange;

    protected Double[] range = new Double[2];

    protected double lineWidth;

    protected Double lineBottom;

    protected Double difference;

    protected double radialVel;

    protected float wavelength;

    protected String idenfitication;

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
        this.radialVel = radialVelocityToShift(wavelength, shift);
    }

    public void setLineRange(double start, double end) {
        this.range[0] = start;
        this.range[1] = end;
        this.lineWidth = end - start;
        this.lineRange = String.format("[%.2f,%.2f]", start, end);
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