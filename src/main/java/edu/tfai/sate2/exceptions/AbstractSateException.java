package edu.tfai.sate2.exceptions;

import edu.tfai.sate2.model.batch.BatchResults;

public class AbstractSateException extends RuntimeException {
    private static final long serialVersionUID = 204477106545641975L;

    protected BatchResults batchResults;

    protected boolean outputMuted;

    public AbstractSateException(BatchResults batchResults, boolean outputMuted) {
        this.batchResults = batchResults;
        this.outputMuted = outputMuted;
    }

    public boolean isOutputMuted() {
        return outputMuted;
    }

    public BatchResults getBatchResults() {
        return batchResults;
    }

}
