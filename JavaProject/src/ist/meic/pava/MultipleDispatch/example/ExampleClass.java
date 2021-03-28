package ist.meic.pava.MultipleDispatch.example;

import ist.meic.pava.MultipleDispatch.UsingMultipleDispatch;

public class ExampleClass {

    public static void example1() {
        Device[] devices = new Device[] { new Screen(), new Printer() };
        Shape[] shapes = new Shape[] { new Line(), new Circle() };
        for (Device device : devices) {
            for (Shape shape : shapes) {
                // device.draw(shape);
                UsingMultipleDispatch.invoke(device, "draw", shape);
            }
        }
    }

    public static void example2() {
        Device[] devices = new Device[] { new Screen(), new Printer() };
        Shape[] shapes = new Shape[] { new Line(), new Circle() };
        Brush[] brushes = new Brush[] { new Pencil(), new Crayon() };
        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Brush brush : brushes) {
                    // device.draw(shape, brush);
                    // UsingMultipleDispatch.invoke(device, "draw", shape, brush);
                }
            }
        }
    }
}
