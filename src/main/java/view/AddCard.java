package view;


import Snippets.CodeSnippets;
import viewUtils.ResourcesUtils;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/* loaded from: KaChat_Client.jar:cn/com/view/AddCard.class */
public class AddCard extends JLabel {
    private CodeSnippets user;
    private ImageIcon imageIcon;
    private JLabel labelIcon = new JLabel();
    private JLabel acceptButton = new JLabel();
    private JLabel rejectedButton = new JLabel();
    private JLabel text = new JLabel();

    public AddCard(Object value) {
        if (value instanceof CodeSnippets) {
            this.user = (CodeSnippets) value;
            isUser();
        }
        init();
        assemble();
    }

    private void isUser() {
        this.imageIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/code.png", "code", ".png")).getAbsolutePath());
        this.text.setText("<html><div style=\"font-family:Sofia;font-size:13px;color:#f29a76;margin:2px;font-weight: bold\" >" + this.user.getContent() + " (UID:" + this.user.getPrefix() + ")</div><div style=\"font-family:Sofia;font-size:10px;color:gray;margin:3px;font-weight: bold\" ></div><div style=\"font-family:Sofia;font-size:12px;margin:3px;font-weight: bold\" >想添加您为好友</div></html>");
    }

    private void init() {
        setLayout(null);
        setSize(260, 279);
        setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/centerListBackground.png", "centerListBackground", ".png")).getAbsolutePath()));
        this.imageIcon.setImage(this.imageIcon.getImage().getScaledInstance(20, 20, 4));
        this.labelIcon.setIcon(this.imageIcon);
        this.labelIcon.setBounds(0, 25, 20, 20);
        this.text.setBounds(0, 25, 30, 10);
        this.acceptButton.setBounds(0, 25, 10, 30);
        this.acceptButton.setHorizontalTextPosition(0);
        this.acceptButton.setText("同意");
        this.acceptButton.setForeground(Color.white);
        this.acceptButton.setFont(new Font("黑体", 0, 20));
        this.acceptButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/code.png", "code", ".png")).getAbsolutePath()));
        this.rejectedButton.setBounds(0, 0, 10, 30);
        this.rejectedButton.setHorizontalTextPosition(0);
        this.rejectedButton.setText("忽略");
        this.rejectedButton.setForeground(Color.white);
        this.rejectedButton.setFont(new Font("黑体", 0, 20));
        this.rejectedButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/code.png", "code", ".png")).getAbsolutePath()));
    }

    private void assemble() {
        add(this.labelIcon);
        add(this.text);
        add(this.acceptButton);
        add(this.rejectedButton);
    }
}
