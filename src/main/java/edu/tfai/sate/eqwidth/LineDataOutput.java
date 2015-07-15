package edu.tfai.sate.eqwidth;

import edu.tfai.sate.swing.RowData;

/**
 * Line output data
 *
 * @author gintaras
 */
public class LineDataOutput implements RowData {
    private static final long serialVersionUID = -233043782468669239L;

    /**
     * LG W/L
     */
    private float log_w_l;

    /**
     * CHIE
     */
    private float chi_l;

    /**
     * GFLOG
     */
    private float log_gf;

    /**
     * ABUN
     */
    private float abund;

    /**
     * X/H
     */
    private float relAbund;

    /**
     * I_CHI
     */
    private float i_chi;

    /**
     * EQW
     */
    private float eqw;

    /**
     * DMP
     */
    private String dmp;

    /**
     * LMBDA
     */
    private float lambda;

    /**
     * EQW(THEOR)
     */
    private float eqw_theor;

    /**
     * ERROR
     */
    private float error;

    /*
         * (non-Javadoc)
         * 
         * @see edu.tfai.news.swing.RowData#getColumNames()
         */
    public String[] getColumNames() {
        String obj[] = new String[11];
        obj[0] = "LG W/L";
        obj[1] = "CHIE";
        obj[2] = "GFLOG";
        obj[3] = "ABUN";
        obj[4] = "X/H";
        obj[5] = "I_CHI";
        obj[6] = "EQW";
        obj[7] = "DMP";
        obj[8] = "LAMBDA";
        obj[9] = "EQW(THEOR)";
        obj[10] = "ERROR";
        return obj;
    }

    /*
         * (non-Javadoc)
         * 
         * @see edu.tfai.news.swing.RowData#getDataArray()
         */
    public Object[] getDataArray() {
        Object obj[] = new Object[11];
        obj[0] = log_w_l;
        obj[1] = chi_l;
        obj[2] = log_gf;
        obj[3] = abund;
        obj[4] = relAbund;
        obj[5] = i_chi;
        obj[6] = eqw;
        obj[7] = dmp;
        obj[8] = lambda;
        obj[9] = eqw_theor;
        obj[10] = error;
        return obj;
    }

    /**
     * Sets array data
     *
     * @param obj array data
     */
    public void setDataArray(String[] obj) throws Exception {
        try {
            log_w_l = Float.parseFloat(obj[0]);
        } catch (Exception e) {
            log_w_l = Float.NaN;
        }

        try {
            chi_l = Float.parseFloat(obj[1]);
        } catch (Exception e) {
            chi_l = Float.NaN;
        }

        try {
            log_gf = Float.parseFloat(obj[2]);
        } catch (Exception e) {
            log_gf = Float.NaN;
        }

        try {
            abund = Float.parseFloat(obj[3]);
        } catch (Exception e) {
            abund = Float.NaN;
        }

        try {
            relAbund = Float.parseFloat(obj[4]);
        } catch (Exception e) {
            relAbund = Float.NaN;
        }

        try {
            i_chi = Float.parseFloat(obj[5]);
        } catch (Exception e) {
            i_chi = Float.NaN;
        }

        try {
            eqw = Float.parseFloat(obj[6]);
        } catch (Exception e) {
            eqw = Float.NaN;
        }

        try {
            dmp = (String) (obj[7]);
        } catch (Exception e) {
            dmp = "X X";
        }

        try {
            lambda = Float.parseFloat(obj[8]);
        } catch (Exception e) {
            lambda = Float.NaN;
        }

        try {

            eqw_theor = Float.parseFloat(obj[9]);
        } catch (Exception e) {
            eqw_theor = Float.NaN;
        }

        try {
            error = Float.parseFloat(obj[10]);
        } catch (Exception e) {
            error = Float.NaN;
        }

    }

    /**
     * Disposes everything
     */
    public void dispose() {
    }

    /**
     * Gets abund value
     *
     * @return abund value
     */
    public float getAbund() {
        return abund;
    }

    /**
     * Sets abund value
     *
     * @param abund abund value
     */
    public void setAbund(float abund) {
        this.abund = abund;
    }

    /**
     * Gets chi_l value
     *
     * @return chi_l value
     */
    public float getChi_l() {
        return chi_l;
    }

    /**
     * Sets chi_l value
     *
     * @param chi_l chi_l value
     */
    public void setChi_l(float chi_l) {
        this.chi_l = chi_l;
    }

    /**
     * Gets dmp value
     *
     * @return dmp value
     */
    public String getDmp() {
        return dmp;
    }

    /**
     * Sets dmp value
     *
     * @param dmp dmp value
     */
    public void setDmp(String dmp) {
        this.dmp = dmp;
    }

    /**
     * Gets eqw value
     *
     * @return eqw value
     */
    public float getEqw() {
        return eqw;
    }

    /**
     * Sets eqw value
     *
     * @param eqw eqw value
     */
    public void setEqw(float eqw) {
        this.eqw = eqw;
    }

    /**
     * Gets eqw_theor value
     *
     * @return eqw_theor value
     */
    public float getEqw_theor() {
        return eqw_theor;
    }

    /**
     * Sets eqw_theor value
     *
     * @param eqw_theor eqw_theor value
     */
    public void setEqw_theor(float eqw_theor) {
        this.eqw_theor = eqw_theor;
    }

    /**
     * Gets error value
     *
     * @return error value
     */
    public float getError() {
        return error;
    }

    /**
     * Sets error value
     *
     * @param error error value
     */
    public void setError(float error) {
        this.error = error;
    }

    /**
     * Gets i_chi value
     *
     * @return i_chi value
     */
    public float getI_chi() {
        return i_chi;
    }

    /**
     * Sets i_chi value
     *
     * @param i_chi i_chi value
     */
    public void setI_chi(float i_chi) {
        this.i_chi = i_chi;
    }

    /**
     * Gets lambda value
     *
     * @return lambda value
     */
    public float getLambda() {
        return lambda;
    }

    /**
     * Sets lambda value
     *
     * @param lambda lambda value
     */
    public void setLambda(float lambda) {
        this.lambda = lambda;
    }

    /**
     * Gets log_gf value
     *
     * @return log_gf value
     */
    public float getLog_gf() {
        return log_gf;
    }

    /**
     * Sets log_gf value
     *
     * @param log_gf log_gf value
     */
    public void setLog_gf(float log_gf) {
        this.log_gf = log_gf;
    }

    /**
     * Gets log_w_l value
     *
     * @return log_w_l value
     */
    public float getLog_w_l() {
        return log_w_l;
    }

    /**
     * Sets log_w_l value
     *
     * @param log_w_l log_w_l value
     */
    public void setLog_w_l(float log_w_l) {
        this.log_w_l = log_w_l;
    }

    /**
     * Gets relAbund value
     *
     * @return relAbund value
     */
    public float getRelAbund() {
        return relAbund;
    }

    /**
     * Sets relAbund value
     *
     * @param relAbund relAbund value
     */
    public void setRelAbund(float relAbund) {
        this.relAbund = relAbund;
    }

    /*
     * (non-Javadoc)
     * @see RowData#setDataArray(java.lang.Object[])
     */
    public void setDataArray(Object[] data) {
        //does nothing
    }
}