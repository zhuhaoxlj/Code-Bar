package view;


import Snippets.CodeSnippets;
import viewUtils.CenterListCellRender;
import viewUtils.ResourcesUtils;
import viewUtils.Style;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;


/* loaded from: KaChat_Client.jar:cn/com/view/MainChatCard.class */
public class CenterCardList extends JLabel {
    List<CodeSnippets> codeSnippetsList;
    public DefaultListModel jListModel = new DefaultListModel();
    public JList<CodeSnippets> jList = new JList<>();
    JScrollPane listJS = new JScrollPane(this.jList);

    /* JADX INFO: Access modifiers changed from: package-private */
    public CenterCardList(List<CodeSnippets> users) {
        this.codeSnippetsList = users;
        for (CodeSnippets user : users) {
            this.jListModel.addElement(user);
        }
        init();
        assemble();
    }

    private void init() {
        setBounds(0, 0, 250, 537);
        setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/centerList.png", "centerList", ".png")).getAbsolutePath()));
        setLayout(null);
        this.listJS.setBorder(Style.nullBorder);
        this.listJS.setOpaque(false);
        this.listJS.getViewport().setOpaque(false);
        this.listJS.setBounds(0, 0, 249, 537);
        this.listJS.setVerticalScrollBarPolicy(20);
        this.listJS.setHorizontalScrollBarPolicy(31);
        this.listJS.getVerticalScrollBar().setUI(new ScrollBarUI());
        this.jList.setCellRenderer(new CenterListCellRender());
        this.jList.setOpaque(false);
        this.jList.setFixedCellHeight(79);
        this.jList.setModel(this.jListModel);
        this.jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println(CenterCardList.this.jList.getSelectedIndex());
                HomeView.updateCodeInfo(CenterCardList.this.jList.getSelectedIndex());
            }
        });
//        this.jList.setSelectedIndex(0);
//        this.jList.setSelectionMode(0);
    }

    private void assemble() {
        add(this.listJS);
    }
}
