package origin;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author PANKAJ
 */
public class JFontDialog extends JDialog {

    private static final long serialVersionUID = -6437493905392469749L;
    private Font font;
    private Font[] fonts;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel previewLabel;
    private JList familyList;
    private JTextField familyTextField;
    private JList styleList;
    private JTextField styleTextField;
    private JList sizeList;
    private JTextField sizeTextField;

    public JFontDialog() {
        this(new Font("Arial", 0, 12));
    }

    public JFontDialog(Font font) {
        this("Font dialog", font);
    }

    public JFontDialog(String title, Font font) {
        super.setTitle(title);
        this.font = font;
        this.init();
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

    private void init() {
        this.fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        this.setLayout(new FlowLayout());
        this.setMinimumSize(new Dimension(400 + 80, 350));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLocationByPlatform(true);
        this.add(this.getFamilyPanel());
        this.add(this.getStylePanel());
        this.add(this.getSizePanel());
        this.add(this.getPreviewPanel());
        this.add(this.getButtonPanel());
        this.eventHandling();
    }

    private JPanel getFamilyPanel() {
        JPanel familyPanel = new JPanel();
        familyPanel.setPreferredSize(new Dimension(200 + 10, 200 + 10));
        familyPanel.setBorder(new TitledBorder("Font:"));
        familyPanel.setLayout(new FlowLayout());
        familyPanel.add(this.getFamilyTextBox());
        familyPanel.add(this.getFamilyScrooledList());
        return familyPanel;
    }

    private JTextField getFamilyTextBox() {
        familyTextField = new JTextField();
        familyTextField.setPreferredSize(new Dimension(200, 20));
        familyTextField.setText(this.getFont().getFamily());
        familyTextField.setEditable(false);
        return familyTextField;
    }

    private JScrollPane getFamilyScrooledList() {
        familyList = new JList(this.getFamily());
        JScrollPane fontPane = new JScrollPane();
        fontPane.setViewportView(familyList);
        fontPane.setPreferredSize(new Dimension(200, 152));
        familyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        familyList.setSelectedValue(this.getFont().getFamily(), true);

        return fontPane;
    }

    private JPanel getStylePanel() {
        JPanel stylePanel = new JPanel();
        stylePanel.setPreferredSize(new Dimension(150 + 10, 200 + 10));
        stylePanel.setBorder(new TitledBorder("Style:"));
        stylePanel.setLayout(new FlowLayout());
        stylePanel.add(this.getStyleTextBox());
        stylePanel.add(this.getStyleScrooledList());
        return stylePanel;
    }

    private JTextField getStyleTextBox() {
        styleTextField = new JTextField();
        styleTextField.setPreferredSize(new Dimension(150, 20));
        String style = this.calculateStyle(this.getFont(), this.getFont().getFamily());
        if (style.equals("")) {
            style = "Plain";
        }
        styleTextField.setText(style);
        styleTextField.setEditable(false);
        return styleTextField;
    }

    private JScrollPane getStyleScrooledList() {
        styleList = new JList(this.getFontStyles(this.getFont().getFamily()));
        JScrollPane stylePane = new JScrollPane();
        stylePane.setViewportView(styleList);
        stylePane.setPreferredSize(new Dimension(150, 152));
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String style = this.calculateStyle(this.getFont(), this.getFont().getFamily());
        if (style.equals("")) {
            style = "Plain";
        }
        styleList.setSelectedValue(style, true);

        return stylePane;
    }

    private JPanel getSizePanel() {
        JPanel sizePanel = new JPanel();
        sizePanel.setPreferredSize(new Dimension(50 + 10, 200 + 10));
        sizePanel.setBorder(new TitledBorder("Size:"));
        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(this.getSizeTextBox());
        sizePanel.add(this.getSizeScrooledList());
        return sizePanel;
    }

    private JTextField getSizeTextBox() {
        sizeTextField = new JTextField();
        sizeTextField.setPreferredSize(new Dimension(50, 20));
        int size = this.getFont().getSize();
        sizeTextField.setText(size + "");
        return sizeTextField;
    }

    private JScrollPane getSizeScrooledList() {
        sizeList = new JList(this.getFontSize());
        JScrollPane sizePane = new JScrollPane();
        sizePane.setViewportView(sizeList);
        sizePane.setPreferredSize(new Dimension(50, 152));
        sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sizeList.setSelectedValue(this.getFont().getSize(), true);
        return sizePane;
    }

    private JPanel getPreviewPanel() {
        JPanel previewPanel = new JPanel();
        previewPanel.setBorder(new TitledBorder("Preview"));
        previewPanel.setPreferredSize(new Dimension(355, 90));
        previewPanel.setLayout(new FlowLayout());
        previewPanel.add(this.getPreviewLabel());
        return previewPanel;
    }

    private JLabel getPreviewLabel() {
        previewLabel = new JLabel("ABC abc 123");
        previewLabel.setPreferredSize(new Dimension(350, 80));
        previewLabel.setHorizontalAlignment(JLabel.CENTER);
        previewLabel.setFont(this.getFont());
        return previewLabel;
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(80, 60));
        buttonPanel.setLayout(new FlowLayout());
        ((FlowLayout) buttonPanel.getLayout()).setVgap(5);
        buttonPanel.add(this.getOKButton());
        buttonPanel.add(this.getCancelButton());
        return buttonPanel;
    }

