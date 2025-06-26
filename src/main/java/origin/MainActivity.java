package origin;

import view.HomeView;
import view.RelaxView;

public class MainActivity {
    public static HomeView homeView;
    private static GlobalKeyListener globalKeyListener;
    
    public static void main(String[] args) {
        // 全局按键监听类
        globalKeyListener = new GlobalKeyListener();
        
        // Register shutdown hook to clean up resources
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (globalKeyListener != null) {
                globalKeyListener.clearKeyEvent();
                globalKeyListener.clearKey();
                System.out.println("Cleaned up hotkey resources");
            }
        }));
        
        // GUI
        homeView = new HomeView();
        
        // 定时休息
        RelaxView relaxView = new RelaxView();
    }
    
    // Getter for globalKeyListener
    public static GlobalKeyListener getGlobalKeyListener() {
        return globalKeyListener;
    }
}
