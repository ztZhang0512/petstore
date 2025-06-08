//MainApp.java文件
package com.a51work6.jpetstore.ui;

import com.a51work6.jpetstore.domain.Account;

//启动类
public class MainApp {

    // 用户登录成功后，保存当前用户信息
    public static Account accout;

    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    }


}
