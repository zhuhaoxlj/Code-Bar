package origin;

import Snippets.CodeSnippets;
import net.sf.json.JSONArray;
import viewUtils.ResourcesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeView {
    private final JFrame jFrame;

    public HomeView() {
        jFrame = new JFrame();
        jFrame.setTitle("Code Bar");
        jFrame.setBounds(600, 300, 500, 400);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon("./img/code.png");
        jFrame.setIconImage(imageIcon.getImage());

        JPanel panel = new JPanel();
        JTextField jTextField = new JTextField();
        jTextField.setSize(300, 300);
        panel.setLayout(null);
        JLabel userLabel = new JLabel("prefix");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField prefix = new JTextField(20);
        prefix.setBounds(100, 20, 165, 25);
        panel.add(prefix);

        // 输入密码的文本域
        JLabel codeTextLabel = new JLabel("CodeText:");
        codeTextLabel.setBounds(10, 50, 80, 25);
        panel.add(codeTextLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextArea codeText = new JTextArea();
        codeText.setBounds(100, 50, 300, 300);
        panel.add(codeText);
        ArrayList<CodeSnippets> codeSnippetsArrayList = new ArrayList<>();
        codeSnippetsArrayList = convertJSON2CodeSnippets(readFile2String(Objects.requireNonNull(ResourcesUtils.getResource("/codesnippets/MySnippets.json", "MySnippets", ".json")).getAbsolutePath()));
        CodeSnippets codeSnippets = new CodeSnippets();
        // 创建登录按钮
        JButton loginButton = new JButton("Add");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
        ArrayList<CodeSnippets> finalCodeSnippetsArrayList = codeSnippetsArrayList;
        ArrayList<CodeSnippets> finalCodeSnippetsArrayList1 = codeSnippetsArrayList;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeSnippets.setPrefix(prefix.getText());
                codeSnippets.setContent(codeText.getText());
                finalCodeSnippetsArrayList.add(codeSnippets);
                writeJSON2Loacl(listToJSONStr(finalCodeSnippetsArrayList1), Objects.requireNonNull(ResourcesUtils.getResource("/codesnippets/MySnippets.json", "MySnippets", ".json")).getAbsolutePath());
            }
        });

        jFrame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    public ArrayList<CodeSnippets> convertJSON2CodeSnippets(String jsonContent) {
        return (ArrayList<CodeSnippets>) jsonStrToList(jsonContent);
    }

    public List<CodeSnippets> jsonStrToList(String jsonString) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        List<CodeSnippets> codeSnippetsList = JSONArray.toList(jsonArray, CodeSnippets.class);
        return codeSnippetsList;
    }

    public void setVisable(Boolean isVisable) {
        jFrame.setVisible(isVisable);
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
}
