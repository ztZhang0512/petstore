//MainApp.java文件
package com.a51work6.jpetstore.ui;

import com.a51work6.jpetstore.domain.Account;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

//启动类
public class MainApp {

    // 用户登录成功后，保存当前用户信息
    public static Account accout;

    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        ImageIcon icon = new ImageIcon(MainApp.class.getResource("/icons/appIcon.png"));
        frame.setIconImage(icon.getImage());
        frame.setVisible(true);
    }


}
