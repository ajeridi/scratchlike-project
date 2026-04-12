package scratch.model.actions;

import javafx.beans.property.SimpleDoubleProperty;
import scratch.model.Scene;
import scratch.model.Variable;

public abstract class Action {
    private final String nom;
   // private final SimpleDoubleProperty value = new SimpleDoubleProperty();
    private static final double DEFAULT_ACTION_VALUE = 0;
    private final Variable variable;
    private  String color;

    public Action(double value, String nom, String color) {
        this.variable = new Variable(value);
        //this.value.set(value);
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
        return valueProperty().getValue();
    }

    public SimpleDoubleProperty valueProperty() {
        return variable.valueProperty();
    }

    public void setValue(double value) {
        if (isValidValue(value))
            valueProperty().set(value);
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
        return nom + (valueProperty().getValue() == 0 ? "" : " " + valueProperty().getValue());
    }
}
