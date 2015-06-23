package edu.tfai.sate2.exceptions;

public class ProfileException extends AbstractSateException {
    private static final long serialVersionUID = 2556910230066025655L;

    private double wave;


    private double minimum;

    private double maximum;

    public ProfileException(double wave, Number minimum, Number maximum) {
        super(null, true);
        this.wave = wave;
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
//	identification = line != null && line.getElementReference() != null ? line.getElementReference()
//		.getIdentification() : "null";
    }

    @Override
    public String getMessage() {
        return String.format("Profile[%.3f] of [%.3f, %.3f]", wave, minimum, maximum);
    }

}
