package edu.tfai.sate2.exceptions;

import edu.tfai.sate.synthetic.batch.BatchResults;

public class IllegalAbundance extends AbstractSateException {
    private static final long serialVersionUID = -3099329256915349672L;

    private double abundance;

    public IllegalAbundance(double abundance, BatchResults batchResults) {
        super(batchResults, true);
        this.abundance = abundance;
    }

    @Override
    public String getMessage() {
        return String.format("AbundErr[%s,%.3f,%.2f]", batchResults.getIdenfitication(),
                batchResults.getWavelength(), abundance);
    }
}
