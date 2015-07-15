package edu.tfai.sate.swing;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Lentels modelio klas
 */
public class MyTableModel extends AbstractTableModel {

    private final float defaultError = 0.0f;

    private static final long serialVersionUID = 5917024689404322880L;

    private ArrayList<Object> data;

    private ArrayList<RowData> dataObject;

    private ArrayList<float[]> error;

    private String columnNames[];

    private int numberOfEditableRows = Integer.MAX_VALUE;

    private int disabledRow = -1;

    private int disabledCol = -1;

    private int visibleCount = 0;

    MyTableActionListener listener = null;

    Hashtable<Integer, Boolean> visible = new Hashtable<Integer, Boolean>();

    /**
     * Klass MyTableModel kontruktorius.
     *
     * @param rowHeaderSize    Eiluts antrats ilgis
     * @param columnHeaderSize Stulpelio antrats ilgis
     * @param listener         Klas, kuri klausosi ar vyko lstels pakeitimas.
     */

    public MyTableModel(int rowHeaderSize, int columnHeaderSize, MyTableActionListener listener, Integer count) {

        data = new ArrayList<Object>();
        dataObject = new ArrayList<RowData>();
        error = new ArrayList<float[]>();
        this.visibleCount = (count == null ? columnHeaderSize : count);
        if (rowHeaderSize < 0)
            throw new NegativeArraySizeException("rowHeaderSize is negative");
        for (int i = 0; i <= count; i++)
            visible.put(i, true);
        columnNames = new String[columnHeaderSize];
        for (int i = 0; i < rowHeaderSize; i++) {
            error.add(new float[columnHeaderSize]);
            data.add(new String[columnHeaderSize]);
            this.dataObject.add(null);
        }
        this.listener = listener;
    }

    /**
     * Ar lastel redaguojama
     *
     * @param row Eilut
     * @param col Stulpelis
     * @return Grinama true jei redaguojama arba false, jei ne.
     */

    public boolean isCellEditable(int row, int col) {
        if (row < numberOfEditableRows) {
            if (row != disabledRow && col != disabledCol)
                return true;
            else
                return false;
        } else {
            return false;
        }

    }

    /**
     * Grinti stulpeli skaii
     *
     * @return Grinamas stulpeli skaiius
     */
    public int getColumnCount() {
        if (visible.size() == 0)
            return columnNames.length;
        else
            return visibleCount;
    }

    /**
     * Grinti eilui skaii
     *
     * @return Grinamas eilui skaiius
     */
    public int getRowCount() {
        return data.size();
    }

    /**
     * Grinti cels reikm
     *
     * @param row Eilut
     * @param col Stulpelis
     * @return Grinama cel
     */

    public Object getValueAt(int row, int col) {
        if (row < data.size() && col < ((Object[]) data.get(row)).length)
            return ((Object[]) data.get(row))[mapColumn(col)];
        return null;
    }

    /**
     * Nustatyti cel tam tikr reikm
     *
     * @param value Reikm row Eilut col Stulpelis
     */
    public void setValueAt(Object value, int row, int col) {
        ((Object[]) data.get(row))[mapColumn(col)] = value;
        fireTableCellUpdated(row, col);
        if (listener != null) {
            listener.cellUpdated(mapColumn(col), row);

        }
    }

    /**
     * Maps to internal column number
     *
     * @param col
     * @return
     */
    public int mapColumn(int col) {
        int count = -1;
        int vizCount = -1;
        for (int i = 0; vizCount < col; i++) {
            Boolean value = visible.get(i);
            if (value != null && value.booleanValue())
                vizCount++;
            count++;

        }
        return count;
    }

    /**
     * Nustatyti klausymosi klas
     *
     * @param listener Klasymosi klas
     */
    public void setListener(MyTableActionListener listener) {
        this.listener = listener;
    }

    /**
     * Nustatyti redaguojam eilui skaii
     *
     * @param rows Eilui skaiius
     */
    public void setNumberOfEditableRows(int rows) {
        this.numberOfEditableRows = rows;
    }

    /**
     * alinti nurodyt eilut
     *
     * @param row Eiluts numeris
     */

    public void deleteRow(int row) throws Exception {
        data.remove(row);
        this.dataObject.remove(row);
        if (listener != null)
            listener.rowDeleted(row);
        fireTableDataChanged();
    }

    /**
     * Sets error for the specified row
     *
     * @param row   index of row
     * @param col   column
     * @param error error
     */
    public void setError(int row, int col, Float error) {
        if (error == null)
            error = defaultError;
        this.error.get(row)[col] = error;
        fireTableDataChanged();
    }

    /**
     * Gets error for specified row
     *
     * @param row row to get
     * @param col column
     * @return error
     */
    public float getError(int row, int col) {
        if (row >= this.error.size())
            return defaultError;
        return ((float[]) error.get(row))[col];
    }

    /**
     * Papildyti viena eilute
     *
     * @param rowDataObject row data object
     */
    public void addRow(RowData rowDataObject) {
        Object[] rowData = rowDataObject.getDataArray();
        this.dataObject.add(rowDataObject);
        data.add(rowData);
        error.add(new float[rowData.length]);
        fireTableDataChanged();
    }

    /**
     * Papildyti viena eilute
     *
     * @param rowData row data array
     */
    public void addRow(Object[] rowData) {
        data.add(rowData);
        error.add(new float[rowData.length]);
        fireTableDataChanged();
    }

    /**
     * Ivalyti lentels turin
     */
    public void clearTheTable() {
        data.clear();
        dataObject.clear();
        error.clear();
        fireTableDataChanged();
    }

    /**
     * Nustatyti neredaguojama eilut
     *
     * @param row
     */
    public void setDisabledRow(int row) {
        disabledRow = row;
    }

    /**
     * Nustatyti neredaguojama eilut
     *
     * @param col
     */
    public void setDisabledColumn(int col) {
        disabledCol = col;
    }

    /**
     * Gets specified row
     *
     * @param row row to get
     * @return column array
     */
    public Object[] getRow(int row) {
        return (Object[]) data.get(row);
    }

    /**
     * Returns row object for specified row
     *
     * @param row row index
     * @return row object
     */
    public RowData getDataObject(int row) {
        return dataObject.get(row);
    }

    public Hashtable<Integer, Boolean> getVisible() {
        return visible;
    }

    public void setVisible(int[] visibleCols) {
        visible.clear();
        for (int vis : visibleCols)
            visible.put(vis, true);
        visibleCount = visibleCols.length;
        fireTableStructureChanged();
    }

}
