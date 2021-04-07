package ist.meic.pava.tests;

import ist.meic.pava.tests.extendedVersionTests.interfaceTests.ITest1;
import ist.meic.pava.tests.extendedVersionTests.interfaceTests.ITest2;
import ist.meic.pava.tests.extendedVersionTests.interfaceTests.TesterClass;

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
        System.err.println("draw a line on screen and with what?");
    }
    public void draw(Line l, Pencil p, Circle c) {
        System.err.println("drawing a line on screen with pencil (circle)!");
    }
    public void draw(Line l, Crayon c, Circle cl) {
        System.err.println("drawing a line on screen with crayon (circle)!");
    }
    public void draw(Circle c, Brush b, Line l) {
        System.err.println("drawing a circle on screen with what (line)?");
    }
    public void draw(Circle c, Pencil p, Line l) {
        System.err.println("drawing a circle on screen with pencil (line)!");
    }

    // For example 4
    public void draw(Line l, Brush b, Color c) {
        System.err.println("draw a line on screen and with what (Color)?");
    }
    public void draw(Line l, Brush b, Yellow c) {
        System.err.println("draw a line on screen and with what (Yellow)?");
    }
    public void draw(Line l, Crayon b, Color c) {
        System.err.println("draw a line on screen and with crayon (Color)?");
    }
    public void draw(Line l, MiniCrayon b, Color c) {
        System.err.println("draw a line on screen and with Minicrayon (Color)?");
    }
    public void draw(Line l, Pencil p, Brown b) {
        System.err.println("drawing a line on screen with pencil (Brown)!");
    }
    public void draw(Line l, Crayon c, Brown b) {
        System.err.println("drawing a line on screen with crayon (Brown)!");
    }
    public void draw(Circle c, Brush b, Yellow y) {
        System.err.println("drawing a circle on screen with what (Yellow)?");
    }
    public void draw(Circle c, Pencil p, Yellow y) {
        System.err.println("drawing a circle on screen with pencil (Yellow)!");
    }

    // For example 5
    public void draw(Line l, Brush b, Color c, Brush b2) {
        System.err.println("draw a line on screen and with what (Color) 2?");
    }
    public void draw(Line l, Brush b, Yellow c, Brush b2) {
        System.err.println("draw a line on screen and with what (Yellow) 2?");
    }
    public void draw(Line l, Crayon b, Color c, Pencil b2) {
        System.err.println("draw a line on screen and with crayon (Color) 2?");
    }
    public void draw(Line l, Crayon b, Color c, Crayon b2) {
        System.err.println("draw a line on screen and with Minicrayon (Color) 2?");
    }
    public void draw(Line l, Pencil p, Brown b, Pencil p2) {
        System.err.println("drawing a line on screen with pencil (Brown) 2!");
    }
    public void draw(Line l, Crayon c, Brown b, Crayon c2) {
        System.err.println("drawing a line on screen with crayon (Brown) 2!");
    }
    public void draw(Circle c, Brush b, Yellow y, Brush b2) {
        System.err.println("drawing a circle on screen with what (Yellow) 2?");
    }
    public void draw(Circle c, Pencil p, Yellow y, Pencil p2) {
        System.err.println("drawing a circle on screen with pencil (Yellow) 2!");
    }

    // For example extended 1/2
    public void draw(Line l, Integer i) {
        System.err.println("draw a line on screen and with what (INTEGER)?");
    }
    public void draw(Line l, int i) {
        System.err.println("drawing a line on screen with (INT)!");
    }
    public void draw(Circle l, int i) {
        System.err.println("draw a circle on screen and with what (INT)");
    }

    // For example extended 3
    public void draw(ITest1 t) {System.err.println("drawing a line on screen TEST1");}
    public void draw(ITest2 t) {System.err.println("drawing a line on screen TEST2");}
}
