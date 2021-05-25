import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        Method[] methods = test.getClass().getMethods();
        methods[0].getName();
        methods[0].getParameterTypes();
        Math.class.getMethods();
        // Class.forName("java.lang.Math");
    }

    public void test(int ola) {

    }
}