package view;

import viewUtils.ResourcesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

/**
 * @author MarkGosling
 * @date 2022/2/10 13:04
 */
public class RelaxView extends JFrame {
    private int relaxTime = 30;
    private int minute = 0;
    private final JLabel countDownTime;

    public RelaxView() {
        // 窗体
        JLabel jframe = new JLabel();
        Icon jframeIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/relaxView/jframe.png", "codesnippets", ".png")).getAbsolutePath());
        jframe.setIcon(jframeIcon);
        jframe.setBounds(0, 0, 695, 461);

        // 窗体控制按钮
        JLabel minimizeButton = new JLabel();
        JLabel maximizeButton = new JLabel();
        JLabel closeButton = new JLabel();
        Icon icon1 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/minimize.png", "minimize", ".png")).getAbsolutePath());
        Icon icon2 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/maximize.png", "maximize", ".png")).getAbsolutePath());
        Icon icon3 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/close.png", "close", ".png")).getAbsolutePath());
        minimizeButton.setIcon(icon1);
        maximizeButton.setIcon(icon2);
        closeButton.setIcon(icon3);
        minimizeButton.setBounds(
                579, 27, 18, 18);
        maximizeButton.setBounds(
                613, 27, 18, 18);
        closeButton.setBounds(
                647, 27, 18, 18);
        // 三个按钮监听事件
        minimizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Icon icon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/minEnter.png", "minEnter", ".png")).getAbsolutePath());
                minimizeButton.setIcon(icon);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                minimizeButton.setIcon(icon1);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                RelaxView.this.setExtendedState(JFrame.ICONIFIED);
            }
        });
        maximizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Icon icon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/maxEnter.png", "maxEnter", ".png")).getAbsolutePath());
                maximizeButton.setIcon(icon);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                maximizeButton.setIcon(icon2);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }
        });
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Icon icon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/closeEnter.png", "closeEnter", ".png")).getAbsolutePath());
                closeButton.setIcon(icon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                RelaxView.this.dispose();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                closeButton.setIcon(icon3);
            }
        });
        // 倒计时数字
        countDownTime = new JLabel(relaxTime + "");
        countDownTime.setFont(new Font("黑体", Font.PLAIN, 160));
        countDownTime.setForeground(new Color(225, 247, 203));
        countDownTime.setBounds(268, 217, 180, 180);
        jframe.add(countDownTime);
        jframe.add(minimizeButton);
        jframe.add(maximizeButton);
        jframe.add(closeButton);

        this.add(jframe);
        this.setSize(695, 461);
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);
        waitPeriodToRelax();
    }

    public void waitPeriodToRelax() {
        System.out.println("定时休息开启");
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    minute += 1;
                    System.out.println(minute);
                    if (minute % 40 == 0) {
                        relaxTime();
                    }
                }
            }
        }.start();
    }

    public void relaxTime() {
        System.out.println("休息时间到~");
        RelaxView.this.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (relaxTime != 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    relaxTime -= 1;
                    countDownTime.setText(relaxTime + "");
                }
                relaxTime = 30;
                RelaxView.this.dispose();
            }
        }.start();
    }
}
