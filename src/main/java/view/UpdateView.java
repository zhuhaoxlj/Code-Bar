package view;

import viewUtils.ResourcesUtils;

import javax.swing.*;
import java.util.Objects;

public class UpdateView extends JFrame {
    public UpdateView(){
        // 主体框架
        JLabel jframe = new JLabel();
        Icon jframeIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/updateView/jframe.png", "right", ".png")).getAbsolutePath());
        jframe.setIcon(jframeIcon);
        jframe.setBounds(0,0,889,544);


        this.setUndecorated(true);
        this.add(jframe);
        this.setVisible(true);
    }
}
