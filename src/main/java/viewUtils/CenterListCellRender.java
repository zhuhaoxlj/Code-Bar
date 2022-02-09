package viewUtils;

import Snippets.CodeSnippets;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Objects;

public class CenterListCellRender extends DefaultListCellRenderer {
    @Serial
    private static final long serialVersionUID = 1;
    CodeSnippets codeSnippets;

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof CodeSnippets) {
            this.codeSnippets = (CodeSnippets) value;
            setText("<html>" + "<div style=\"color: black;font-size:10px;padding-left: 10px;padding-bottom:10px \" >" + codeSnippets.getDescription() + "</div>" + "<div style=\" color: gray;font-size:9px;padding-left:13px; \" >" + codeSnippets.getPrefix() + "</div></html>");
//            setText("<html>" + this.codeSnippets.getPrefix());
//            setIcon();
            setVerticalTextPosition(SwingConstants.CENTER);
            setFont(new Font("黑体", Font.PLAIN, 18));
            setBackground(new Color(255, 255, 255));
        }
        if (isSelected) {
            setBackground(new Color(240, 248, 255));
        } else {
            setForeground(new Color(221, 229, 248));
        }
        return this;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(240, 242, 245));
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}
