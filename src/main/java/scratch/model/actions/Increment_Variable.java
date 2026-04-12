package scratch.model.actions;

import javafx.beans.property.StringProperty;
import scratch.model.Scene;

public class Increment_Variable extends Target_Action{

    private static final String DEFAULT_INCREMENT_VAR_NAME = "Incr/Decr variable";
    private static final String DEFAULT_INCREMENT_VAR_COLOR = "cyan";

    public Increment_Variable(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Increment_Variable() {
        super(DEFAULT_INCREMENT_VAR_NAME, DEFAULT_INCREMENT_VAR_COLOR);
    }

    public Increment_Variable(Increment_Variable incrementVariable) {
        super(incrementVariable.getTargetted(), incrementVariable.getValue(), incrementVariable.getNom(), incrementVariable.getColor());
    }

    @Override
    public void execute(Scene scene) {

    }

    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public Action create_default() {
        return new Increment_Variable(0, DEFAULT_INCREMENT_VAR_NAME, DEFAULT_INCREMENT_VAR_COLOR);
    }

    @Override
    public Action copy() {
        return new Increment_Variable(this);
    }

    @Override
    public String valueUnit() {
        return "";
    }

    @Override
    public String getType() {
        return "INCREMENT_VARIABLE";
    }

    @Override
    public String getDetailString() {
        return "Incrementation de la variable";
    }
}
