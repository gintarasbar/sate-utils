package edu.tfai.sate.model;

import edu.tfai.sate2.utils.NumberUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

import static java.lang.String.format;

@Slf4j
public class Element implements Serializable, Cloneable {
    private static final long serialVersionUID = -6843330694519646713L;
    @Getter
    private final String symbol;
    @Getter
    private final int ion;
    /**
     * Element number
     */
    @Getter
    @Setter
    private int elementNumber;

    @Getter
    @Setter
    private Double meanAbundance = null;

    @Setter
    @Getter
    private Double medianAbundance = null;

    @Getter
    @Setter
    private Double stdDevAbund = null;

    @Getter
    @Setter
    private Double errorMean = null;

    @Getter
    @Setter
    private Double errorStDev = null;


    /**
     * Abundance
     */
    @Getter
    @Setter
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


    public Element(String symbol, int ion, int elementNumber) {
        this.elementNumber = elementNumber;
        this.symbol = symbol;
        this.ion = ion;
    }

    public static String getIdentification(LineData line) {
        return line != null && line.getElementReference() != null ? line.getElementReference()
                .getIdentification() : "null";
    }

    public static String getFullIdentification(LineData line) {
        return format("%s %.3f", getIdentification(line), line.getWavelength());
    }

    public String getIdentification() {
        return getSymbol() + "_" + getIonRomain();
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
     * Gets ionRomain value
     *
     * @return ionRomain value
     */
    public String getIonRomain() {
        return NumberUtil.convertToRomain(ion);
    }


    /**
     * Gets lines value
     *
     * @return lines value
     */
    public ArrayList<LineData> getLines() {
        //extra measurement if reference is null
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
        if (lineData.getWavelength() != null) {
            lineHash.put(format("%.3f", lineData.getWavelength()), lineData);
        }
        lines.add(lineData);
    }

    /**
     * Adds a list of line data. No dublicates can be involved
     *
     * @param lineData line data
     */
    public void addLines(ArrayList<LineData> lineData) {
        for (LineData line : lineData)
            lineHash.put(format("%.3f", line.getWavelength()), line);
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
            //line.setElementReference(this);
        }
        return el;
    }

    public ArrayList<Float> getWavelengthsToRemove() {
        return wavelengthToRemove;
    }

    public void setWavelengthsToRemove(ArrayList<Float> wavelengthToRemove) {
        this.wavelengthToRemove = wavelengthToRemove;
    }

    public List<Double> getChiLower() {
        List<Double> chil = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded()) {
                if (line.getChi_l() == null) {
                    log.warn("Chi_l null for line {} {}", getIdentification(), line.getWavelength());
                }
                chil.add(line.getChi_l().doubleValue());
            }
        return chil;
    }

    public List<Double> getLambdas() {
        List<Double> wave = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded()) {
                if (line.getWavelength() == null) {
                    log.warn("Wavelength null for line {}", getIdentification());
                }
                wave.add(line.getWavelength().doubleValue());
            }
        return wave;
    }

    public List<Double> getEqws() {
        List<Double> eqw = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded()) {
//                if (line.getEqwidth() == null) {
//                    log.warn("Eqw null for line {} {}", getIdentification(), line.getWavelength());
//                }
                eqw.add(line.getEqwidth().doubleValue());
            }
        return eqw;
    }

    public List<Double> getAbundances() {
        List<Double> abund = new ArrayList<Double>();
        for (LineData line : lines)
            if (line.isIncluded()) {
//                if (line.getEvaluatedAbundance() == null) {
//                    log.warn("Abundance null for line {} {}", getIdentification(), line.getWavelength());
//                }
                abund.add(line.getEvaluatedAbundance());
            }
        return abund;
    }

    public LineData getLine(Float lineWave) {
        return lineHash.get(format("%.3f", lineWave));
    }
}
