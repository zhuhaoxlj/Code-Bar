package origin;

import view.HomeView;
import view.RelaxView;

public class MainActivity {
    public static HomeView homeView;
    public static void main(String[] args) {
        // 全局按键监听类
        GlobalKeyListener globalKeyListener = new GlobalKeyListener();
        // GUI
        HomeView homeView = new HomeView();
        // 定时休息
        RelaxView relaxView = new RelaxView();
    }
}
