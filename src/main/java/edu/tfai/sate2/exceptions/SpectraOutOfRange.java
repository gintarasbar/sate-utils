package edu.tfai.sate2.exceptions;

public class SpectraOutOfRange extends AbstractSateException {
    private static final long serialVersionUID = 2556910230066025655L;

    private double wave;

    private String identification;

    private double minimum;

    private double maximum;

    public SpectraOutOfRange(double wave, Number minimum, Number maximum, String lineId) {
        super(null, true);
        this.wave = wave;
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
        identification = lineId;
    }

    @Override
    public String getMessage() {
        return String.format("Range[%s,%.3f] of [%.3f, %.3f]", identification, wave, minimum, maximum);
    }

}
