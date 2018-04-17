import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println(5_000_000 + 5);
        System.out.println(1.0e5);
        //use p in hexadecimal number instead of e, because e is a number of hexadecimal
        System.out.println(0x1.0p-5);
        System.out.println(0x1.0p-10);
        System.out.println(2.0 - 1.1);
        System.out.println(new BigDecimal(2.0).subtract(new BigDecimal(1.1)));
    }
}
