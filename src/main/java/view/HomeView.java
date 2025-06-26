package view;

import Snippets.CodeSnippets;
import net.sf.json.JSONArray;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import origin.FindDialogDoc;
import origin.GlobalKeyListener;
import origin.JFontDialog;
import viewUtils.ResourcesUtils;
import viewUtils.Style;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class HomeView extends JFrame implements ActionListener, DocumentListener {
    public static CenterCardList centerCardList;
    public boolean isDraging;
    private int xx;
    private int yy;
    private boolean isHideOnTray = true;
    // 左边代码片段分组列表
    public static List<String> groupNameList;
    public static JLabel windowFrame;
    public static HomeView homeView;
    public static JLabel numberOfSnippets;
    public static GroupListView groupListView;
    public static List<CodeSnippets> selectedGroupSnippets;
    public static JLabel title;
    public static JLabel languageText;
    // 要删除的 CodeSnippets ID
    public static int toDeleteID = -1;
    private File file;
    public boolean changed = false;
    private boolean toolBarEnable = true;

    public static RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);

    JFrame frame = new JFrame("Input Dialog Box Frame");
    JButton button = new JButton("Show Input Dialog Box");
    JFileChooser fc = new JFileChooser();

    private JTextArea ta;
    private int count;
    private JMenuBar menuBar;
    private String pad;
    private JToolBar toolBar;
    private JMenu file_1;
    private JMenuItem n;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem saveas;
    private JMenuItem 退出;
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

    private void newFile() {
        if (changed)
            saveFile();
        file = null;
        textArea.setText("");
        changed = false;
        setTitle("Editor");
    }

    private String readFile(File file) {
        StringBuilder result = new StringBuilder();
        try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr);) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot read file !", "Error !", JOptionPane.ERROR_MESSAGE);
        }
        return result.toString();
    }

    private void saveAs(String dialogTitle) {
        JFileChooser dialog = new JFileChooser(System.getProperty("Document.this"));
        dialog.setDialogTitle(dialogTitle);
        int result = dialog.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION)
            return;
        file = dialog.getSelectedFile();
        try (PrintWriter writer = new PrintWriter(file);) {
            writer.write(textArea.getText());
            changed = false;
            setTitle("Editor - " + file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveFile() {
        if (changed) {
            int ans = JOptionPane.showConfirmDialog(null, "The file has changed. You want to save it?", "Save file",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.NO_OPTION)
                return;
        }
        if (file == null) {
            saveAs("Save");
            return;
        }
        String text = textArea.getText();
        System.out.println(text);
        try (PrintWriter writer = new PrintWriter(file);) {
            if (!file.canWrite())
                throw new Exception("Cannot write file!");
            writer.write(text);
            changed = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        JFileChooser dialog = new JFileChooser(System.getProperty("Document.this"));
        dialog.setMultiSelectionEnabled(false);
        try {
            int result = dialog.showOpenDialog(this);
            if (result == JFileChooser.CANCEL_OPTION)
                return;
            if (result == JFileChooser.APPROVE_OPTION) {
                if (changed)
                    saveFile();
                file = dialog.getSelectedFile();
                textArea.setText(readFile(file));
                changed = false;
                setTitle("Editor - " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public HomeView() {
        textArea.getDocument().addDocumentListener(this);
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

        count = 0;
        pad = " ";
        ta = new JTextArea(); // textarea

        menuBar = new JMenuBar();
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

//        setJMenuBar(menuBar);

        file_1 = new JMenu("文件");
        file_1.setMnemonic('F');
        menuBar.add(file_1);

        n = new JMenuItem("新建");
        n.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFile();
            }
        });
        n.setMnemonic('N');
        file_1.add(n);

        open = new JMenuItem("打开");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFile();

            }
        });
        open.setMnemonic('O');
        file_1.add(open);

        save = new JMenuItem("保存");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        save.setMnemonic('S');
        file_1.add(save);

        saveas = new JMenuItem("另存为...");
        saveas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAs("另存为...");
            }
        });
        file_1.add(saveas);

        退出 = new JMenuItem("退出");
        退出.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (changed)
                    saveFile();

                System.exit(0);
            }
        });
        退出.setMnemonic('Q');
        file_1.add(退出);

        edit = new JMenu("编辑");
        edit.setMnemonic('E');
        menuBar.add(edit);

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
        menuBar.add(format);

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
        menuBar.add(view);

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
        menuBar.add(help);

        about = new JMenuItem("关于");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Text Editor in Java. \n\n 2021", "Text Editor", 1);
            }
        });
        help.add(about);


        status = new JTextField();
        status.setText("行: 1 列: 1");
        toolBar.add(status);


        homeView = this;
        groupNameList = new ArrayList<>();
        initGroupsOfCodeSnippets();
        ImageIcon taskBarIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/code.png", "code", ".png")).getAbsolutePath());
        this.setIconImage(taskBarIcon.getImage());
        this.setSize(1200, 680);
        // 左边栏
        JLabel leftPart = new JLabel();
        Icon leftIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/left.png", "left", ".png")).getAbsolutePath());
        leftPart.setBounds(0, 0, 244, 680);
        leftPart.setIcon(leftIcon);

        // logo 板块
        JLabel logo = new JLabel();
        Icon logoIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/logo.png", "logo", ".png")).getAbsolutePath());
        logo.setIcon(logoIcon);
        logo.setBounds(0, 0, 244, 98);
        leftPart.add(logo);

        // 收藏夹栏
        JLabel favoriteBar = new JLabel();
        Icon favoriteIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/favoritesbar.png", "favoritesbar", ".png")).getAbsolutePath());
        favoriteBar.setIcon(favoriteIcon);
        favoriteBar.setBounds(0, 98, 244, 36);

        // 收藏夹按钮
        JLabel favoriteButton = new JLabel();
        Icon favoriteButtonIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/favoriteButton.png", "favoriteButton", ".png")).getAbsolutePath());
        favoriteButton.setIcon(favoriteButtonIcon);
        favoriteButton.setBounds(191, 8, 38, 22);
        favoriteBar.add(favoriteButton);
        leftPart.add(favoriteBar);

        // 全部片段
        JLabel allSnippets = new JLabel();
        Icon allSnippetsIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/allsnippets.png", "allsnippets", ".png")).getAbsolutePath());
        allSnippets.setIcon(allSnippetsIcon);
        allSnippets.setBounds(0, 134, 244, 36);
        leftPart.add(allSnippets);
        allSnippets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Icon allSnippetsIconSelected = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/allsnippetsSelected.png", "allsnippetsSelected", ".png")).getAbsolutePath());
                allSnippets.setIcon(allSnippetsIconSelected);
                GlobalKeyListener.nowShowList = GlobalKeyListener.loadList;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                allSnippets.setIcon(allSnippetsIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                updateCenterListView(-1);
            }
        });

        // 分组栏
        JLabel groupBar = new JLabel();
        Icon groupBarIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/groupBar.png", "groupBar", ".png")).getAbsolutePath());
        groupBar.setIcon(groupBarIcon);
        groupBar.setBounds(0, 170, 244, 36);
        leftPart.add(groupBar);

        // 分组列表
        JList<JLabel> groupList = new JList<>();
        groupList.setBackground(new Color(0, 0, 0, 0));
        groupList.setBounds(0, 206, 244, 474);
