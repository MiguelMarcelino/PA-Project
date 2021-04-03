package ist.meic.pava.example;

import ist.meic.pava.MultipleDispatch.UsingMultipleDispatch;

public class ExampleClass {

    public static void example1() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                System.err.println(device.getClass().getSimpleName() + " " +
                        shape.getClass().getSimpleName());
                UsingMultipleDispatch.invoke(device, "draw", shape);
            }
        }
    }

    public static void example2() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Brush[] brushes = new Brush[]{new Pencil(), new Crayon()};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Brush brush : brushes) {
                    System.err.println(device.getClass().getSimpleName() + " " +
                            shape.getClass().getSimpleName() + " " +
                            brush.getClass().getSimpleName());
                    UsingMultipleDispatch.invoke(device, "draw", shape, brush);
                }
            }
        }
    }

    public static void example3() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Brush[] brushes = new Brush[]{new Pencil(), new Crayon()};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Brush brush : brushes) {
                    for (Shape shape2 : shapes) {
                        System.err.println(device.getClass().getSimpleName() + " " +
                                shape.getClass().getSimpleName() + " " +
                                brush.getClass().getSimpleName() + " " +
                                shape2.getClass().getSimpleName());
                        UsingMultipleDispatch.invoke(device, "draw", shape, brush, shape2);
                    }
                }
            }
        }
    }

    public static void example4() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Brush[] brushes = new Brush[]{new Pencil(), new Crayon()};
        Color[] colors = new Color[]{new Brown(), new Yellow()};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Brush brush : brushes) {
                    for (Color color : colors) {
                        System.err.println(device.getClass().getSimpleName() + " " +
                                shape.getClass().getSimpleName() + " " +
                                brush.getClass().getSimpleName() + " " +
                                color.getClass().getSimpleName());
                        UsingMultipleDispatch.invoke(device, "draw", shape, brush, color);
                    }
                }
            }
        }
    }

    public static void example5() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Brush[] brushes = new Brush[]{new Pencil(), new Crayon()};
        Color[] colors = new Color[]{new Brown(), new Yellow()};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Brush brush : brushes) {
                    for (Color color : colors) {
                        for (Brush brush2 : brushes) {
                            System.err.println(device.getClass().getSimpleName() + " " +
                                    shape.getClass().getSimpleName() + " " +
                                    brush.getClass().getSimpleName() + " " +
                                    color.getClass().getSimpleName() + " " +
                                    brush2.getClass().getSimpleName());
                            UsingMultipleDispatch.invoke(device, "draw", shape, brush, color, brush2);
                        }
                    }
                }
            }
        }
    }

}
