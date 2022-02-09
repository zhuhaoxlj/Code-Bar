package origin;

import view.HomeView;

public class MainActivity {
    public static HomeView homeView;

    public static void main(String[] args) {
        // 全局按键监听类
        GlobalKeyListener globalKeyListener = new GlobalKeyListener();
        // GUI
        HomeView homeView = new HomeView();
    }
}
