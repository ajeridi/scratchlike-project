package scratch.model;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class Action {
    private String nom;
    private SimpleDoubleProperty value = new SimpleDoubleProperty();

    public Action(double value, String nom) {
        this.value.set(value);
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
}
