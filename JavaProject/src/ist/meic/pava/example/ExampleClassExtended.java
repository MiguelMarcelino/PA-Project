package ist.meic.pava.example;

import ist.meic.pava.MultipleDispatchExtended.UsingMultipleDispatchUnboxing;

public class ExampleClassExtended {

    public static void exampleExtended1() {
        Device[] devices = new Device[]{new Screen(), new Printer()};
        Shape[] shapes = new Shape[]{new Line(), new Circle()};
        Integer[] ints = {1, 2};
        Integer[] primitivePos = {};

        for (Device device : devices) {
            for (Shape shape : shapes) {
                for (Integer intValue : ints) {
                    System.err.println(device.getClass().getSimpleName() + " " +
                            shape.getClass().getSimpleName());
                    UsingMultipleDispatchUnboxing.invoke(device, "draw", primitivePos, shape, intValue);
                }
            }
        }
    }

    /*
        (int)
    * (drawing a line on screen with (INT)!)
    * (drawing a line on screen with (INT)!)
    * (draw a circle on screen and with what (INT))
    * (draw a circle on screen and with what (INT))
    * (draw a line on screen and with what (INTEGER)?)
    * (draw a line on screen and with what (INTEGER)?)
    * (draw a circle on screen and with what (INT)?)
    * (draw a circle on screen and with what (INT)?)


        (Integer)
        (draw a line on screen and with what (INTEGER)?)
        (draw a line on screen and with what (INTEGER)?)
        (draw what where (INTEGER)?)
        (draw what where (INTEGER)?)
        (draw a line on screen and with what (INTEGER)?)
        (draw a line on screen and with what (INTEGER)?)
        (draw a circle on screen and with what (INTEGER)?)
        (draw a circle on screen and with what (INTEGER)?)
    * */
}

