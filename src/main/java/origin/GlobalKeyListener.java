package origin;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import Snippets.CodeSnippets;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.tulskiy.keymaster.common.Provider;
import com.tulskiy.keymaster.common.HotKeyListener;
import javax.swing.KeyStroke;
import net.sf.json.JSONArray;
import view.HomeView;

import static view.AddSnippetsView.readFile2String;

public class GlobalKeyListener {
    private SearchView searchView;
    private Robot robot;
    private HotkeyListener hotkeyListener;
    private Provider provider;
    private boolean isWindows = false;
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

        if (isWindows) {
            this.addKey();
            this.addKeyEvent();
        } else {
            // Use jkeymaster for Mac/Linux
            initializeCrossPlatformHotKeys();
        }
        
        loadList = convertJSON2CodeSnippets(readFile2String("./CodeSnippets.json"));
        nowShowList = loadList;
    }

    private void initializeCrossPlatformHotKeys() {
        provider = Provider.getCurrentProvider(true);

        // Register Ctrl+Space
        provider.register(KeyStroke.getKeyStroke("control SPACE"), hotKey -> {
            System.out.println("Pressed Ctrl+Space");
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
        });

        // Register Ctrl+Alt+Enter
        provider.register(KeyStroke.getKeyStroke("control alt ENTER"), hotKey -> {
            System.out.println("启动软件");
            MainActivity.homeView.setVisible(true);
        });

        // Register Escape key
        provider.register(KeyStroke.getKeyStroke("ESCAPE"), hotKey -> {
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            MainActivity.homeView.setVisible(false);
        });
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
            System.out.println("JIntellitype not available on this platform");
        }
    }

    public void clearKey() {
        if (!isWindows) return;
        try {
            JIntellitype.getInstance().unregisterHotKey(1);
            JIntellitype.getInstance().unregisterHotKey(2);
            JIntellitype.getInstance().unregisterHotKey(3);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JIntellitype not available on this platform");
        }
    }

    public void clearKeyEvent() {
        if (!isWindows) {
            if (provider != null) {
                provider.reset();
                provider.stop();
            }
            return;
        }
        
        try {
            JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JIntellitype not available on this platform");
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
            System.out.println("JIntellitype not available on this platform");
        }
    }
}