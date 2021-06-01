import java.lang.reflect.Method;
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
        LocalDate.now().plusDays(12);
        // Class.forName("java.lang.Math");
    }

    public void test(int ola) {

    }
}