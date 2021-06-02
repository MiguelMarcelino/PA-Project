import java.lang.reflect.Method;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        Method[] methods = test.getClass().getMethods();
        methods[0].getName();
        methods[0].getParameterTypes();
        methods[0].getParameterTypes();
        Math.class.getMethods();
        LocalDate l = LocalDate.now(Clock.systemUTC());
        LocalDate.of(2002, 12, 11);
        Date d = new Date();
        d.toString();
        d.getDate();
        d.getTime();
        d.getMinutes();
        
        // Class.forName("java.lang.Math");
    }

    public Test(String test) {
        System.out.println("This is a test: " + test);
    }

    public void test(int testInteger) {
        System.out.println("This is a test integer: " + testInteger);
    }
}