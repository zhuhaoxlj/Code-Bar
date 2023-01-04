package view;

/**
 * @author MarkGosling
 * @date 2022/2/15 13:03
 */
public class Test {
    public static void main(String[] args) {
        int count = 0;
        int i,j,k;
        for (i=1; i <= 10; i++) {
            for (j=1; j <= i; j++) {
                for (k=1; k <= j; k++) {
                    count += 1;
                }
            }
        }
        System.out.println(count);
    }
}
