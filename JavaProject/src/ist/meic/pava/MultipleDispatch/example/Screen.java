package ist.meic.pava.MultipleDispatch.example;

public class Screen extends Device{
    // For example 1
    public void draw(Shape s) {
        System.err.println("draw what on screen?");
    }
    public void draw(Line l) {
        System.err.println("drawing a line on screen!");
    }
    public void draw(Circle c) {
        System.err.println("drawing a circle on screen!");
    }

    // For example 2
    public void draw(Line l, Brush b) {
        System.err.println("draw a line on screen and with what?");
        // Previously it was: draw what where and with what? --> should it be on screen?
    }
    public void draw(Line l, Pencil p) {
        System.err.println("drawing a line on screen with pencil!");
    }
    public void draw(Line l, Crayon c) {
        System.err.println("drawing a line on screen with crayon!");
    }
    public void draw(Circle c, Brush b) {
        System.err.println("drawing a circle on screen with what?");
    }
    public void draw(Circle c, Pencil p) {
        System.err.println("drawing a circle on screen with pencil!");
    }

    // For example 3
    public void draw(Line l, Brush b, Shape s) {
        System.err.println("----draw a line on screen and with what?");
        // Previously it was: draw what where and with what? --> should it be on screen?
    }
    public void draw(Line l, Pencil p, Circle c) {
        System.err.println("----drawing a line on screen with pencil!");
    }
    public void draw(Line l, Crayon c, Circle cl) {
        System.err.println("----drawing a line on screen with crayon!");
    }
    public void draw(Circle c, Brush b, Line l) {
        System.err.println("----drawing a circle on screen with what?");
    }
    public void draw(Circle c, Pencil p, Line l) {
        System.err.println("----drawing a circle on screen with pencil!");
    }
}
