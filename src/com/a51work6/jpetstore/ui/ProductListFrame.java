//ProductListFrame.java文件
package com.a51work6.jpetstore.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import com.a51work6.jpetstore.dao.ProductDao;
import com.a51work6.jpetstore.dao.mysql.ProductDaoImp;
import com.a51work6.jpetstore.domain.Product;
import javax.swing.BoxLayout;


//商品列表窗口
public class ProductListFrame extends MyFrame {

    private JTable table;
    private JLabel lblImage;
    private JLabel lblListprice;
    private JLabel lblDescn;
    private JLabel lblUnitcost;

    // 商品列表集合
    private List<Product> products = null;
    // 创建商品Dao对象
    private ProductDao dao = new ProductDaoImp();

    // 购物车，键是选择的商品Id，值是商品的数量
    private Map<String, Integer> cart = new HashMap<>();
    // 选择的商品索引
    private int selectedRow = -1;

    public ProductListFrame() {

        super("商品列表", 1000, 700);
        // 查询所有商品
        products = dao.findAll();

        // 添加顶部搜索面板
        getContentPane().add(getSearchPanel(), BorderLayout.NORTH);

        // 创建分栏面板
        JSplitPane splitPane = new JSplitPane();
        // 设置指定分隔条位置，从窗格的左边到分隔条的左边
        splitPane.setDividerLocation(600);
        // 设置左侧面板
        splitPane.setLeftComponent(getLeftPanel());
        // 设置右侧面板
        splitPane.setRightComponent(getRightPanel());
        // 把分栏面板添加到内容面板
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    // 初始化搜索面板
    private JPanel getSearchPanel() {

        JPanel searchPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) searchPanel.getLayout();
        flowLayout.setVgap(20);
        flowLayout.setHgap(40);

        JLabel lbl = new JLabel("选择商品类别：");
        lbl.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        searchPanel.add(lbl);

        String[] categorys = {"所有类别", "鱼类", "犬类", "爬行类", "猫类", "鸟类"};
        JComboBox comboBox = new JComboBox(categorys);
        comboBox.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        searchPanel.add(comboBox);

        JButton btnGo = new JButton("查询");
        btnGo.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        searchPanel.add(btnGo);

        JButton btnReset = new JButton("重置");
        btnReset.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        searchPanel.add(btnReset);

        // 注册查询按钮的ActionEvent事件监听器
        btnGo.addActionListener(e -> {
            // 所选择的类别
            String category = (String) comboBox.getSelectedItem();
            if ("所有类别".equals(category)) {
                // 查询所有数据
                products = dao.findAll();
            } else {
                // 按照类别进行查询数据
                products = dao.findByCategory(category);
            }
            TableModel model = new ProductTableModel(products);
            table.setModel(model);
        });

        // 注册重置按钮的ActionEvent事件监听器
        btnReset.addActionListener(e -> {
            products = dao.findAll();
            TableModel model = new ProductTableModel(products);
            table.setModel(model);
        });

        return searchPanel;
    }

    // 初始化右侧面板
    private JPanel getRightPanel() {

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BorderLayout());

        // 商品图片面板
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(lblImage, BorderLayout.CENTER);
        rightPanel.add(imagePanel, BorderLayout.NORTH);
        imagePanel.setBackground(Color.WHITE);

        // 商品详细信息面板
        JPanel detailPanel = new JPanel();
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));

        JSeparator separator_1 = new JSeparator();
        detailPanel.add(separator_1);

        lblListprice = new JLabel();
        lblListprice.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        detailPanel.add(lblListprice);

        lblUnitcost = new JLabel();
        lblUnitcost.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        detailPanel.add(lblUnitcost);

        lblDescn = new JLabel();
        lblDescn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        detailPanel.add(lblDescn);

        JSeparator separator_2 = new JSeparator();
        detailPanel.add(separator_2);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton btnAdd = new JButton("添加到购物车");
        btnAdd.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        buttonPanel.add(btnAdd);

        JButton btnCheck = new JButton("查看购物车");
        btnCheck.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        buttonPanel.add(btnCheck);

        JButton btnPendingOrders = new JButton("待支付订单");
        btnPendingOrders.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        buttonPanel.add(btnPendingOrders);


        JButton btnHistoryOrders = new JButton("查看历史订单"); // 新增按钮
        btnHistoryOrders.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        buttonPanel.add(btnHistoryOrders);

        detailPanel.add(buttonPanel);

        rightPanel.add(detailPanel, BorderLayout.CENTER);

        // 添加按钮事件监听器
        btnAdd.addActionListener(e -> {
            if (selectedRow < 0) {
                return;
            }
            Product selectProduct = products.get(selectedRow);
            String productid = selectProduct.getProductid();

            if (cart.containsKey(productid)) {
                Integer quantity = cart.get(productid);
                cart.put(productid, ++quantity);
            } else {
                cart.put(productid, 1);
            }

            System.out.println(cart);
        });

        btnCheck.addActionListener(e -> {
            CartFrame cartFrame = new CartFrame(cart, this);
            cartFrame.setVisible(true);
            setVisible(false);
        });

        btnPendingOrders.addActionListener(e -> {
            PendingOrderFrame pendingOrderFrame = new PendingOrderFrame(MainApp.accout.getUserid(), this);
            pendingOrderFrame.setVisible(true);
            setVisible(false);
        });

        // 新增历史订单按钮事件
        btnHistoryOrders.addActionListener(e -> {
            HistoryOrderFrame historyOrderFrame = new HistoryOrderFrame(MainApp.accout.getUserid());
            historyOrderFrame.setVisible(true);
        });

        return rightPanel;
    }


    // 初始化左侧面板
    private JScrollPane getLeftPanel() {

        JScrollPane leftScrollPane = new JScrollPane();
        leftScrollPane.setViewportView(getTable());
        return leftScrollPane;
    }


    // 初始化左侧面板中的表格控件
    private JTable getTable() {

        TableModel model = new ProductTableModel(this.products);

        if (table == null) {
            table = new JTable(model);
            table.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 16));
            table.setRowHeight(51);
            table.setRowSelectionAllowed(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // 设置列宽
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(200);

            ListSelectionModel rowSelectionModel = table.getSelectionModel();

            rowSelectionModel.addListSelectionListener(e -> {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                selectedRow = lsm.getMinSelectionIndex();
                if (selectedRow < 0) {
                    return;
                }
                Product p = products.get(selectedRow);
                String petImage = String.format("/images/%s", p.getImage());
                ImageIcon icon = new ImageIcon(ProductListFrame.class.getResource(petImage));
                lblImage.setIcon(icon);

                String descn = p.getDescn();
                lblDescn.setText("商品描述：" + descn);

                double listprice = p.getListprice();
                String slistprice = String.format("商品市场价：%.2f", listprice);
                lblListprice.setText(slistprice);

                double unitcost = p.getUnitcost();
                String slblUnitcost = String.format("商品单价：%.2f", unitcost);
                lblUnitcost.setText(slblUnitcost);
            });
        } else {
            table.setModel(model);
        }
        return table;
    }


}
