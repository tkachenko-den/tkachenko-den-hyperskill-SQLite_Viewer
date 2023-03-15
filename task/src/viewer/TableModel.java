package viewer;

import javax.swing.table.AbstractTableModel;

class TableModel extends AbstractTableModel {

        TableData td = new TableData();

        public void setTd(TableData td) {
            this.td = td;
        }

        @Override
        public int getRowCount() {
            return td.data.size();
        }

        @Override
        public int getColumnCount() {
            return td.columns.size();
        }

        @Override
        public String getColumnName(int column) {
            return td.columns.get(column);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return td.data.get(rowIndex).get(columnIndex);
        }
    }