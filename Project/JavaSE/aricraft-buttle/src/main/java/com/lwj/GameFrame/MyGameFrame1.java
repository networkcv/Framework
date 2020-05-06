package com.lwj.GameFrame;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * create by lwj on 2020/4/24
 */
@Slf4j
public class MyGameFrame1 extends JFrame {
    @Override
    public void paint(Graphics g) {
        g.drawLine(100, 100, 300, 300);
        g.drawRect(100, 100, 300, 300);
        g.drawOval(100, 100, 300, 300);
    }

    /**
     * 初始化窗口
     */
    public void launchFrame() {
        log.info("启动界面加载");
        this.setVisible(true);
        this.setSize(500, 500);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("closing");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                log.info("closed");
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        MyGameFrame1 myGameFrame = new MyGameFrame1();
        myGameFrame.launchFrame();
//      TimeUnit.SECONDS.sleep(5);
//      myGameFrame.dispose();
    }
    /*
        22:24:35.899 [main] INFO com.lwj.GameFrame.MyGameFrame - 启动界面加载
        22:24:38.804 [AWT-EventQueue-0] INFO com.lwj.GameFrame.MyGameFrame - closing
        22:24:40.978 [AWT-EventQueue-0] INFO com.lwj.GameFrame.MyGameFrame - closed
     */
}
