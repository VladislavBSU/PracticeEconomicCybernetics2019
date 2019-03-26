package pack;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CatalogTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<>();
    private List<GameNode> infoNodes = new ArrayList<>();

    private static final String[] columnNames = new String[]{"Name", "Year", "System", "Price($)"};
    private static final Class<?>[] columnTypes = new Class[]{String.class, String.class, String.class, Integer.class};

    CatalogTableModel() {
    }

    CatalogTableModel(List<GameNode> al) {
        setInfoNodes(al);
    }

    private void setInfoNodes(List<GameNode> gameNodes) {
        infoNodes = gameNodes;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return infoNodes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GameNode gameNode = infoNodes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return gameNode.getName();
            case 1:
                return gameNode.getYear();
            case 2:
                return gameNode.getSystem();
            case 3:
                return gameNode.getPrice();
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
}