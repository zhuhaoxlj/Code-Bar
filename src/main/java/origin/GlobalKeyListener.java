package origin;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import Snippets.CodeSnippets;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import net.sf.json.JSONArray;

import static view.AddSnippetsView.readFile2String;

public class GlobalKeyListener {
    private SearchView searchView;
    private Robot robot;
    private HotkeyListener hotkeyListener;
    public static List<CodeSnippets> loadList;
    public static List<CodeSnippets> nowShowList;

    public GlobalKeyListener() {
        loadList = new ArrayList<>();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ;
        this.addKey();
        this.addKeyEvent();
        loadList = convertJSON2CodeSnippets(readFile2String("./CodeSnippets.json"));
        nowShowList = loadList;
    }

    public List<CodeSnippets> jsonStrToList(String jsonString) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        return (List<CodeSnippets>) JSONArray.toList(jsonArray, CodeSnippets.class);
    }

    public ArrayList<CodeSnippets> convertJSON2CodeSnippets(String jsonContent) {
        return (ArrayList<CodeSnippets>) jsonStrToList(jsonContent);
    }

    public void addKey() {
//        JIntellitype.getInstance().registerHotKey(3, 0, 'F');
//        JIntellitype.getInstance().registerHotKey(1, 0, 'G');
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL, 32);
    }


    public void clearKey() {
        JIntellitype.getInstance().unregisterHotKey(1);
        JIntellitype.getInstance().unregisterHotKey(2);
        JIntellitype.getInstance().unregisterHotKey(3);
    }

    public void clearKeyEvent() {
        JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
    }


    public void addKeyEvent() {
        hotkeyListener = new HotkeyListener() {
            public void onHotKey(int code) {
                switch (code) {
                    case 1: {
                        //先注销热键
                        clearKey();
                        //模拟按键原本功能
                        robot.keyPress(KeyEvent.VK_G);
                        robot.delay(50);
                        robot.keyRelease(KeyEvent.VK_G);
                        //这里写其他事件
                        System.out.println("按了G");
                        //再重新注册热键
                        addKey();
                        break;
                    }
                    case 3: {
                        //先注销热键
                        clearKey();
                        //模拟按键原本功能
                        robot.keyPress(KeyEvent.VK_F);
                        robot.delay(50);
                        robot.keyRelease(KeyEvent.VK_F);
                        //这里写其他事件
                        System.out.println("按了F");
                        //再重新注册热键
                        addKey();
                        break;
                    }
                    case 2: {
                        //这里写想实现的功能
                        System.out.println("按了Ctrl+Space");
                        // GUI
                        try {
                            loadList = convertJSON2CodeSnippets(readFile2String("./CodeSnippets.json"));
                            if (searchView != null) {
                                searchView.showView();
                            } else {
                                searchView = new SearchView();
                            }
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        };
        JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
    }
}