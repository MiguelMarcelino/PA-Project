package ist.meic.pava.MultipleDispatch.example;

public class Device {
    // For example 1
    public void draw(Shape s) {
        System.err.println("draw what where?");
    }
    public void draw(Line l) {
        System.err.println("draw a line where?");
    }
    public void draw(Circle c) {
        System.err.println("draw a circle where?");
    }

    // For example 2
    public void draw(Shape s, Brush b) {
        System.err.println("draw what where and with what?");
    }
    public void draw(Line l, Brush b) {
        System.err.println("draw a line where and with what?");
    }
    public void draw(Circle c, Brush b) {
        System.err.println("draw a circle where and with what?");
    }
}