//        leftPart.add(groupList);

        groupListView = new GroupListView(groupNameList);
        groupListView.setBounds(0, 206, 244, 474);

        leftPart.add(groupListView);
        // 顶部横条
        JLabel topBar = new JLabel();
        Icon topBarIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/top.png", "top", ".png")).getAbsolutePath());
        topBar.setIcon(topBarIcon);
        topBar.setBounds(244, 0, 959, 60);

        // 中间
//        Icon groupListBackGround = new ImageIcon(Objects.requireNonNull(viewUtils.ResourcesUtils.getResource("/img/groupList.png", "groupList", ".png")).getAbsolutePath());
        List<CodeSnippets> codeSnippetsList = GlobalKeyListener.loadList;
        centerCardList = new CenterCardList(codeSnippetsList);
        centerCardList.setBounds(244, 121, 250, 537);

        // 统计代码片段数量
        JLabel countSnippets = new JLabel();
        Icon countSnippetsIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/countSnippets.png", "countSnippets", ".png")).getAbsolutePath());
        countSnippets.setIcon(countSnippetsIcon);
        numberOfSnippets = new JLabel("3个片段", JLabel.CENTER);
        numberOfSnippets.setFont(new Font("黑体", Font.PLAIN, 14));
        numberOfSnippets.setForeground(new Color(255, 255, 255));
        numberOfSnippets.setBounds(0, 0, 250, 23);
        countSnippets.add(numberOfSnippets);
        countSnippets.setBounds(244, 658, 250, 23);

        // 搜索框
        JLabel searchBar = new JLabel();
        Icon searchBarIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/searchBar.png", "searchBar", ".png")).getAbsolutePath());
        searchBar.setIcon(searchBarIcon);
        searchBar.setBounds(256, 72, 206, 40);
        JTextField searchInput = new JTextField("搜索");
        searchInput.setBounds(35, 12, 156, 15);
        searchInput.setBorder(Style.nullBorder);
        searchInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                searchInput.setText("");
            }
        });
        searchBar.add(searchInput);

        // 添加代码片段
        JLabel addSnippets = new JLabel();
        Icon addSnippetsIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippets.png", "addSnippets", ".png")).getAbsolutePath());
        addSnippets.setBounds(460, 78, 27, 27);
        addSnippets.setIcon(addSnippetsIcon);
        addSnippets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new AddSnippetsView();
            }
        });

        // 右边
        JLabel rightPart = new JLabel();
        Icon rightPartIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/right.png", "right", ".png")).getAbsolutePath());
        rightPart.setIcon(rightPartIcon);
        rightPart.setBounds(494, 61, 706, 619);

        // 删除按钮
        JLabel delete = new JLabel();
        Icon deleteIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/deleteIcon.png", "right", ".png")).getAbsolutePath());
        delete.setIcon(deleteIcon);
        delete.setBounds(519, 15, 33, 33);
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                List<CodeSnippets> temp = GlobalKeyListener.loadList;
                CodeSnippets toDeleteEntity = new CodeSnippets();
                for (CodeSnippets codeSnippets : temp) {
                    if (codeSnippets.getId() == toDeleteID) {
                        toDeleteEntity = codeSnippets;
                    }
                }
                System.out.println(toDeleteEntity.getDescription());
                temp.remove(toDeleteEntity);
                System.out.println(temp.size());
                GlobalKeyListener.loadList = temp;
                System.out.println(temp.size());
                HomeView.updateCenterListView(HomeView.groupListView.jList.getSelectedIndex());
                // 更新左侧分组列表
                updateGroupListView();
                // 更新本地文件
                writeJSON2Loacl(listToJSONStr(temp), "./CodeSnippets.json");
            }
        });
        rightPart.add(delete);


        // 代码高亮模块
        JLabel codeFiled = new JLabel();
        Icon codeFiledIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/codeFiled.png", "right", ".png")).getAbsolutePath());
        codeFiled.setIcon(codeFiledIcon);
        codeFiled.setBounds(26, 70, 658, 520);
        sp.setBounds(0, 0, 658, 520);
        codeFiled.add(sp);
        rightPart.add(codeFiled);
        // 代码文件图标
        JLabel code = new JLabel();
        Icon codeIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/codeIcon.png", "right", ".png")).getAbsolutePath());
        code.setIcon(codeIcon);
        code.setBounds(31, 13, 45, 45);
        rightPart.add(code);
        // 代码片段标题
        title = new JLabel("");
        title.setFont(new Font("黑体", Font.PLAIN, 20));
        title.setBounds(86, 24, 600, 30);
        rightPart.add(title);

        // 代码片段语言图标
        JLabel language = new JLabel();
        Icon languageIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/codeType.png", "right", ".png")).getAbsolutePath());
        language.setIcon(languageIcon);
        language.setBounds(571, 17, 70, 29);
        languageText = new JLabel("未配置", JLabel.CENTER);
        languageText.setBounds(0, 6, 70, 16);
        languageText.setForeground(new Color(52, 174, 123));
        languageText.setFont(new Font("黑体", Font.PLAIN, 16));
        rightPart.add(language);
        language.add(languageText);

        // 功能模块图标
        JLabel func = new JLabel();
        Icon funcIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/funcIcon.png", "right", ".png")).getAbsolutePath());
        func.setIcon(funcIcon);
        func.setBounds(653, 17, 29, 29);
        rightPart.add(func);

        // 窗体框架
        windowFrame = new JLabel();
        JLabel minimizeButton = new JLabel();
        JLabel maximizeButton = new JLabel();
        JLabel closeButton = new JLabel();
        Icon backGroundIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/jframe.png", "jframe", ".png")).getAbsolutePath());
        Icon icon1 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/minimize.png", "minimize", ".png")).getAbsolutePath());
        Icon icon2 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/maximize.png", "maximize", ".png")).getAbsolutePath());
        Icon icon3 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/close.png", "close", ".png")).getAbsolutePath());
        windowFrame.setIcon(backGroundIcon);
        minimizeButton.setIcon(icon1);
        maximizeButton.setIcon(icon2);
        closeButton.setIcon(icon3);
        minimizeButton.setBounds(
                840, 21, 18, 18);
        maximizeButton.setBounds(
                874, 21, 18, 18);
        closeButton.setBounds(
                908, 21, 18, 18);
        topBar.add(minimizeButton);
        topBar.add(maximizeButton);
        topBar.add(closeButton);

        windowFrame.add(topBar);
        windowFrame.add(leftPart);
        windowFrame.add(rightPart);
        windowFrame.add(centerCardList);
        windowFrame.add(countSnippets);
        windowFrame.add(searchBar);
        windowFrame.add(addSnippets);

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
                HomeView.this.setExtendedState(JFrame.ICONIFIED);
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
                Icon icon = new ImageIcon(ResourcesUtils.getResource("/img/closeEnter.png", "closeEnter", ".png").getAbsolutePath());
                closeButton.setIcon(icon);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                HomeView.this.dispose();
                if (!isHideOnTray) {
                    systemTray();
                    isHideOnTray = true;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                closeButton.setIcon(icon3);
            }
        });
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                Icon icon1 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/minimize.png", "minimize", ".png")).getAbsolutePath());
                minimizeButton.setIcon(icon1);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                Icon icon2 = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/close.png", "close", ".png")).getAbsolutePath());
                closeButton.setIcon(icon2);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

        });

        // JFrame 设置
        this.add(windowFrame);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color(0, 0, 0, 0));

        //实现无边框拖拽界面
        //监听最初位置
        this.addMouseListener(new MouseAdapter() {    //给JFrame窗体添加一个鼠标监听
            @Override
            public void mousePressed(MouseEvent e) {     //鼠标点击时记录一下初始位置
                isDraging = true;
                xx = e.getX();
                yy = e.getY();
            }

            public void mouseReleased(MouseEvent e) {  //鼠标松开时
                isDraging = false;
            }
        });
        //时刻更新鼠标位置
        this.addMouseMotionListener(new MouseMotionAdapter() { //添加指定的鼠标移动侦听器，以接收发自此组件的鼠标移动事件。如果侦听器 l 为 null，则不会抛                                                         出异常并且不执行动作。
            public void mouseDragged(MouseEvent e) {
                //修改位置
                if (isDraging) {                                //只要鼠标是点击的（isDraging），就时刻更改窗体的位置
                    int left = getLocation().x;
                    int top = getLocation().y;
                    setLocation(left + e.getX() - xx, top + e.getY() - yy);

                }
            }
        });

        // 默认选择全部代码片段
        updateCenterListView(-1);
        systemTray();
    }

    public String listToJSONStr(List<CodeSnippets> codeSnippetsList) {
        return JSONArray.fromObject(codeSnippetsList).toString();
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
        String action = e.getActionCommand();
        if (action.equals("查找")) {
            FindDialogDoc find = new FindDialogDoc(this, true);
            find.showDialog();
        }
    }

    // This helper function updates the status bar with the line number and column
