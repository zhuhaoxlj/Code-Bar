package origin;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Test {
    public static void main(String[] args) throws InterruptedException, AWTException {
        Robot robot = new Robot();
        Thread.sleep(100);
        System.out.println("我滴任务完成了!");
        pressMultipleKeyByNumber(KeyEvent.VK_WINDOWS, KeyEvent.VK_D);
        System.out.println("我按完了!");
    }

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
