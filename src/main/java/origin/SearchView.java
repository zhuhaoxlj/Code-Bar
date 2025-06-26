package origin;

import Snippets.CodeSnippets;
import net.sf.json.JSONArray;
import viewUtils.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchView {
    private final JTextField jTextField;
    private final JFrame jFrame;
    private Robot robot;
    private List<CodeSnippets> codeSnippetsList;
    private JList<String> codeSnippetsJList;
    private boolean upFlag = true;
    List<String> searchList = new ArrayList<>();
    JPanel panel;
    String[] objectList = new String[0];
    boolean isZH = false;
    List<CodeSnippets> codeSnippetsList1;

    public SearchView() throws AWTException {
//        codeSnippetsList1 = convertJSON2CodeSnippets(readFile2String(Objects.requireNonNull(ResourcesUtils.getResource("/codesnippets/MySnippets.json", "MySnippets", ".json")).getAbsolutePath()));
        codeSnippetsList1 = GlobalKeyListener.loadList;
        codeSnippetsList = codeSnippetsList1;
        jFrame = new JFrame();
        // 失去边框和标题栏的修饰
        jFrame.setUndecorated(true);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        jFrame.setSize(700, 60);
        int x = (int) ((width - jFrame.getWidth()) / 2);
        int y = (int) (height - jFrame.getHeight()) / 2;
        jFrame.setLocation(x, y);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel(null);
        jTextField = new JTextField(20);
        jTextField.setBounds(0, 0, 700, 60);
        jTextField.setBorder(Style.nullBorder1);
        jTextField.setFont(new Font("Microsoft YaHei", Font.BOLD, 45));
        panel.add(jTextField);
        jFrame.getContentPane().add(panel);

        // 托盘程序
        ImageIcon imageIcon = new ImageIcon("");
        TrayIcon trayIcon = new TrayIcon(imageIcon.getImage());
        trayIcon.setToolTip("系统托盘");
        PopupMenu popMenu = new PopupMenu();      //创建弹出菜单


        // 输入框焦点监听
        jTextField.addFocusListener(new FocusAdapter() {
            final Robot robot = new Robot();

            @Override
            public void focusGained(FocusEvent e) {
                if (!isZH) {
                    // 切换输入法
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    isZH = true;
                }
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jTextField.getText().equals("")) {
                    hidePanel();
                }
                if (upFlag) {
                    hidePanel();
                }
                super.focusLost(e);
            }
        });
        // 输入监听
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // 如果输入的是字母和退格键
                if (isLetter(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE || isChinese(jTextField.getText())) {
                    searchCode();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    hidePanel();
                }

                drawList(objectList);
                if (e.getKeyCode() == KeyEvent.VK_DOWN && !jTextField.getText().equals("")) {

                    if (codeSnippetsJList != null) {
                        panel.remove(codeSnippetsJList);
                    }

                    codeSnippetsJList = new JList<>(objectList);
                    codeSnippetsJList.setVisibleRowCount(5);
                    codeSnippetsJList.setFont(new Font("Microsoft YaHei", Font.BOLD, 35));
                    codeSnippetsJList.setBounds(0, 60, 700, 51 * searchList.size());

                    codeSnippetsJList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            upFlag = false;
                            super.mouseEntered(e);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            upFlag = true;
                            super.mouseExited(e);
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            String content = getCodeByDes(objectList[codeSnippetsJList.getSelectedIndex()]);
                            if (!content.equals("")) {
                                pasteCode(content);
                            }
                            super.mouseClicked(e);
                        }
                    });
                    codeSnippetsJList.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            int keyCode = e.getKeyCode();
                            if (keyCode == KeyEvent.VK_ESCAPE) {
                                hidePanel();
                            }
                            char inputChar = e.getKeyChar();
                            if (isLetter(inputChar)) {
                                jTextField.setText(jTextField.getText() + inputChar);
                                searchCode();
                                drawList(objectList);
                            }
                            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                                String content = jTextField.getText();
                                jTextField.setText(content.substring(0, Math.max((content.length() - 1), 0)));
                                searchCode();
                                drawList(objectList);
                                return;
                            }
                            if (!jTextField.getText().equals("")) {
                                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                                    System.out.println("下");
                                    if (keyCode == KeyEvent.VK_UP && upFlag) {
                                        jTextField.requestFocus();
                                        codeSnippetsJList.setBounds(0, 60, 700, 60);
                                        panel.remove(codeSnippetsJList);
                                        jFrame.paint(jFrame.getGraphics());
                                    }
                                    if (keyCode == KeyEvent.VK_UP && codeSnippetsJList.getSelectedIndex() == 0) {
                                        upFlag = true;
                                    }
                                    if (keyCode != KeyEvent.VK_UP && keyCode != KeyEvent.VK_DOWN) {
                                        jTextField.requestFocus();
                                        try {
                                            robot = new Robot();
                                        } catch (AWTException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    if (keyCode == KeyEvent.VK_ENTER) {
                                        System.out.println(codeSnippetsJList.getSelectedIndex() + 1 + "个被选择");
                                    }
                                }
                            }
                            if (keyCode == KeyEvent.VK_ENTER) {
                                String content = getCodeByDes(objectList[codeSnippetsJList.getSelectedIndex()]);
                                if (!content.equals("")) {
                                    pasteCode(content);
                                }
                            }
                            super.keyReleased(e);
                        }
                    });
                    jFrame.setSize(700, 60 + 51 * searchList.size());
                    panel.add(codeSnippetsJList);
                    panel.paint(panel.getGraphics());
                    upFlag = false;
                    codeSnippetsJList.grabFocus();
                    codeSnippetsJList.setSelectedIndex(0);
                }
                String inputText = jTextField.getText();
                for (CodeSnippets codeSnippets : codeSnippetsList) {
                    if (inputText.equals(codeSnippets.getPrefix()) && !codeSnippets.getPrefix().equals("")) {
                        pasteCode(codeSnippets.getContent());
                    }
                }
                super.keyReleased(e);
            }
        });
        jFrame.setVisible(true);
    }

    public boolean isChinese(String text) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(text);
        return m.find();
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
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonText);
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showView() {
        jFrame.setVisible(true);
        codeSnippetsList1 = GlobalKeyListener.loadList;
        codeSnippetsList = codeSnippetsList1;
    }

    public void searchCode() {
        searchList = new ArrayList<>();
        // 搜索
        String searchString = jTextField.getText();
        System.out.println(searchString);
        if (!searchString.equals("")) {
            for (CodeSnippets codeSnippets : codeSnippetsList) {
                String tempPrefix = codeSnippets.getPrefix();
                String description = codeSnippets.getDescription();
                if (tempPrefix.contains(searchString) || tempPrefix.contains(searchString.toUpperCase(Locale.ROOT)) || description.contains(searchString) || description.contains(searchString.toUpperCase(Locale.ROOT))) {
                    System.out.println(codeSnippetsList.indexOf(codeSnippets) + ":" + tempPrefix);
                    searchList.add(description);
                }
            }
        }
        objectList = searchList.toArray(new String[0]);
    }

    public boolean isLetter(char tmpChar) {
        return (tmpChar - 'a') >= 0 && ('z' - tmpChar) >= 0 || (tmpChar - 'A') >= 0 && ('Z' - tmpChar) >= 0;
    }

    public ArrayList<CodeSnippets> convertJSON2CodeSnippets(String jsonContent) {
        return (ArrayList<CodeSnippets>) jsonStrToList(jsonContent);
    }

    public void drawListFocusOnSearchBar(String[] objectList) {
        if (codeSnippetsJList != null) {
            panel.remove(codeSnippetsJList);
        }

        codeSnippetsJList = new JList<>(objectList);
        codeSnippetsJList.setVisibleRowCount(5);
        codeSnippetsJList.setFont(new Font("Microsoft YaHei", Font.BOLD, 35));
        codeSnippetsJList.setBounds(0, 60, 700, 51 * searchList.size());
        codeSnippetsJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                upFlag = false;
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                upFlag = true;
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                String content = getCodeByDes(objectList[codeSnippetsJList.getSelectedIndex()]);
                if (!content.equals("")) {
                    pasteCode(content);
                }
                super.mouseClicked(e);
            }
        });
        codeSnippetsJList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    hidePanel();
                }
                // 可以向下
                if (keyCode == KeyEvent.VK_UP && upFlag) {
                    jTextField.requestFocus();
                    codeSnippetsJList.setBounds(0, 60, 700, 51 * searchList.size());
                    panel.remove(codeSnippetsJList);
                    jFrame.paint(jFrame.getGraphics());
                }
                // 添加可以向下标志
                if (keyCode == KeyEvent.VK_UP && codeSnippetsJList.getSelectedIndex() == 0) {
                    upFlag = true;
                }
                if (keyCode != KeyEvent.VK_UP && keyCode != KeyEvent.VK_DOWN) {
                    jTextField.requestFocus();
                    char inputChar = e.getKeyChar();
                    if (isLetter(inputChar)) {
                        jTextField.setText(jTextField.getText() + inputChar);
                    }
                    try {
                        robot = new Robot();
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    System.out.println(codeSnippetsJList.getSelectedIndex() + 1 + "个被选择");
                }
                super.keyReleased(e);
            }
        });
        jFrame.setSize(700, 60 + 51 * searchList.size());
        panel.add(codeSnippetsJList);
        panel.paint(panel.getGraphics());
        jTextField.grabFocus();
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    public List<CodeSnippets> jsonStrToList(String jsonString) {
        List<CodeSnippets> codeSnippetsList = (List<CodeSnippets>) JSONArray.toList(JSONArray.fromObject(jsonString), CodeSnippets.class);
        return codeSnippetsList;
    }

    public void drawList(String[] objectList) {
        if (codeSnippetsJList != null) {
            panel.remove(codeSnippetsJList);
        }

        codeSnippetsJList = new JList<>(objectList);
        codeSnippetsJList.setVisibleRowCount(5);
        codeSnippetsJList.setFont(new Font("Microsoft YaHei", Font.BOLD, 35));
        codeSnippetsJList.setBounds(0, 60, 700, 51 * searchList.size());
        codeSnippetsJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                upFlag = false;
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                upFlag = true;
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                String content = getCodeByDes(objectList[codeSnippetsJList.getSelectedIndex()]);
                if (!content.equals("")) {
                    pasteCode(content);
                }
                super.mouseClicked(e);
            }
        });
        codeSnippetsJList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    hidePanel();
                }
                // 可以向下
                if (keyCode == KeyEvent.VK_UP && upFlag) {
                    jTextField.requestFocus();
                    codeSnippetsJList.setBounds(0, 60, 700, 51 * searchList.size());
                    panel.remove(codeSnippetsJList);
                    jFrame.paint(jFrame.getGraphics());
                }
                // 添加可以向下标志
                if (keyCode == KeyEvent.VK_UP && codeSnippetsJList.getSelectedIndex() == 0) {
                    upFlag = true;
                }
                if (keyCode != KeyEvent.VK_UP && keyCode != KeyEvent.VK_DOWN) {
                    jTextField.requestFocus();
                    char inputChar = e.getKeyChar();
                    if (isLetter(inputChar)) {
                        jTextField.setText(jTextField.getText() + inputChar);
                    }
                    try {
                        robot = new Robot();
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    System.out.println(codeSnippetsJList.getSelectedIndex() + 1 + "个被选择");
                }
                super.keyReleased(e);
            }
        });
        jFrame.setSize(700, 60 + 51 * searchList.size());
        panel.add(codeSnippetsJList);
        panel.paint(panel.getGraphics());
    }

    /**
     * 将文件读取到字符串
     *
     * @param filePath 文件路径
     * @return String 返回文件字符串内容
     */
    public static String readFile2String(String filePath) {
        String fileContent = "";
        //文件逐行读入
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath)));
            String linestr;//按行读取 将每次读取一行的结果赋值给linestr
            while ((linestr = br.readLine()) != null) {
                fileContent = fileContent + linestr;
            }
            br.close();//关闭IO
        } catch (Exception e) {
            System.out.println("文件操作失败");
            e.printStackTrace();
        }
        return fileContent;
    }

    public String getCodeByPrefix(String prefix) {
        for (CodeSnippets codeSnippets : codeSnippetsList) {
            if (codeSnippets.getPrefix().equals(prefix)) {
                return codeSnippets.getContent();
            }
        }
        return "";
    }

    public String getCodeByDes(String des) {
        for (CodeSnippets codeSnippets : codeSnippetsList) {
            if (codeSnippets.getDescription().equals(des)) {
                return codeSnippets.getContent();
            }
        }
        return "";
    }

    public void pasteCode(String code) {
        StringSelection stringSelection = new StringSelection(code);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
        try {
            robot = new Robot();
            hidePanel();
            Thread.sleep(100);
            pressMultipleKeyByNumber(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hidePanel() {
        jTextField.setText("");
        searchCode();
        drawList(objectList);
        jFrame.hide();
    }

    /**
     * 按下组合键，如 ctrl + c、ctrl + v、alt + tab 等等
     *
     * @param keycode：组合健数组，如 {KeyEvent.VK_CONTROL,KeyEvent.VK_V}
     */
    public static void pressMultipleKeyByNumber(int... keycode) {
        try {
            Robot robot = new Robot();

            //按顺序按下健
            for (int i = 0; i < keycode.length; i++) {
                robot.keyPress(keycode[i]);
                robot.delay(50);
            }

            //按反序松开健
            for (int i = keycode.length - 1; i >= 0; i--) {
                robot.keyRelease(keycode[i]);
                robot.delay(50);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
