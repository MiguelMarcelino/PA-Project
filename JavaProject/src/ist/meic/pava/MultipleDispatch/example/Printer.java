package ist.meic.pava.MultipleDispatch.example;

public class Printer extends Device {
    public void draw(Shape s) {
        System.err.println("draw what on printer?");
    }
    public void draw(Line l) {
        System.err.println("drawing a line on printer!");
    }
    public void draw(Circle c) {
        System.err.println("drawing a circle on printer!");
    }

    public void draw(Line l, Brush b) {
        System.err.println("drawing a line on printer with what");
    }

    public void draw(Circle c, Pencil p) {
        System.err.println("drawing a circle on printer with pencil");
    }

    public void draw(Circle c, Crayon r) {
        System.err.println("drawing a circle on printer with crayon");
    }
}