    private JButton getOKButton() {
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 25));

        return okButton;
    }

    private JButton getCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(80, 25));

        return cancelButton;
    }

    private String[] getFontStyles(String fontFamily) {
        List fontStyles = new ArrayList();
        for (int i = 0; i < fonts.length; i++) {
            Font f = fonts[i];
            if (f.getFamily().equals(fontFamily)) {
                if (f.getFontName().length() == fontFamily.length()) {
                    fontStyles.add("Plain");
                    continue;
                }
                fontStyles.add(this.calculateStyle(f, fontFamily));
            }
        }
        String[] str = new String[fontStyles.size()];
        for (int i = 0; i < str.length; i++) {
            str[i] = fontStyles.get(i).toString();
        }
        return str;
    }

    private String[] getFamily() {
        Set stylesSet = new TreeSet();
        for (int i = 0; i < this.fonts.length; i++) {
            stylesSet.add(this.fonts[i].getFamily());
        }
        Object[] obj = stylesSet.toArray();
        String[] str = new String[obj.length];
        for (int i = 0; i < str.length; i++) {
            str[i] = obj[i].toString();
        }
        return str;
    }

    private String calculateStyle(Font font, String fontFamily) {
        StringBuffer style = new StringBuffer(font.getFontName().replace(fontFamily, ""));
        if (style.toString().startsWith(" ")) {
            style.replace(0, 1, "");
        }
        return style.toString();
    }

    private Integer[] getFontSize() {
        Integer[] fs = new Integer[25];
        int n = 6;
        for (int i = 0; i < fs.length; i++) {
            n = n + 2;
            fs[i] = n;
        }
        return fs;
    }

    private void updateFont() {
        String family = this.familyTextField.getText();
        String style = this.styleTextField.getText();
        int size = Integer.parseInt(this.sizeTextField.getText());
        if (!style.equals("Plain") && !style.contains(".")) {
            family = family + " " + style;
        } else if (!style.equals("Plain") && style.contains(".")) {
            family = family + "." + style;
        }
        this.font = new Font(family, 0, size);
        this.previewLabel.setFont(this.font);
    }

    private void eventHandling() {
        this.familyList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String selectedFamily = familyList.getSelectedValue().toString();
                familyTextField.setText(selectedFamily);
                styleList.setListData(getFontStyles(selectedFamily));
                styleList.setSelectedIndex(0);
            }
        });
        this.styleList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String selectedStyle = styleList.getSelectedValue().toString();
                styleTextField.setText(selectedStyle);
                updateFont();
            }
        });
        this.sizeList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedSize = Integer.parseInt(sizeList.getSelectedValue().toString());
                sizeTextField.setText(selectedSize + "");
                updateFont();
            }
        });
        this.sizeTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                if (evt.getKeyChar() < '0' || evt.getKeyChar() > '9') {
                    evt.consume();
                }
            }

            public void keyReleased(KeyEvent evt) {
                String value = sizeTextField.getText();
                evt.consume();
                if (value.equals("") || value.equals("0")) {
                    sizeTextField.setText("8");
                }
                sizeList.setSelectedValue(Integer.valueOf(value), true);
                updateFont();
            }
        });
        this.okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setFont(previewLabel.getFont());
                dispose();
            }
        });
        this.cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}