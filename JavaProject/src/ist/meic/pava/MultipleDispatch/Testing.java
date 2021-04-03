package ist.meic.pava.MultipleDispatch;

import ist.meic.pava.MultipleDispatch.example.ExampleClass;
import ist.meic.pava.MultipleDispatch.example.ExampleClassExtended;

public class Testing {
    public static void main(String[] args) {
        System.err.println("Starting test 1");
        ExampleClass.example1();

        System.err.println("\nStarting test 2");
        ExampleClass.example2();

        System.err.println("\nStarting test 3");
        ExampleClass.example3();

        System.err.println("\nStarting test 4");
        ExampleClass.example4();

        System.err.println("\nStarting test 5");
        ExampleClass.example5();

        System.err.println("\n---------------------------");
        System.err.println("Extended tests");
        System.err.println("---------------------------");

        System.err.println("Test for Boxing an Unboxing");
        System.err.println("Starting extended test 1");
        ExampleClassExtended.exampleExtended1();
    }
}