// number.
    private void updateStatus(int linenumber, int columnnumber) {
        status.setText("行: " + linenumber + " 列: " + columnnumber);
    }

    /**
     * 把 JSON 字符串写入本地
     *
     * @param jsonText
     * @param filePath
     */
    public void writeJSON2Loacl(String jsonText, String filePath) {
        try {
            File file = new File(filePath);
            // 创建上级目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果文件存在，则删除文件
            if (file.exists()) {
                file.delete();
            }
            // 创建文件
            file.createNewFile();
            // 写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            write.write(jsonText);
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新代码文本信息
     */
    public static void updateCodeInfo(int groupIndex) {
        String snippetsDes;
        String language;
        CodeSnippets temp;
        if (groupListView.jList.getSelectedIndex() == -1) {
            temp = GlobalKeyListener.loadList.get(groupIndex);
            snippetsDes = temp.getDescription();
            language = temp.getLanguageType();
            if (language.equals("")) {
                language = "未配置";
            }
        } else {
            temp = selectedGroupSnippets.get(groupIndex);
            snippetsDes = temp.getDescription();
            language = temp.getLanguageType();
            if (language.equals("")) {
                language = "未配置";
            }
        }
        title.setText(snippetsDes);
        languageText.setText(language);
        textArea.setText(temp.getContent());
    }

    /**
     * 更新中间的list
     */
    public static void updateCenterListView(int groupIndex) {
        // 分组展示的数组
        List<CodeSnippets> codeSnippetsList = new ArrayList<>();
        if (groupIndex == -1) {
            DefaultListModel<CodeSnippets> jListModel = new DefaultListModel<>();
            for (CodeSnippets temp : GlobalKeyListener.loadList) {
                jListModel.addElement(temp);
            }
            groupListView.jList.removeSelectionInterval(0, groupNameList.size());
            numberOfSnippets.setText(GlobalKeyListener.loadList.size() + "个片段");
            centerCardList.jList.setModel(jListModel);
        } else {
            for (CodeSnippets codeSnippets : GlobalKeyListener.loadList) {
                if (groupNameList.get(groupIndex).equals(codeSnippets.getGroup())) {
                    codeSnippetsList.add(codeSnippets);
                    selectedGroupSnippets = codeSnippetsList;
                }
            }
            DefaultListModel<CodeSnippets> jListModel = new DefaultListModel<>();
            for (CodeSnippets temp : codeSnippetsList) {
                jListModel.addElement(temp);
            }
            centerCardList.jList.setModel(jListModel);
            numberOfSnippets.setText(codeSnippetsList.size() + "个片段");
            GlobalKeyListener.nowShowList = codeSnippetsList;
        }
    }

    /**
     * 更新左边分组的list
     */
    public static void updateGroupListView() {
        initGroupsOfCodeSnippets();
        // 分组展示的数组
        List<String> groupList = groupNameList;
        DefaultListModel<String> jListModel = new DefaultListModel<>();
        for (String temp : groupList) {
            jListModel.addElement(temp);
        }
        groupListView.jList.setModel(jListModel);
    }

    /**
     * 获取代码片段分组
     */
    public static void initGroupsOfCodeSnippets() {
        Set<String> tempSet = new HashSet<>();
        for (CodeSnippets temp : GlobalKeyListener.loadList) {
            String group = temp.getGroup();
            if (!group.equals("")) {
                tempSet.add(group);
            }
        }
        groupNameList = new ArrayList<>(tempSet);
    }

    /**
     * 处理系统托盘
     */
    private void systemTray() {
        if (SystemTray.isSupported()) { // 判断系统是否支持托盘功能.
            // 创建托盘右击弹出菜单
            PopupMenu popupMenu = new PopupMenu();

            //创建弹出菜单中的退出项
            MenuItem itemExit = new MenuItem("退出程序");
            MenuItem aboutMe = new MenuItem("关于");
            MenuItem showMainActivity = new MenuItem("显示主程序");
            MenuItem update = new MenuItem("更新");
            itemExit.addActionListener(e -> System.exit(0));
            showMainActivity.addActionListener(e -> HomeView.this.setVisible(true));
            aboutMe.addActionListener(e -> System.out.println("MarkGosling yyds"));
            update.addActionListener(e->{
                new UpdateView();
            });
            Font f = new Font("黑体", Font.PLAIN, 11);//宋体
            itemExit.setFont(f);
            popupMenu.add(showMainActivity);
            popupMenu.add(update);
            popupMenu.add(aboutMe);
            popupMenu.add(itemExit);
            //创建托盘图标
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/code.png", "codesnippets", ".png")).getAbsolutePath()); // 创建图片对象

            TrayIcon trayIcon = new TrayIcon(icon.getImage(), "Code Bar",
                    popupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> HomeView.this.setVisible(true));

            //把托盘图标添加到系统托盘
            //这个可以点击关闭之后再放到托盘里面，在此是打开程序直接显示托盘图标了
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }
}














