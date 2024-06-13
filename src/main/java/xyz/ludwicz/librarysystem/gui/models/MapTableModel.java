package xyz.ludwicz.librarysystem.gui.models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MapTableModel<T extends Comparable<T>> extends AbstractTableModel {

    private final String[] columnNames;
    private final Map<T, Object[]> data;
    private final List<T> keys;

    public MapTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        this.data = new TreeMap<>();
        this.keys = new ArrayList<>(data.keySet());
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T key = keys.get(rowIndex);
        Object[] rowData = data.get(key);
        return rowData[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        T key = keys.get(rowIndex);
        Object[] rowData = data.get(key);
        rowData[columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void setRow(T key, Object[] row) {
        if (!data.containsKey(key)) {
            keys.add(key);
            keys.sort(null); // 정렬된 순서를 유지하기 위해 정렬
        }
        data.put(key, row);
    }

    public void removeRow(T key) {
        data.remove(key);
        keys.remove(key);
    }

    public Set<T> keySet() {
        return data.keySet();
    }

    public T getKeyAtRow(int rowIndex) {
        return keys.get(rowIndex);
    }
}