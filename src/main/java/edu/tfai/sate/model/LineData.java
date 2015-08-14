package edu.tfai.sate.model;

import edu.tfai.sate.swing.RowData;
import edu.tfai.sate2.utils.NumberUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Element one line data
 */

@Getter
@Setter
@ToString
public class LineData  implements RowData, Cloneable  {
    private static final long serialVersionUID = 978917136740347113L;

    /**
     * Wavelength
     */
    protected  Float wavelength;

    protected Element elementReference;
    /**
     * Error. Fixed value.
     */
    Float err = 1.0f;
    /**
     * Transition
     */
    String transition;
    /**
     * Full transition
     */
    String fullTransition;
    /**
     * Line deleted
     */
    String deleted;
    /**
     * Comment to the line
     */
    private String comment = "";
    /**
     * Shift of the line
     */
    private Double shift = null;
    /**
     * Flag showing if line was edited
     */
    private boolean edited = false;
    /**
     * Instrumental instrumentalProfile
     */
    private int instrumentalProfile = 80;
    /**
     * Instrumental instrumentalProfile
     */
    private Double evaluatedAbundance = null;
    /**
     * Is line included
     */
    private boolean included = true;
    /**
     * Lower level excitation potential
     */
    private Float chi_l;
    /**
     * Log gf
     */
    private Float log_gf;
    /**
     * vad der Waals constant
     */
    private Float FDAMP;
    /**
     * Gamrad
     */
    private Float gamrad;
    /**
     * Equivalent width
     */
    private Float eqwidth;
    /**
     * Retrieval error, due to wavelength shift
     */
    private Float gurtovenkoError = 0.0f;
    /**
     * Vald error
     */
    private Float valdError = 0.0f;
    /**
     * Use extra shift to center the line
     */
    private boolean useExtraShift = true;

    /**
     * use bottom fitting. eg. for O line
     */
    private boolean useBottomFitting = false;

    /**
     * Use continuum correction involving line instrumentalProfile
     */
    private boolean useContinuumCorrection = false;

    private boolean useSmooth = true;

    private boolean useForMicroturbulence = false;

    private double continuumCorrection = 1.0;

    private Double profileRange[] = new Double[2];

    private Float fwhm = -99f;

    private Double residual = 0.0d;

    /*
     * (non-Javadoc)
     * 
     * @see edu.tfai.news.swing.RowData#getArray()
     */
    public Object[] getDataArray() {
        Object[] s = new Object[15];
        s[0] = included;
        s[1] = wavelength;
        s[2] = chi_l;
        s[3] = log_gf;
        s[4] = FDAMP;
        s[5] = gamrad;
        s[6] = eqwidth;
        s[7] = err;
        s[8] = transition;
        s[9] = instrumentalProfile;
        s[10] = evaluatedAbundance;
        s[11] = comment;
        s[12] = edited;
        s[13] = shift;
        s[14] = deleted;
        return s;
    }

    /**
     * Sets data array
     *
     * @param data data to set
     */
    public void setDataArray(Object[] data) {
        included = (Boolean) data[0];
        wavelength = NumberUtil.tryParseFloat(data[1]);
        chi_l = NumberUtil.tryParseFloat(data[2]);
        log_gf = NumberUtil.tryParseFloat(data[3]);
        FDAMP = NumberUtil.tryParseFloat(data[4]);
        gamrad = NumberUtil.tryParseFloat(data[5]);
        eqwidth = NumberUtil.tryParseFloat(data[6]);
        err = NumberUtil.tryParseFloat(data[7]);
        transition = (String) data[8];
        instrumentalProfile = NumberUtil.parseInteger(data[9].toString());
        evaluatedAbundance = NumberUtil.tryParseDouble(data[10]);
        comment = (String) data[11];
        edited = (Boolean) data[12];
        shift = NumberUtil.tryParseDouble(data[13]);
        deleted = (String) (data[14] == null ? "N" : "Y");
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.tfai.news.swing.RowData#getColumNames()
     */
    public String[] getColumNames() {
        String[] s = new String[14];
        s[0] = "*";
        s[1] = "Wavelength (1)";
        s[2] = "Chi l (2)";
        s[3] = "log gf (3)";
        s[4] = "FDAMP (4)";
        s[5] = "gamrad (5)";
        s[6] = "eqwidth (6)";
        s[7] = "err (7)";
        s[8] = "trans (8)";
        s[9] = "instr.prof. (9)";
        s[10] = "[A/H] (10)";
        s[11] = "comment (11)";
        s[12] = "locked (12)";
        s[13] = "shift (13)";
        return s;
    }

    /**
     * Disposes line data
     */
    public void dispose() {
        wavelength = null;
        chi_l = null;
        log_gf = null;
        FDAMP = null;
        gamrad = null;
        eqwidth = null;
        err = null;
        transition = null;

        comment = null;
        deleted = null;
        elementReference = null;
        evaluatedAbundance = null;
        fullTransition = null;
        gurtovenkoError = null;
        shift = null;
        valdError = null;
    }

    @Override
    public boolean equals(Object arg0) {
        LineData lineData = (LineData) arg0;
        return Math.abs(this.wavelength - lineData.getWavelength()) < 0.01;
    }

    public boolean isLocked() {
        return edited;
    }

    public void setLocked(boolean edited) {
        this.edited = edited;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        LineData line = new LineData();
        line.setWavelength(getWavelength());
        line.setLog_gf(getLog_gf());
        line.setChi_l(getChi_l());
        line.setComment(getComment());
        line.setLocked(isLocked());
        line.setEqwidth(getEqwidth());
        line.setErr(getErr());
        line.setEvaluatedAbundance(getEvaluatedAbundance());
        line.setFDAMP(getFDAMP());
        line.setFullTransition(getFullTransition());
        line.setTransition(getTransition());
        line.setGamrad(getGamrad());
        line.setGurtovenkoError(getGurtovenkoError());
        line.setValdError(getValdError());
        line.setIncluded(isIncluded());
        line.setInstrumentalProfile(getInstrumentalProfile());
        line.setShift(getShift());
        line.setUseExtraShift(useExtraShift);
        line.setElementReference(elementReference);
        line.setUseBottomFitting(useBottomFitting);
        line.setUseContinuumCorrection(useContinuumCorrection);
        line.setUseSmooth(useSmooth);
        line.setContinuumCorrection(continuumCorrection);
        line.setUseForMicroturbulence(useForMicroturbulence);
        line.setResidual(residual);
        line.setFwhm(fwhm);
        if (profileRange == null)
            profileRange = new Double[2];
        line.setProfileRange(profileRange[0], profileRange[1]);
        return line;
    }

    public void setProfileRange(Double start, Double end) {
        this.profileRange[0] = start;
        this.profileRange[1] = end;
    }


}
