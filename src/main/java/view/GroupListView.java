package view;


import Snippets.CodeSnippets;
import viewUtils.GroupListCellRender;
import viewUtils.ResourcesUtils;
import viewUtils.Style;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import javax.swing.*;


/* loaded from: KaChat_Client.jar:cn/com/view/MainChatCard.class */
public class GroupListView extends JLabel {
    List<String> codeSnippetsList;
    public DefaultListModel jListModel = new DefaultListModel();
    public JList<String> jList = new JList<>();
    JScrollPane listJS = new JScrollPane(this.jList);

    /* JADX INFO: Access modifiers changed from: package-private */
    public GroupListView(List<String> users) {
        this.codeSnippetsList = users;
        for (String user : users) {
            this.jListModel.addElement(user);
        }
        init();
        assemble();
    }

    private void init() {
        setBounds(0, 0, 244, 474);
        setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/groupList.png", "groupList", ".png")).getAbsolutePath()));
        setLayout(null);
        this.listJS.setBorder(Style.nullBorder);
        this.listJS.setOpaque(false);
        this.listJS.getViewport().setOpaque(false);
        this.listJS.setBounds(0, 0, 244, 474);
        this.listJS.setVerticalScrollBarPolicy(20);
        this.listJS.setHorizontalScrollBarPolicy(31);
        this.listJS.getVerticalScrollBar().setUI(new ScrollBarUI());
        this.jList.setCellRenderer(new GroupListCellRender());
        this.jList.setOpaque(false);
        this.jList.setFixedCellHeight(36);
        this.jList.setSelectionInterval(100, 1000);
        this.jList.setModel(this.jListModel);
        this.jList.setSelectionMode(0);
        this.jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                HomeView.updateCenterListView(GroupListView.this.jList.getSelectedIndex());
            }
        });
    }

    private void assemble() {
        add(this.listJS);
    }
}
