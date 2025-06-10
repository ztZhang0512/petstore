// HistoryOrderFrame.java
package com.a51work6.jpetstore.ui;

import com.a51work6.jpetstore.dao.OrderDao;
import com.a51work6.jpetstore.dao.mysql.OrderDaoImp;
import com.a51work6.jpetstore.domain.Order;

import com.a51work6.jpetstore.dao.OrderDetailDao;
import com.a51work6.jpetstore.dao.ProductDao;
import com.a51work6.jpetstore.dao.mysql.OrderDetailDaoImp;
import com.a51work6.jpetstore.dao.mysql.ProductDaoImp;
import com.a51work6.jpetstore.domain.OrderDetail;
import com.a51work6.jpetstore.domain.Product;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class HistoryOrderFrame extends MyFrame {

    private JTable table;
    private String userid;

    public HistoryOrderFrame(String userid) {
        super("历史订单", 800, 600);
        this.userid = userid;

        JPanel topPanel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setHgap(20);
        layout.setVgap(10);
        topPanel.setLayout(layout);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JButton btnReturn = new JButton("返回");
        btnReturn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        topPanel.add(btnReturn);

        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(getTable());

        // 返回按钮事件
        btnReturn.addActionListener(e -> {
            setVisible(false);
        });
    }

    private JTable getTable() {
        OrderDao orderDao = new OrderDaoImp();
        List<Order> orders = orderDao.findHistoryOrdersByUser(userid); // 查询当前用户的历史订单

        Object[][] data = new Object[orders.size()][6];

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getOrderid();
            data[i][1] = "已付款"; // 固定为已付款
            data[i][2] = order.getOrderdate();
            data[i][3] = order.getAmount();
            data[i][4] = "查看详情"; // 新增详情按钮占位符
            data[i][5] = "删除";     // 原有删除按钮
        }

        TableModel model = new HistoryOrderTableModel(data);
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
                JLabel label = new JLabel(value.toString());
                label.setForeground(Color.BLUE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            });

            // 操作列蓝色字体 + 删除响应
            table.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                JLabel label = new JLabel(value.toString());
                label.setForeground(Color.BLUE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            });


            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (col == 4 && row >= 0 && data[row][0] != null) { // 点击详情列
                        try {
                            long orderId = ((Number) data[row][0]).longValue();
                            showOrderDetails(orderId);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(HistoryOrderFrame.this, "无效的订单ID", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    if (col == 5 && row >= 0 && data[row][0] != null) { // 点击删除列
                        try {
                            long orderId = ((Number) data[row][0]).longValue();
                            deleteOrder(orderId, row, model);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(HistoryOrderFrame.this, "无效的订单ID", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        } else {
            table.setModel(model);
        }

        return table;
    }

    private void showOrderDetails(long orderId) {
        OrderDetailDao orderDetailDao = new OrderDetailDaoImp();
        ProductDao productDao = new ProductDaoImp();

        // 获取该订单的所有订单明细
        List<OrderDetail> details = orderDetailDao.findByOrder(orderId);

        if (details == null || details.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到该订单的商品详情", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 创建一个面板用于显示商品详情
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        double totalAmount = 0.0;

        for (OrderDetail detail : details) {
            String productid = detail.getProductid();
            int quantity = detail.getQuantity();
            double unitCost = detail.getUnitcost();

            // 查询商品信息
            Product product = productDao.findById(productid);
            if (product == null) continue;

            String cname = product.getCname();
            String descn = product.getDescn();

            totalAmount += unitCost * quantity;

            // 构建每项商品的信息
            JLabel label = new JLabel("商品名称：" + cname +
                    " | 数量：" + quantity +
                    " | 单价：" + unitCost +
                    " | 描述：" + descn);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            panel.add(label);
        }

        // 添加总金额
        JLabel totalLabel = new JLabel("订单总金额：" + totalAmount);
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(totalLabel);

        // 显示详情对话框
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "订单详情 - 订单ID: " + orderId, JOptionPane.INFORMATION_MESSAGE);
    }


    private void deleteOrder(long orderId, int rowIndex, TableModel model) {
        OrderDao orderDao = new OrderDaoImp();
        Order order = new Order();
        order.setOrderid(orderId);
        int result = orderDao.remove(order);

        if (result > 0) {
            ((HistoryOrderTableModel) model).removeRow(rowIndex);
            JOptionPane.showMessageDialog(this, "订单已删除");
        } else {
            JOptionPane.showMessageDialog(this, "删除失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
