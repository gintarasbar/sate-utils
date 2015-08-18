package edu.tfai.sate2.exceptions;

public class SpectraException extends AbstractSateException {
    private static final long serialVersionUID = 2556910230066025655L;

    private String identification;

    private double minimum;

    private double maximum;

    public SpectraException(Number minimum, Number maximum, String lineId) {
        super(null, true);
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
        identification = lineId;
    }

    @Override
    public String getMessage() {
        return String.format("Spectra loading error for line %s of [%.3f, %.3f]", identification, minimum, maximum);
    }

}
