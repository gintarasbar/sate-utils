package edu.tfai.sate.eqwidth;

import edu.tfai.sate.swing.RowData;
import edu.tfai.sate2.utils.NumberUtil;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;

/**
 * Output element data
 *
 * @author gintaras
 */
public class ElementOutput implements RowData {
    private static final long serialVersionUID = -4438019417574441581L;

    /**
     * Line data array
     */
    private ArrayList<LineDataOutput> lines = new ArrayList<LineDataOutput>();

    /**
     * Element symbol
     */
    private String symbol;

    /**
     * Element number
     */
    private int elNumber;

    /**
     * Element ion
     */
    private int ion;

    /**
     * Mean abundance
     */
    private float meanAbund;

    /**
     * Mean relative abundance [X/H]
     */
    private float meanRelatAbund;

    /**
     * Standard deviation
     */
    private float stDev;

    /**
     * Standard deviation mean
     */
    private float stDevMean;

    /**
     * line count
     */
    private int lineCount;

    /*
     * (non-Javadoc)
     * 
     * @see edu.tfai.news.swing.RowData#getColumNames()
     */
    public String[] getColumNames() {
        String[] names = new String[8];
        names[0] = "El.";
        names[1] = "No.";
        names[2] = "Ion";
        names[3] = "Mean abund.";
        names[4] = "Line count";
        names[5] = "Mean [X/H]";
        names[6] = "St. dev.";
        names[7] = "St. dev. mean.";
        return names;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.tfai.news.swing.RowData#getDataArray()
     */
    public Object[] getDataArray() {
        Object[] obj = new Object[8];
        obj[0] = symbol;
        obj[1] = elNumber;
        obj[2] = ion;
        obj[3] = meanAbund;
        obj[4] = getLineCount();
        obj[5] = meanRelatAbund;
        obj[6] = stDev;
        obj[7] = stDevMean;
        return obj;
    }

    /**
     * Gets elNumber value
     *
     * @return elNumber value]
     */
    public int getElementNumber() {
        return elNumber;
    }

    /**
     * Sets elNumber value
     *
     * @param elNumber elNumber value
     */
    public void setElementNumber(int elNumber) {
        this.elNumber = elNumber;
    }

    /**
     * Gets ion value
     *
     * @return ion value
     */
    public int getIon() {
        return ion;
    }

    /**
     * Sets ion value
     *
     * @param ion ion value
     */
    public void setIon(int ion) {
        this.ion = ion;
    }

    /**
     * Gets meanAbund value
     *
     * @return meanAbund value
     */
    public float getMeanAbund() {
        return meanAbund;
    }

    /**
     * Sets meanAbund value
     *
     * @param meanAbund meanAbund value
     */
    public void setMeanAbund(float meanAbund) {
        this.meanAbund = meanAbund;
    }

    /**
     * Gets meanRelatAbund value
     *
     * @return meanRelatAbund value
     */
    public float getMeanRelatAbund() {
        return meanRelatAbund;
    }

    /**
     * Sets meanRelatAbund value
     *
     * @param meanRelatAbund meanRelatAbund value
     */
    public void setMeanRelatAbund(float meanRelatAbund) {
        this.meanRelatAbund = meanRelatAbund;
    }

    /**
     * Gets stDev value
     *
     * @return stDev value
     */
    public float getStDev() {
        return stDev;
    }

    /**
     * Calculates st deviation
     *
     * @return st.dev.
     */
    public double calculateStDev() {
        SummaryStatistics list = new SummaryStatistics();
        for (LineDataOutput line : lines) {
            list.addValue((double) line.getRelAbund());
        }

        double values = list.getStandardDeviation();
        return values;
    }

    /**
     * Sets stDev value
     *
     * @param stDev stDev value
     */
    public void setStDev(float stDev) {
        this.stDev = stDev;
    }

    /**
     * Gets symbol value
     *
     * @return symbol value
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets symbol value
     *
     * @param symbol symbol value
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets stDevMean value
     *
     * @return stDevMean value
     */
    public float getStDevMean() {
        return stDevMean;
    }

    /**
     * Sets stDevMean value
     *
     * @param stDevMean stDevMean value
     */
    public void setStDevMean(float stDevMean) {
        this.stDevMean = stDevMean;
    }

    /**
     * Adds output line data
     *
     * @param line line to add
     */
    public void addLine(LineDataOutput line) {
        lines.add(line);
    }

    /**
     * Adds all lines
     *
     * @param lines
     */
    public void addLines(ArrayList<LineDataOutput> lines) {
        this.lines.addAll(lines);
        lineCount = lines.size();
    }

    /**
     * Disposes everything
     */
    public void dispose() {
        for (LineDataOutput line : lines) {
            line.dispose();
        }
        lines.clear();
    }

    /**
     * Gets lines value
     *
     * @return lines value
     */
    public ArrayList<LineDataOutput> getLines() {
        return lines;
    }

    /**
     * Gets lineCount value
     *
     * @return lineCount value
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * Sets read line count
     *
     * @param lineCount
     *            line count
     */
//    public void setLineCount(int lineCount)
//    {
//	this.lineCount = lineCount;
//    }

    /**
     * Element identification
     *
     * @return symbol + roman ion
     */
    public String getIdentification() {
        String ion = NumberUtil.convertToRomain(getIon());
        return getSymbol().toLowerCase() + "_" + ion;
    }

    /**
     * Gets romain represnatation of ion
     *
     * @return ion in Romain
     */
    public String getIonRomain() {
        return NumberUtil.convertToRomain(getIon());
    }

    public void setDataArray(Object[] data) {
        // does nothing
    }
}