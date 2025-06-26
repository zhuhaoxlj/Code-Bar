package origin;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.ImageIcon;

import Snippets.CodeSnippets;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import net.sf.json.JSONArray;

import static view.AddSnippetsView.readFile2String;

public class GlobalKeyListener {
    private SearchView searchView;
    private Robot robot;
    private HotkeyListener hotkeyListener;
    private boolean isWindows = false;
    private boolean isMac = false;
    private SystemTray tray = null;
    private TrayIcon trayIcon = null;
    private Map<Integer, javax.swing.KeyStroke> keyMap = new HashMap<>();
    public static List<CodeSnippets> loadList;
    public static List<CodeSnippets> nowShowList;

    public GlobalKeyListener() {
        loadList = new ArrayList<>();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        String osName = System.getProperty("os.name").toLowerCase();
        isWindows = osName.contains("win");
        isMac = osName.contains("mac");

        if (isWindows) {
            // 在Windows上使用JIntellitype
            try {
                this.addKey();
                this.addKeyEvent();
            } catch (UnsatisfiedLinkError e) {
                System.out.println("JIntellitype not available on this platform: " + e.getMessage());
                setupTrayMenu();
            }
        } else {
            // 在Mac/Linux上使用系统托盘
            setupTrayMenu();
        }

        loadList = convertJSON2CodeSnippets(readFile2String("./CodeSnippets.json"));
        nowShowList = loadList;
    }

    public static String readFile2String(String filePath) {
        StringBuilder fileContent = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath), StandardCharsets.UTF_8));
            String linestr;
            while ((linestr = br.readLine()) != null) {
                fileContent.append(linestr);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("文件操作失败");
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    private void setupTrayMenu() {
        if (SystemTray.isSupported()) {
            try {
                tray = SystemTray.getSystemTray();

                // 创建一个popup菜单
                PopupMenu popup = new PopupMenu();

                // 使用默认应用图标或者从资源加载
                Image image = null;
                File iconFile = new File("src/main/resources/img/codeIcon.png");
                if (iconFile.exists()) {
                    image = new ImageIcon(iconFile.getAbsolutePath()).getImage();
                }

                // 如果没有找到图标，使用默认图标
                if (image == null) {
                    image = new ImageIcon(getClass().getResource("/img/codeIcon.png")).getImage();
                }

                // 创建托盘图标
                trayIcon = new TrayIcon(image, "Code-Bar");
                trayIcon.setImageAutoSize(true);

                // 添加菜单项
                MenuItem searchItem = new MenuItem("打开代码片段搜索 (Ctrl+Space)");
                MenuItem showItem = new MenuItem("显示主界面 (Ctrl+Alt+Enter)");
                MenuItem hideItem = new MenuItem("隐藏主界面 (Escape)");
                MenuItem exitItem = new MenuItem("退出");

                // 搜索代码片段菜单
                searchItem.addActionListener(e -> {
                    try {
                        loadList = convertJSON2CodeSnippets(readFile2String("./CodeSnippets.json"));
                        if (searchView != null) {
                            searchView.showView();
                        } else {
                            searchView = new SearchView();
                        }
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                });

                // 显示主界面菜单
                showItem.addActionListener(e -> {
                    if (MainActivity.homeView != null) {
                        MainActivity.homeView.setVisible(true);
                    }
                });

                // 隐藏主界面菜单
                hideItem.addActionListener(e -> {
                    if (MainActivity.homeView != null) {
                        MainActivity.homeView.setVisible(false);
                    }
                });

                // 退出应用菜单
                exitItem.addActionListener(e -> {
                    System.exit(0);
                });

                // 添加菜单项到弹出菜单
                popup.add(searchItem);
                popup.add(showItem);
                popup.add(hideItem);
                popup.addSeparator();
                popup.add(exitItem);

                // 设置托盘图标的弹出菜单
                trayIcon.setPopupMenu(popup);

                // 添加托盘图标到系统托盘
                tray.add(trayIcon);

                // 显示通知
                trayIcon.displayMessage("Code-Bar 已启动",
                        "使用系统托盘菜单或按下热键来使用应用功能。",
                        TrayIcon.MessageType.INFO);

            } catch (Exception e) {
                System.out.println("无法设置系统托盘: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("系统托盘不支持");
        }
    }

    public List<CodeSnippets> jsonStrToList(String jsonString) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        return (List<CodeSnippets>) JSONArray.toList(jsonArray, CodeSnippets.class);
    }

    public ArrayList<CodeSnippets> convertJSON2CodeSnippets(String jsonContent) {
        return (ArrayList<CodeSnippets>) jsonStrToList(jsonContent);
    }

    // Original Windows-specific methods
    public void addKey() {
        if (!isWindows) return;
        try {
            JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL, 32);
            JIntellitype.getInstance().registerHotKey(3, 0, 27);
            JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 13);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JIntellitype not available on this platform: " + e.getMessage());
        }
    }

    public void clearKey() {
        if (!isWindows) return;
        try {
            JIntellitype.getInstance().unregisterHotKey(1);
            JIntellitype.getInstance().unregisterHotKey(2);
            JIntellitype.getInstance().unregisterHotKey(3);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JIntellitype not available on this platform: " + e.getMessage());
        }
    }

    public void clearKeyEvent() {
        if (!isWindows) {
            if (tray != null && trayIcon != null) {
                tray.remove(trayIcon);
            }
            return;
        }

        try {
            JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JIntellitype not available on this platform: " + e.getMessage());
        }
    }

    public void addKeyEvent() {
        if (!isWindows) return;

        try {
            hotkeyListener = code -> {
                switch (code) {
                    case 1: {
                        System.out.println("启动软件");
                        MainActivity.homeView.setVisible(true);
                        break;
                    }
                    case 3: {
                        clearKey();
                        robot.keyPress(KeyEvent.VK_ESCAPE);
                        robot.delay(50);
                        robot.keyRelease(KeyEvent.VK_ESCAPE);
                        MainActivity.homeView.setVisible(false);
                        addKey();
                        break;
                    }
                    case 2: {
                        System.out.println("按了Ctrl+Space");
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
                    default:
                        break;
                }
            };
            JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JIntellitype not available on this platform: " + e.getMessage());
        }
    }
}