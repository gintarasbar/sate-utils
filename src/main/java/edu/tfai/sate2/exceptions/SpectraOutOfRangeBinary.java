package edu.tfai.sate2.exceptions;

public class SpectraOutOfRangeBinary extends SpectraException {
    private static final long serialVersionUID = 2556910230066025655L;

    private double wave;


    private double minimum;

    private double maximum;

    public SpectraOutOfRangeBinary(double wave, Number minimum, Number maximum) {
        super(minimum, maximum, null);
        this.wave = wave;
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
    }

    @Override
    public String getMessage() {
        return String.format("Binary search range for %.3f [%.3f, %.3f]", wave, minimum, maximum);
    }

}
