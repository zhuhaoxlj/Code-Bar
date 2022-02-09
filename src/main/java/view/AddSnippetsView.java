package view;

import Snippets.CodeSnippets;
import net.sf.json.JSONArray;
import viewUtils.ResourcesUtils;
import viewUtils.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddSnippetsView extends JFrame {
    public boolean isDraging;
    private int xx;
    private int yy;

    public AddSnippetsView() {
        JLabel jframe = new JLabel();
        Icon jframeIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/jframe.png", "jframe", ".png")).getAbsolutePath());
        jframe.setIcon(jframeIcon);

        // 窗体控制按钮
        JLabel minimizeButton = new JLabel();
        JLabel maximizeButton = new JLabel();
        JLabel closeButton = new JLabel();
        minimizeButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/minimize.png", "jframe", ".png")).getAbsolutePath()));
        maximizeButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/maximize.png", "jframe", ".png")).getAbsolutePath()));
        closeButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/close.png", "jframe", ".png")).getAbsolutePath()));
        minimizeButton.setBounds(751, 24, 21, 21);
        maximizeButton.setBounds(785, 24, 21, 21);
        closeButton.setBounds(819, 24, 21, 21);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddSnippetsView.this.dispose();
            }
        });

        //  灰色背景
        JLabel grayBackground = new JLabel();
        grayBackground.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/grayBackground.png", "jframe", ".png")).getAbsolutePath()));
        grayBackground.setBounds(50, 62, 760, 429);

        // 输入框
        JLabel nameInput = new JLabel();
        nameInput.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/nameBg.png", "jframe", ".png")).getAbsolutePath()));
        nameInput.setBounds(315, 36, 201, 32);
        JTextField nameJF = new JTextField();
        nameJF.setFont(new Font("黑体", Font.PLAIN, 20));
        nameJF.setBorder(Style.nullBorder);
        nameJF.setBounds(5, 0, 190, 30);
        nameInput.add(nameJF);
        JLabel name = new JLabel("名称:");
        name.setFont(new Font("黑体", Font.PLAIN, 20));
        name.setBounds(225, 26, 80, 50);
        grayBackground.add(name);
        grayBackground.add(nameInput);
        JLabel describeInput = new JLabel();
        describeInput.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/describeBg.png", "jframe", ".png")).getAbsolutePath()));
        describeInput.setBounds(315, 84, 287, 59);
        JLabel describe = new JLabel("内容:");
        describe.setFont(new Font("黑体", Font.PLAIN, 20));
        describe.setBounds(225, 79, 80, 50);
        JTextArea describeJT = new JTextArea();
        describeJT.setBorder(Style.nullBorder);
        describeJT.setBounds(0, 0, 290, 60);
        describeJT.setLineWrap(true);
        describeInput.add(describeJT);
        grayBackground.add(describe);
        grayBackground.add(describeInput);
        JLabel groupInput = new JLabel();
        groupInput.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/groupBg.png", "jframe", ".png")).getAbsolutePath()));
        groupInput.setBounds(315, 158, 201, 32);
        JTextField groupJF = new JTextField();
        groupJF.setFont(new Font("黑体", Font.PLAIN, 20));
        groupJF.setBorder(Style.nullBorder);
        groupJF.setBounds(5, 0, 190, 30);
        groupInput.add(groupJF);
        JLabel group = new JLabel("分组:");
        group.setFont(new Font("黑体", Font.PLAIN, 20));
        group.setBounds(225, 148, 80, 50);
        grayBackground.add(group);
        grayBackground.add(groupInput);
        JLabel commandInput = new JLabel();
        commandInput.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/commandBg.png", "jframe", ".png")).getAbsolutePath()));
        commandInput.setBounds(315, 216, 201, 32);
        JLabel command = new JLabel("指令:");
        command.setFont(new Font("黑体", Font.PLAIN, 20));
        command.setBounds(225, 206, 80, 50);
        JTextField commandJF = new JTextField();
        commandJF.setFont(new Font("黑体", Font.PLAIN, 20));
        commandJF.setBorder(Style.nullBorder);
        commandJF.setBounds(5, 0, 190, 30);
        commandInput.add(commandJF);
        grayBackground.add(command);
        grayBackground.add(commandInput);
        JLabel typeInput = new JLabel();
        typeInput.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/typeBg.png", "jframe", ".png")).getAbsolutePath()));
        typeInput.setBounds(315, 289, 201, 32);
        JLabel type = new JLabel("片段类型");
        type.setFont(new Font("黑体", Font.PLAIN, 20));
        type.setBounds(189, 278, 200, 50);
        grayBackground.add(type);
        grayBackground.add(typeInput);
        // 添加窗体控制按钮
        jframe.add(minimizeButton);
        jframe.add(maximizeButton);
        jframe.add(closeButton);
        // 添加灰色背景
        jframe.add(grayBackground);
        // 添加标题
        JLabel title = new JLabel();
        Icon titleIcon = new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsText.png", "right", ".png")).getAbsolutePath());
        title.setIcon(titleIcon);
        title.setBounds(376, 21, 108, 21);
        // 添加底部按钮
        JLabel cancelButton = new JLabel();
        cancelButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/cancelButton.png", "jframe", ".png")).getAbsolutePath()));
        cancelButton.setBounds(253, 510, 120, 58);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddSnippetsView.this.dispose();
            }
        });
        jframe.add(cancelButton);
        jframe.add(title);
        JLabel createButton = new JLabel();
        createButton.setIcon(new ImageIcon(Objects.requireNonNull(ResourcesUtils.getResource("/img/addSnippetsView/createButton.png", "jframe", ".png")).getAbsolutePath()));
        createButton.setBounds(510, 510, 120, 58);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AddSnippetsView.this.dispose();
            }
        });
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String name = nameJF.getText();
                String content = describeJT.getText();
                String group = groupJF.getText();
                String prefix = commandJF.getText();
                CodeSnippets codeSnippets = new CodeSnippets();
                codeSnippets.setDescription(name);
                codeSnippets.setContent(content);
                codeSnippets.setPrefix(prefix);
                codeSnippets.setGroup(group);
                ArrayList<CodeSnippets> codeSnippetsArrayList;
                codeSnippetsArrayList = convertJSON2CodeSnippets(readFile2String("./CodeSnippets.json"));
                codeSnippetsArrayList.add(codeSnippets);
//                writeJSON2Loacl(listToJSONStr(codeSnippetsArrayList), Objects.requireNonNull(ResourcesUtils.getResource("/codesnippets/CodeSnippets.json", "MySnippets", ".json")).getAbsolutePath());
                writeJSON2Loacl(listToJSONStr(codeSnippetsArrayList), "./CodeSnippets.json");
                AddSnippetsView.this.dispose();
            }
        });
        jframe.add(createButton);
        this.setSize(861, 587);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color(0, 0, 0, 0));

        // 添加窗体框架
        this.add(jframe);

        this.setVisible(true);

        // 失去焦点就关闭
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                AddSnippetsView.this.dispose();
            }
        });

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
    }

    public ArrayList<CodeSnippets> convertJSON2CodeSnippets(String jsonContent) {
        return (ArrayList<CodeSnippets>) jsonStrToList(jsonContent);
    }

    public List<CodeSnippets> jsonStrToList(String jsonString) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        List<CodeSnippets> codeSnippetsList = JSONArray.toList(jsonArray, CodeSnippets.class);
        return codeSnippetsList;
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

    public String listToJSONStr(List<CodeSnippets> codeSnippetsList) {
        return JSONArray.fromObject(codeSnippetsList).toString();
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
}
