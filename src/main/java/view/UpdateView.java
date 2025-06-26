package view;

import viewUtils.ResourcesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class UpdateView extends JFrame {
    public UpdateView() {
        // 主体框架
        JLabel jframe = new JLabel();
        Icon jframeIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/updateView/jframe.png", "codesnippets", ".png")).getAbsolutePath());
        jframe.setIcon(jframeIcon);
        jframe.setBounds(0, 0, 889, 544);
        // logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/code.png", "code", ".png")).getAbsolutePath());
        this.setIconImage(logo.getImage());

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
                783, 21, 18, 18);
        maximizeButton.setBounds(
                817, 21, 18, 18);
        closeButton.setBounds(
                851, 21, 18, 18);
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
                UpdateView.this.setExtendedState(JFrame.ICONIFIED);
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
                UpdateView.this.dispose();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                closeButton.setIcon(icon3);
            }
        });

        // 背景块
        JLabel background = new JLabel();
        Icon backgroundIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/updateView/updateBg.png", "codesnippets", ".png")).getAbsolutePath());
        background.setIcon(backgroundIcon);
        background.setBounds(56,77, 777,342);

        // 确认更新按钮
        JLabel confirm = new JLabel();
        Icon confirmIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/updateView/confirmUpdateButton.png", "codesnippets", ".png")).getAbsolutePath());
        confirm.setIcon(confirmIcon);
        confirm.setBounds(246,448,130,70);

        // 取消更新按钮
        JLabel cancel = new JLabel();
        Icon cancelIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/updateView/cancelUpdateButton.png", "codesnippets", ".png")).getAbsolutePath());
        cancel.setIcon(cancelIcon);
        cancel.setBounds(540,448,130,70);

        // 标题
        JLabel title = new JLabel();
        Icon titleIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/updateView/updateTitle.png", "codesnippets", ".png")).getAbsolutePath());
        title.setIcon(titleIcon);
        title.setBounds(384,22,120,35);

        jframe.add(minimizeButton);
        jframe.add(maximizeButton);
        jframe.add(closeButton);
        jframe.add(background);
        jframe.add(confirm);
        jframe.add(cancel);
        jframe.add(title);

        this.add(jframe);
        this.setSize(889, 544);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setVisible(true);
    }
}
