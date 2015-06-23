package edu.tfai.sate2.exceptions;

import edu.tfai.sate.synthetic.batch.BatchResults;

public class AtmosphereException extends AbstractSateException {
    private static final long serialVersionUID = -3099329256915349672L;

    private float atmWave;

    private float chie_l;

    public AtmosphereException(BatchResults batchResults, float atmWave, float chie_l) {
        super(batchResults, false);
        this.atmWave = atmWave;
        this.chie_l = chie_l;
    }

    @Override
    public String getMessage() {
        return String.format("Atm[%s,%.3f,atm=%.3f, chie=%.3f]", batchResults.getIdenfitication(),
                batchResults.getWavelength(), atmWave, chie_l);
    }
}
