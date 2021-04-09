package ist.meic.pava.tests;

import ist.meic.pava.tests.extendedVersionTests.interfaceTests.ITest1;
import ist.meic.pava.tests.extendedVersionTests.interfaceTests.Test;

public class Printer extends Device {
    // For example 1
    public void draw(Shape s) {
        System.err.println("draw what on printer?");
    }
    public void draw(Line l) {
        System.err.println("drawing a line on printer!");
    }
    public void draw(Circle c) {
        System.err.println("drawing a circle on printer!");
    }

    // For example 2
    public void draw(Line l, Brush b) {
        System.err.println("drawing a line on printer with what");
    }
    public void draw(Circle c, Pencil p) {
        System.err.println("drawing a circle on printer with pencil");
    }
    public void draw(Circle c, Crayon r) {
        System.err.println("drawing a circle on printer with crayon");
    }

    // For example 3
    public void draw(Line l, Brush b, Circle c) {
        System.err.println("drawing a line on printer with what (circle)");
    }
    public void draw(Circle c, Pencil p, Line l) {
        System.err.println("drawing a circle on printer with pencil (line)");
    }
    public void draw(Circle c, Crayon r, Line l) {
        System.err.println("drawing a circle on printer with crayon (line)");
    }

    // For example 4
    public void draw(Line l, Brush b, Brown c) {
        System.err.println("drawing a line on printer with what (Brown)");
    }
    public void draw(Circle c, Pencil p, Yellow y) {
        System.err.println("drawing a circle on printer with pencil (Yellow)");
    }
    public void draw(Circle c, Crayon r, Brown cl) {
        System.err.println("drawing a circle on printer with crayon (Brown)");
    }

    // For example 5
    public void draw(Line l, Brush b, Brown c, Brush b2) {
        System.err.println("drawing a line on printer with what (Brown)");
    }
    public void draw(Circle c, Pencil p, Yellow y,  Pencil p2) {
        System.err.println("drawing a circle on printer with pencil (Yellow)");
    }
    public void draw(Circle c, Crayon r, Brown cl, Crayon r2) {
        System.err.println("drawing a circle on printer with crayon (Brown)");
    }

    // For example extended 1/2
    public void draw(Line l, Integer i) {
        System.err.println("draw a line on printer and with what (INTEGER)?");
    }
    public void draw(Circle l, int i) {
        System.err.println("draw a circle on printer and with what (INT)?");
    }
    public void draw(Circle l, Integer i) {
        System.err.println("draw a circle on printer and with what (INTEGER)?");
    }

    // For example extended 3
    public void draw(Line l, Test t) {
        System.err.println("drawing a line on printer TEST");
    }
    public void draw(Circle l, ITest1 t1) {
        System.err.println("draw a circle on printer TEST1");
    }
}
