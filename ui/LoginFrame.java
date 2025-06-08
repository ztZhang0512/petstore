//LoginFrame.java文件
package com.a51work6.jpetstore.ui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.a51work6.jpetstore.dao.AccountDao;
import com.a51work6.jpetstore.dao.mysql.AccountDaoImp;
import com.a51work6.jpetstore.domain.Account;

//用户登录窗口
public class LoginFrame extends MyFrame {

    private JTextField txtAccountId;
    private JPasswordField txtPassword;

    public LoginFrame() {
        super("用户登录", 400, 230);
        // 设置布局管理为绝对布局
        getContentPane().setLayout(null);

        JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(SwingConstants.RIGHT);
        label1.setBounds(51, 33, 83, 30);
        getContentPane().add(label1);
        label1.setText("账号：");
        label1.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        txtAccountId = new JTextField(10);
        txtAccountId.setText("j2ee");
        txtAccountId.setBounds(158, 33, 157, 30);
        txtAccountId.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        getContentPane().add(txtAccountId);

        JLabel label2 = new JLabel();
        label2.setText("密码：");
        label2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        label2.setHorizontalAlignment(SwingConstants.RIGHT);
        label2.setBounds(51, 85, 83, 30);
        getContentPane().add(label2);

        txtPassword = new JPasswordField(10);
        txtPassword.setText("j2ee");
        txtPassword.setBounds(158, 85, 157, 30);
        getContentPane().add(txtPassword);

        JButton btnOk = new JButton();
        btnOk.setText("确定");
        btnOk.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnOk.setBounds(61, 140, 100, 30);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton();
        btnCancel.setText("取消");
        btnCancel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnCancel.setBounds(225, 140, 100, 30);
        getContentPane().add(btnCancel);

        // 注册btnOk的ActionEvent事件监听器
        btnOk.addActionListener(e -> {
            AccountDao accountDao = new AccountDaoImp();
            Account account = accountDao.findById(txtAccountId.getText());

            String passwordText = new String(txtPassword.getPassword());
            if (account != null && passwordText.equals(account.getPassword())) {
                System.out.println("登录成功。");
                ProductListFrame form = new ProductListFrame();
                form.setVisible(true);
                setVisible(false);
                //用户登录成功后，将用户信息保存到MainApp.accout静态变量中
                MainApp.accout = account;
            } else {
                JLabel label = new JLabel("您输入的账号或密码有误，请重新输入。");
                label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
                JOptionPane.showMessageDialog(null, label,
                        "登录失败", JOptionPane.ERROR_MESSAGE);
            }

        });

        // 注册btnCancel的ActionEvent事件监听器
        btnCancel.addActionListener(e -> {
            // 退出系统
            System.exit(0);
        });
    }
}
