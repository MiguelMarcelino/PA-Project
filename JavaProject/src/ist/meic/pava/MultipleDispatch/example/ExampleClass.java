package ist.meic.pava.MultipleDispatch.example;

import ist.meic.pava.MultipleDispatch.UsingMultipleDispatch;

public class ExampleClass {

    public static void example1() {
         Device[] devices = new Device[] { new Screen(), new Printer() };
         Shape[] shapes = new Shape[] { new Line(), new Circle() };
//        Device[] devices = new Device[] { new Printer() };
//        Shape[] shapes = new Shape[] { new Line() };
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
//        Device[] devices = new Device[] { new Screen() };
//        Shape[] shapes = new Shape[] { new Circle() };
//        Brush[] brushes = new Brush[] { new Crayon() };
        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Brush brush : brushes) {
                    // device.draw(shape, brush);
                    UsingMultipleDispatch.invoke(device, "draw", shape, brush);
                }
            }
        }
    }

    public static String defaultValues() {
        String resTest1 = "Starting test 1\n" +
                "drawing a line on screen!\n" +
                "drawing a circle on screen!\n" +
                "drawing a line on printer!\n" +
                "drawing a circle on printer!";

        String resTest2 = "\nStarting test 2\n" +
                "drawing a line on screen with pencil!\n" +
                "drawing a line on screen with crayon!\n" +
                "drawing a circle on screen with pencil!\n" +
                "drawing a circle on screen with what?\n" +
                "drawing a line on printer with what\n" +
                "drawing a line on printer with what\n" +
                "drawing a circle on printer with pencil\n" +
                "drawing a circle on printer with crayon";

        String finalRes = resTest1.concat(resTest2);

        return finalRes;
    }
}
