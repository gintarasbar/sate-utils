package edu.tfai.sate2.exceptions;

import edu.tfai.sate2.model.batch.BatchResults;

public class EstimationException extends AbstractSateException {
    private static final long serialVersionUID = -3099329256915349672L;

    private double abundance;

    public EstimationException(double abundance, BatchResults batchResults) {
        super(batchResults, true);
        this.abundance = abundance;
    }

    @Override
    public String getMessage() {
        return String.format("EstimErr[%s,%.3f, abund=%.2f]", batchResults.getIdenfitication(),
                batchResults.getWavelength(), abundance);
    }
}
