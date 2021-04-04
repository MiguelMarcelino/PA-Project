package ist.meic.pava.tests;

import ist.meic.pava.tests.extendedVersionTests.interfaceTests.Test;

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

    // For example 3
    public void draw(Shape s, Brush b, Shape s2) {
        System.err.println("draw what where and with what?");
    }
    public void draw(Line l, Brush b, Circle c) {
        System.err.println("draw a line where and with what (circle)?");
    }
    public void draw(Circle c, Brush b, Line l) {
        System.err.println("draw a circle where and with what (line)?");
    }

    // For example 4
    public void draw(Shape s, Brush b, Color c) {
        System.err.println("draw what where and with what (color)?");
    }

    // For example 5
    public void draw(Shape s, Brush b, Color c, Brush b2) {
        System.err.println("draw what where and with what (color)?");
    }

    // For example extended 1/2
    public void draw(Shape s, Integer i) {
        System.err.println("draw what where (INTEGER)?");
    }

    // For example extended 3
    public void draw(Shape l, Test t) {
        System.err.println("draw what where TEST");
    }
}
