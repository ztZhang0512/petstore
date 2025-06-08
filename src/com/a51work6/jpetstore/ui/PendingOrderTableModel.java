package com.a51work6.jpetstore.ui;

import javax.swing.table.AbstractTableModel;

public class PendingOrderTableModel extends AbstractTableModel {

    private String[] columnNames = {"订单编号", "状态", "下单时间", "金额", "付款", "操作"};
    private Object[][] data;

    public PendingOrderTableModel(Object[][] data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        return data[row][column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 4 || column == 5; // 允许编辑付款和删除列
    }

    public void removeRow(int row) {
        Object[][] newData = new Object[data.length - 1][data[0].length];
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            if (i != row) {
                newData[index++] = data[i];
            }
        }
        data = newData;
        fireTableDataChanged();
    }
}
