package edu.tfai.sate2.exceptions;


import edu.tfai.sate2.model.batch.BatchResults;

public class NanInTheLine extends AbstractSateException {
    private static final long serialVersionUID = 646733472954282340L;

    public NanInTheLine(String id, double wave) {
        super(new BatchResults(id, (float) wave), true);
    }

    @Override
    public String getMessage() {
        return String.format("NanInLine[%s,%.3f]", batchResults.getIdenfitication(), batchResults.getWavelength());
    }
}
