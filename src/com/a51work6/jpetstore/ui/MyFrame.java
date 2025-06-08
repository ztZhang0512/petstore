//MyFrame.java文件
package com.a51work6.jpetstore.ui;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

//这是一个屏幕居中的自定义窗口
public class MyFrame extends JFrame {

    // 获得当前屏幕的宽
    private double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    // 获得当前屏幕的高
    private double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public MyFrame(String title, int width, int height) {
        super(title);

        // 设置窗口大小
        setSize(width, height);
        // 计算窗口位于屏幕中心的坐标
        int x = (int) (screenWidth - width) / 2;
        int y = (int) (screenHeight - height) / 2;
        // 设置窗口位于屏幕中心
        setLocation(x, y);

        // 注册窗口事件
        addWindowListener(new WindowAdapter() {
            // 单击窗口关闭按钮时调用
            public void windowClosing(WindowEvent e) {
                // 退出系统
                System.exit(0);
            }
        });
    }
}
