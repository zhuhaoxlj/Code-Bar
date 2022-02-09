package origin;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

@SuppressWarnings("serial")
public class Document1 extends JFrame implements ActionListener, DocumentListener {


    public boolean changed = false;
    private boolean toolBarEnable = true;

    RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);

    JFrame frame = new JFrame("Input Dialog Box Frame");
    JButton button = new JButton("Show Input Dialog Box");
    JFileChooser fc = new JFileChooser();

    private JTextArea ta;
    private String pad;
    private JToolBar toolBar;
    private JMenu file_1;
    private JMenu edit;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem find;
    private JMenuItem sall;
    private JMenu format;
    private JMenuItem font;
    private JMenu view;
    private JMenuItem showstatus;
    private JMenu help;
    private JMenuItem about;
    private JTextField status;

    public Document1() {

        super("Text Editor");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea.getDocument().addDocumentListener(this);

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.addCaretListener(new CaretListener() {
            // Each time the caret is moved, it will trigger the listener and its method
            // caretUpdate.
            // It will then pass the event to the update method including the source of the
            // event (which is our textarea control)
            public void caretUpdate(CaretEvent e) {
                JTextArea editArea = (JTextArea) e.getSource();

                // Lets start with some default values for the line and column.
                int linenum = 1;
                int columnnum = 1;

                // We create a try catch to catch any exceptions. We will simply ignore such an
                // error for our demonstration.
                try {
                    // First we find the position of the caret. This is the number of where the
                    // caret is in relation to the start of the JTextArea
                    // in the upper left corner. We use this position to find offset values (eg what
                    // line we are on for the given position as well as
                    // what position that line starts on.
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);

                    // We subtract the offset of where our line starts from the overall caret
                    // position.
                    // So lets say that we are on line 5 and that line starts at caret position 100,
                    // if our caret position is currently 106
                    // we know that we must be on column 6 of line 5.
                    columnnum = caretpos - editArea.getLineStartOffset(linenum);

                    // We have to add one here because line numbers start at 0 for getLineOfOffset
                    // and we want it to start at 1 for display.
                    linenum += 1;
                    columnnum += 1;
                } catch (Exception ex) {
                }

                // Once we know the position of the line and the column, pass it to a helper
                // function for updating the status bar.
                updateStatus(linenum, columnnum);
            }
        });

        RTextScrollPane sp = new RTextScrollPane(textArea);
        pane.add(sp);

        pad = " ";
        ta = new JTextArea(); // textarea

        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);


        file_1 = new JMenu("文件");
        file_1.setMnemonic('F');







        edit = new JMenu("编辑");
        edit.setMnemonic('E');

        cut = new JMenuItem("剪切");
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.cut();
            }
        });
        cut.setMnemonic('T');
        edit.add(cut);

        copy = new JMenuItem("复制");
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.copy();
            }
        });
        copy.setMnemonic('C');
        edit.add(copy);

        paste = new JMenuItem("粘贴");
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.paste();
            }
        });
        paste.setMnemonic('P');
        edit.add(paste);

        find = new JMenuItem("查找");
        find.addActionListener(this);
        find.setMnemonic('F');
        edit.add(find);

        sall = new JMenuItem("选择全部");
        sall.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.selectAll();
            }
        });
        sall.setMnemonic('A');
        edit.add(sall);

        format = new JMenu("格式");

        font = new JMenuItem("字体");
        font.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFontDialog fontDialog = new JFontDialog();
                fontDialog.setVisible(true);
                Font selectedFont = fontDialog.getFont();
                System.out.println(selectedFont.toString());
                textArea.setFont(selectedFont);

            }
        });
        format.add(font);

        view = new JMenu("查看");

        showstatus = new JMenuItem("状态栏");
        showstatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (toolBarEnable == true) {
                    toolBar.setVisible(false);
                    toolBarEnable = false;

                } else {
                    toolBar.setVisible(true);
                    toolBarEnable = true;
                }
            }
        });
        view.add(showstatus);

        help = new JMenu("帮助");

        about = new JMenuItem("关于");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Text Editor in Java. \n\n 2021", "Text Editor", 1);
            }
        });
        help.add(about);

        pane.add(toolBar, BorderLayout.SOUTH);

        status = new JTextField();
        status.setText("行: 1 列: 1");
        toolBar.add(status);

        setVisible(true);

    }

    void writetofile(File ff) throws Exception {
        FileWriter fw = new FileWriter(ff.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(textArea.getText());
        bw.close();
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + "\n============================================================\nOUTPUT:\n\n\n\n",
                pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
    }

//	public void actionPerformed(ActionEvent e) {
//		JMenuItem choice = (JMenuItem) e.getSource();
//
//	}



    @Override
    public void insertUpdate(DocumentEvent e) {
        changed = true;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changed = true;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changed = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    // This helper function updates the status bar with the line number and column
// number.
    private void updateStatus(int linenumber, int columnnumber) {
        status.setText("行: " + linenumber + " 列: " + columnnumber);
    }

    public static void main(String[] args) {
        new Document1();
    }
}
