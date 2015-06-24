package edu.tfai.sate.swing;

import java.io.Serializable;

/**
 * Gets row data
 *
 * @author gintaras
 */
public interface RowData extends Serializable {
    /**
     * Gets array data
     *
     * @return array data
     */
    public Object[] getDataArray();

    /**
     * Sets data array for the object
     *
     * @param data data array object
     */
    public void setDataArray(Object[] data);

    /**
     * Column names
     *
     * @return column name array
     */
    public String[] getColumNames();
}
