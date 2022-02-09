package viewUtils;

import Snippets.CodeSnippets;

import java.awt.*;
import java.io.Serial;
import java.util.Objects;
import javax.swing.*;

/* loaded from: KaChat_Client.jar:cn/com/view/viewutil/viewUtils.FriendsCellRender.class */
public class GroupListCellRender extends DefaultListCellRenderer {
    @Serial
    private static final long serialVersionUID = 1;
    String codeSnippets;

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof String) {
            this.codeSnippets = (String) value;
//            setText("<html>" + this.codeSnippets.getPrefix() + "<div style=\"color: gray;font-size:10px\" >" + msg+"123" + "</div></html>");
            setText("<html>" + this.codeSnippets);
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/groupTag.png", "groupTag", ".png")).getAbsolutePath());
            icon.setImage(icon.getImage().getScaledInstance(45, 22, 16));
            setIcon(icon);
            setFont(new Font("黑体", Font.PLAIN, 18));
            setVerticalTextPosition(0);
            setHorizontalTextPosition(4);
            setBackground(Style.nullColor);
        }
        if (isSelected) {
            setBackground(new Color(47, 155, 255));
        } else {
            setForeground(new Color(221, 229, 248));
        }
        return this;
    }
}
