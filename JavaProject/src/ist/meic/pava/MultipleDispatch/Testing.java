package ist.meic.pava.MultipleDispatch;

import ist.meic.pava.MultipleDispatch.example.ExampleClass;

public class Testing {
    public static void main(String[] args) {
        System.err.println("Starting test 1\n");
        ExampleClass.example1();
        System.err.println("\nStarting test 2\n");
        ExampleClass.example2();
    }
}
