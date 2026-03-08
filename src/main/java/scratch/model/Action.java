package scratch.model;

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

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        if (isValidValue(value))
            this.value.set(value);
    }

    abstract public void execute(Scene scene);

    abstract public boolean isValidValue(double value);

    abstract public Action create_default();
    abstract public Action copy();

    abstract public String valueUnit();
    abstract public String getType();

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
