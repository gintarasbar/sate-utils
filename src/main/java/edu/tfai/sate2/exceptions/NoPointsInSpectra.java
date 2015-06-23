package edu.tfai.sate2.exceptions;

public class NoPointsInSpectra extends AbstractSateException {
    private static final long serialVersionUID = 2556910230066025655L;

    private String file;

    public NoPointsInSpectra(String file) {
        super(null, true);
        this.file = file;
    }

    @Override
    public String getMessage() {
        return String.format("No data points found in spectra %s", file);
    }

}
