package temp;

/**
 * Created by wangdexun on 2017/2/23.
 */
public class JavaSample {

    public static void main(String[] args) {
//        add(5, 6);
        System.out.println("wdx love gsj!");
    }

    public int add(int a, int b) throws InterruptedException {
        Thread.sleep(1000);
        return a * b;
    }

    public int sub(int a, int b) {
        throw new NullPointerException("wdx");
    }

}
