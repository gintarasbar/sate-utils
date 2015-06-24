package edu.tfai.sate2.exceptions;

public class NoiseException extends AbstractSateException {
    private static final long serialVersionUID = 2556910230066025655L;

    private double wave;

    private String identification;

    private double snRatio;

    public NoiseException(double wave, String lineIdentification, double snRatio) {
        super(null, true);
        this.snRatio = snRatio;
        this.wave = wave;
        this.identification = lineIdentification;
    }

    @Override
    public String getMessage() {
        return String.format("Noise[%s,%.3f, S/N=%.0f]", identification, wave, snRatio);
    }

}
