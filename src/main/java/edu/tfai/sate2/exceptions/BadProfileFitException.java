package edu.tfai.sate2.exceptions;

import edu.tfai.sate2.model.batch.BatchResults;

public class BadProfileFitException extends AbstractSateException {
    private static final long serialVersionUID = -3099329256915349672L;

    private final double abundance;
    private final double error;

    public BadProfileFitException(double abundance, double error, BatchResults batchResults) {
        super(batchResults, true);
        this.abundance = abundance;
        this.error = error;
    }

    @Override
    public String getMessage() {
        return String.format("BadFit[%s,%.3f, abund=%.2f, err=%.6f]", batchResults.getIdenfitication(),
                batchResults.getWavelength(), abundance, error);
    }
}
