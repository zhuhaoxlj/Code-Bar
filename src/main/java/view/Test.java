package view;

/**
 * @author MarkGosling
 * @date 2022/2/15 13:03
 */
public class Test {
    public static void main(String[] args) {
        enum INTENTCODE {
            PROFILE(1),
            NICKNAME(2);

            INTENTCODE(int i) {
            }
        }
        System.out.println(INTENTCODE.PROFILE);
    }
}
