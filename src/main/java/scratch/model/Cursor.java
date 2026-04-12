package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Cursor {

    private final double DEFAULT_CURSOR_HEAD_ANGLE = 0;

    private Point origine;
    private final DoubleProperty direction = new SimpleDoubleProperty(DEFAULT_CURSOR_HEAD_ANGLE);
    private final DoubleProperty cursorX = new SimpleDoubleProperty(0);
    private final DoubleProperty cursorY = new SimpleDoubleProperty(0);
    private boolean writing = true;

    public Cursor(Point origine){ // constructeur par défaut
        setOrigine(origine);
        //this.origine = origine;
    }

    // peut etre inutile, a voir
    public Cursor(Point origine, double direction, boolean writing) {
        this(origine);
        this.direction.setValue(direction);
        this.writing = writing;
    }

    public Point getOrigine() {
        return origine;
    }

    public void setOrigine(Point origine) {
        this.origine = origine;
        cursorX.unbind();
        cursorX.bind(origine.getXProperty());
        cursorY.unbind();
        cursorY.bind(origine.getYProperty());

    }

    public double getDirection() {
        return direction.getValue();
    }

    public DoubleProperty getDirectionProperty() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction.set(direction);
        if (this.direction.get() == 360 || this.direction.get() == -360)
            this.direction.set(DEFAULT_CURSOR_HEAD_ANGLE);
    }

    public boolean isWriting() {
        return writing;
    }

    public void setWriting(boolean writing) {
        this.writing = writing;
    }

    public double getNorm() {
        double x = origine.getX();
        double y = origine.getY();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public String toString() {
        return "Cursor is at position " + this.origine + " with a direction of " + this.direction + " degrees and a norm of " + getNorm();
    }

    public void reset() {
        origine.setX(0);
        origine.setY(0);
        direction.set(DEFAULT_CURSOR_HEAD_ANGLE);
        setWriting(true);
    }

    public ReadOnlyDoubleProperty getXProperty() {
        return cursorX;
    }

    public ReadOnlyDoubleProperty getYProperty() {
        return cursorY;
    }
}
