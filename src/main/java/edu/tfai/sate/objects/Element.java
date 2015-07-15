package edu.tfai.sate.objects;

import edu.tfai.sate2.utils.NumberUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * One element lines with all the data
 *
 * @author gintaras
 */
public class Element implements Serializable, Cloneable {
    private static final long serialVersionUID = -6843330694519646713L;

    /**
     * Element number
     */
    @Getter
    @Setter
    private int elementNumber;

    @Getter
    private final String symbol;

    @Getter
    private final int ion;

    private Double meanAbundance = null;

    @Setter
    private Double medianAbundance = null;

    private Double stdDevAbund = null;

    private Double errorMean = null;

    private Double errorStDev = null;


    /**
     * Abundance
     */
    @Getter
    private Float abundance = null;

    /**
     * Element symbol
     */


    /**
     * Line data
     */
    private ArrayList<LineData> lines = new ArrayList<LineData>();

    private ArrayList<Float> wavelengthToRemove = new ArrayList<Float>();

    private Hashtable<String, LineData> lineHash = new Hashtable<String, LineData>();




    public String getIdentification() {
        return getSymbol() + "_" + getIonRomain();
    }

    /**
     * Gets ionRomain value
     *
     * @return ionRomain value
     */
    public String getIonRomain() {
        return NumberUtil.convertToRomain(ion);
    }

    public Element(String symbol, int ion, int elementNumber){
        this.elementNumber = elementNumber;
        this.symbol = symbol;
        this.ion = ion;
    }

//    public int getElementNumber() {
//        if (elementNumber == 0) {
//            try {
//                elementNumber = ElementDB.getNumber(symbol);
//            } catch (Exception e) {
//            }
//        }
//        return elementNumber;
//    }

    /**
     * Sets abundance value
     *
     * @param abundance abundance value
     */
    public void setAbundance(Float abundance) {
        this.abundance = abundance;
    }


    /**
     * Gets lines value
     *
     * @return lines value
     */
    public ArrayList<LineData> getLines() {
        for (LineData line : lines)
            line.setElementReference(this);
        return lines;
    }

    /**
     * Gets valid line count
     *
     * @return lines count
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * Adds line
     *
     * @param lineData line data
     */
    public void addLine(LineData lineData) {
        lineData.setElementReference(this);
        if (lineData.getWavelength() != null)
            lineHash.put(String.format("%.3f", lineData.getWavelength()), lineData);
        lines.add(lineData);
    }

    /**
     * Adds a list of line data. No dublicates can be involved
     *
     * @param lineData line data
     */
    public void addLines(ArrayList<LineData> lineData) {
        for (LineData line : lineData)
            lineHash.put(String.format("%.3f", line.getWavelength()), line);
        lines.addAll(lineData);
    }

    @Override
    public String toString() {
        return getSymbol();
    }

    /**
     * Sorts line list
     */
    public void sort() {
        Collections.sort(lines, new Comparator<LineData>() {
            public int compare(LineData a1, LineData a2) {
                if (a1.getWavelength() == null)
                    return -1;
                if (a2.getWavelength() == null)
                    return 1;
                if (a1.getWavelength() < a2.getWavelength())
                    return -1;
                else if (a1.getWavelength() > a2.getWavelength())
                    return 1;
                else
                    return 0;
            }
        });
    }

    /**
     * Disposes element
     */
    public void dispose() {
        meanAbundance = null;
        abundance = null;
        errorMean = null;
        errorStDev = null;
        stdDevAbund = null;
        wavelengthToRemove.clear();
        for (LineData line : lines) {
            line.dispose();
        }
        lines.clear();
        lineHash.clear();
    }

    public String getMeanAbundance() {
        if (meanAbundance == null)
            return "-";
        return NumberUtil.format(meanAbundance.floatValue(), 2);
    }

    public Double getMeanAbundanceNumber() {
        return meanAbundance;
    }

    public void setMeanAbundance(Double meanAbundance) {
        this.meanAbundance = meanAbundance;
    }

    public String getStdDevAbund() {
        if (stdDevAbund == null)
            return "-";
        return NumberUtil.format(stdDevAbund.floatValue(), 2);
    }

    public Double getStdDevAbundNumber() {
        return stdDevAbund;
    }

    public void setStdDevAbund(Double stdDev) {
        this.stdDevAbund = stdDev;
    }

    /**
     * Gets included line count
     *
     * @return line count
     */
    public int getIncludedLineCount() {
        int count = 0;
        for (LineData lines : getLines()) {
            if (lines.isIncluded())
                count++;
        }
        return count;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Element el = new Element(getSymbol(), getIon(), getElementNumber());
        el.setAbundance(abundance);
        el.setMeanAbundance(meanAbundance);
        el.setStdDevAbund(stdDevAbund);
        for (LineData line : lines) {
            el.addLine((LineData) line.clone());
            line.setElementReference(this);
        }
        return el;
    }

    public ArrayList<Float> getWavelengthsToRemove() {
        return wavelengthToRemove;
    }

    public void setWavelengthsToRemove(ArrayList<Float> wavelengthToRemove) {
        this.wavelengthToRemove = wavelengthToRemove;
    }

    public String getErrorMean() {
        if (errorMean == null)
            return "-";
        return NumberUtil.format(errorMean.floatValue(), 6);
    }

    public void setErrorMean(Double errorMean) {
        this.errorMean = errorMean;
    }

    public String getErrorStDev() {
        if (errorStDev == null)
            return "-";
        return NumberUtil.format(errorStDev.floatValue(), 6);
    }

    public void setErrorStDev(Double errorStDev) {
        this.errorStDev = errorStDev;
    }

    public List<Double> getChiLower() {
        List<Double> abund = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded())
                abund.add(line.getChi_l().doubleValue());
        return abund;
    }

    public List<Double> getLambdas() {
        List<Double> abund = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded())
                abund.add(line.getWavelength().doubleValue());
        return abund;
    }

    public List<Double> getEqws() {
        List<Double> abund = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded())
                abund.add(line.getEqwidth().doubleValue());
        return abund;
    }

    public List<Double> getAbundances() {
        List<Double> abund = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded())
                abund.add(line.getEvaluatedAbundance());
        return abund;
    }

    public LineData getLine(Float lineWave) {
        return lineHash.get(String.format("%.3f", lineWave));
    }
}
