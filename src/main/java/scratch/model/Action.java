package scratch.model;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class Action {
    private final String nom;
    private final SimpleDoubleProperty value = new SimpleDoubleProperty();
    private static final double DEFAULT_ACTION_VALUE = 0;
    private  String color;

    public Action(double value, String nom, String color) {
        this.value.set(value);
        this.nom = nom;
        this.color = color;
    }

    public Action(String nom, String color) {
        this(DEFAULT_ACTION_VALUE, nom, color);
    }

    public String getNom() {
        return nom;
    }


    public double getValue() {
        return value.getValue();
    }

    public ReadOnlyDoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    abstract public MyLine execute(Cursor cursor);

    abstract public Action copy();

    public void setColor(String color) {
        this.color = color;
    };

    public String getColor(){
        return color;
    }

    @Override
    public String toString() {
        return nom + (value.getValue() == 0 ? "" : " " + value.getValue());
    }
}
