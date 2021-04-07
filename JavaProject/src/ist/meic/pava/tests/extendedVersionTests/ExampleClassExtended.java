package ist.meic.pava.tests.extendedVersionTests;

import ist.meic.pava.MultipleDispatchExtended.UsingMultipleDispatchInterface;
import ist.meic.pava.MultipleDispatchExtended.UsingMultipleDispatchUnboxing;
import ist.meic.pava.tests.*;
import ist.meic.pava.tests.extendedVersionTests.interfaceTests.*;

public class ExampleClassExtended {

    /**
     * Test for boxing/unboxing nr.1
     */
    public static void exampleExtended1() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Integer[] ints = {1, 2};
        Integer[] primitivePos = {};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Integer intValue : ints) {
                    System.err.println("- " + device.getClass().getSimpleName() + " " +
                            shape.getClass().getSimpleName());
                    UsingMultipleDispatchUnboxing.invoke(device, "draw", primitivePos, shape, intValue);

                    // using the method with an empty array has the same results as invoking base version
                    // comment out line 29 and comment line 25 to see results (All tests should pass)
                    // UsingMultipleDispatch.invoke(device, "draw", shape, intValue);
                }
            }
        }
    }

    /**
     * Test for boxing/unboxing nr.2
     */
    public static void exampleExtended2() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Integer[] ints = {1, 2};
        Integer[] primitivePos = {1}; // now test with unboxing

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Integer intValue : ints) {
                    System.err.println("- " + device.getClass().getSimpleName() + " " +
                            shape.getClass().getSimpleName());
                    UsingMultipleDispatchUnboxing.invoke(device, "draw", primitivePos, shape, intValue);
                }
            }
        }
    }

    /**
     * Test for interfaces
     */
    public static void exampleExtended3() {
        // In class Test1, the ITest1 interface is specified first, so methods that have
        // ITest1 as arguments will be considered as the most specific methods. Same goes
        // for Test2 but with ITest2 being the most specific one
        UsingMultipleDispatchInterface.invoke(new Screen(), "draw", new Test1());
        UsingMultipleDispatchInterface.invoke(new Screen(), "draw", new Test2());
    }

}

