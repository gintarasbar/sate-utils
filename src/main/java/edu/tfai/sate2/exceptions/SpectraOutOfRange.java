package edu.tfai.sate2.exceptions;

public class SpectraOutOfRange extends SpectraException {
    private static final long serialVersionUID = 2556910230066025655L;

    private double wave;

    private String identification;

    private double minimum;

    private double maximum;

    public SpectraOutOfRange(double wave, Number minimum, Number maximum, String lineId) {
        super(minimum, maximum, lineId);
        this.wave = wave;
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
        identification = lineId;
    }

    @Override
    public String getMessage() {
        return String.format("Spectra out of range[%.3f, %.3f] for %s, wave=%s", minimum, maximum, identification, wave);
    }

}
