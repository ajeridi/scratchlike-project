package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();

    public Point(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    public double getX() {
        return x.getValue();
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.getValue();
    }

    public void setY(double y) {
        this.y.set(y);
    }

    @Override
    public String toString() {
        return "(" + x.getValue() + "," + y.getValue() + ")";
    }

    public Point translate(double translateX, double translateY){
        return new Point(x.getValue() + translateX, y.getValue() - translateY);
    }

    public ReadOnlyDoubleProperty getXProperty() {
        return x;
    }

    public ReadOnlyDoubleProperty getYProperty() {
        return y;
    }
}
