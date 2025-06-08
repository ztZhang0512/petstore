package com.a51work6.jpetstore.ui;

import com.a51work6.jpetstore.dao.OrderDao;
import com.a51work6.jpetstore.dao.mysql.OrderDaoImp;
import com.a51work6.jpetstore.domain.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PendingOrderFrame extends MyFrame {

    private JTable table;
    private String userid;

    public PendingOrderFrame(String userid, ProductListFrame productListFrame) {
        super("待支付订单", 800, 600);
        this.userid = userid;

        JPanel topPanel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setHgap(20);
        layout.setVgap(10);
        topPanel.setLayout(layout);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JButton btnReturn = new JButton("返回商品列表");
        btnReturn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        topPanel.add(btnReturn);

        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(getTable());

        // 返回按钮事件
        btnReturn.addActionListener(e -> {
            productListFrame.setVisible(true);
            setVisible(false);
        });
    }

    private JTable getTable() {
        OrderDao orderDao = new OrderDaoImp();
        List<Order> orders = orderDao.findPendingOrdersByUser(userid); // ✅ 只查当前用户未删除的订单
        Object[][] data = new Object[orders.size()][5];

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getOrderid();
            data[i][1] = order.getStatus() == 0 ? "待付款" : "已付款";
            data[i][2] = order.getOrderdate();
            data[i][3] = order.getAmount();
            data[i][4] = "删除";
        }


        TableModel model = new PendingOrderTableModel(data);
        if (table == null) {
            table = new JTable(model);
            // 设置居中渲染
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // 操作列蓝色字体 + 删除响应
            table.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                JLabel label = new JLabel();
                if (value != null) {
                    label.setText(value.toString());
                } else {
                    label.setText(""); // 空字符串或 "N/A"
                }
                label.setForeground(Color.BLUE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            });


            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (col == 4 && row >= 0 && data.length > row && data[row] != null && data[row][0] != null) {
                        try {
                            long orderId = ((Number) data[row][0]).longValue();
                            deleteOrder(orderId, row, model);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(PendingOrderFrame.this, "无效的订单ID", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        } else {
            table.setModel(model);
        }

        return table;
    }

    private void deleteOrder(long orderId, int rowIndex, TableModel model) {
        OrderDao orderDao = new OrderDaoImp();
        Order order = new Order();
        order.setOrderid(orderId);
        order.setIsdel(1); // 逻辑删除
        int result = orderDao.remove(order);

        if (result > 0) {
            ((PendingOrderTableModel) model).removeRow(rowIndex);
            JOptionPane.showMessageDialog(this, "订单已删除（逻辑）");
        } else {
            JOptionPane.showMessageDialog(this, "删除失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
