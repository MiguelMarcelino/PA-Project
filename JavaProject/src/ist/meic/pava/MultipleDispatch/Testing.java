package ist.meic.pava.MultipleDispatch;

import ist.meic.pava.MultipleDispatch.example.ExampleClass;

public class Testing {
    public static void main(String[] args) {
        System.err.println("Starting test 1");
        ExampleClass.example1();
        System.err.println("\nStarting test 2");
        ExampleClass.example2();
        System.err.println("\nStarting test 3");
        ExampleClass.example3();
    }
}
