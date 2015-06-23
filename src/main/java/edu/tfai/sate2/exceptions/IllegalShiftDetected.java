package edu.tfai.sate2.exceptions;

import edu.tfai.sate.synthetic.batch.BatchResults;

import static edu.tfai.sate2.utils.RadialVelocityUtil.radialVelocityToShift;

public class IllegalShiftDetected extends AbstractSateException {
    private static final long serialVersionUID = 646733472954282340L;

    private double radial;

    private double meanRadial;

    public IllegalShiftDetected(double wave, double shift, double meanVel, BatchResults batchResults) {
        super(batchResults, true);
        this.radial = radialVelocityToShift(wave, shift);
        this.meanRadial = meanVel;
    }

    public double getShift() {
        return radial;
    }

    @Override
    public String getMessage() {
        return String.format("ShiftRad[%s,%.3f,rad=%.3f, mean=%.3f ]", batchResults.getIdenfitication(),
                batchResults.getWavelength(), radial, meanRadial);
    }
}
