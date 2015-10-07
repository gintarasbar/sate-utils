package edu.tfai.sate2.exceptions;

import edu.tfai.sate2.model.batch.BatchResults;

public class BadProfileFitException extends AbstractSateException {
    private static final long serialVersionUID = -3099329256915349672L;

    private final double abundance;

    public BadProfileFitException(double abundance, BatchResults batchResults) {
        super(batchResults, true);
        this.abundance = abundance;
    }

    @Override
    public String getMessage() {
        return String.format("BadFit[%s,%.3f, abund=%.2f, err=%.6f]", batchResults.getIdenfitication(),
                batchResults.getWavelength(), abundance, batchResults.getChi2Error());
    }
}
