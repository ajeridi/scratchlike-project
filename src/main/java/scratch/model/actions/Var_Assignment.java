package scratch.model.actions;

import javafx.beans.property.SimpleStringProperty;
import scratch.model.Scene;

public class Var_Assignment extends Action implements Target_Action{

    private final SimpleStringProperty target_name = new SimpleStringProperty("");
    private static final String DEFAULT_VAR_ASSIGNMENT_NAME = "Assignation";
    private static final String DEFAULT_VAR_ASSIGNMENT_COLOR = "cyan";

    public Var_Assignment(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Var_Assignment(Var_Assignment varAssignment) {
        this(varAssignment.getValue(), varAssignment.getNom(), varAssignment.getColor());
    }

    public Var_Assignment() {
        super(DEFAULT_VAR_ASSIGNMENT_NAME, DEFAULT_VAR_ASSIGNMENT_COLOR);
    }


    @Override
    public void execute(Scene scene) {
        // modifier la valeur de la variable 'target_name'

    }

    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public Action create_default() {
        return new Var_Assignment(0, DEFAULT_VAR_ASSIGNMENT_NAME, DEFAULT_VAR_ASSIGNMENT_COLOR);
    }

    @Override
    public Action copy() {
        return new Var_Assignment(this);
    }

    @Override
    public String valueUnit() {
        return "";
    }

    @Override
    public String getType() {
        return "VAR_ASSIGNMENT";
    }

    @Override
    public String getDetailString() {
        return "Assignation de la variable";
    }

    @Override
    public String getTargetted() {
        return target_name.getValue();
    }

    @Override
    public void setTarget(String name) {
        target_name.setValue(name);
    }

    @Override
    public String toString() {
        String variable = (variableHasName() ? variableName() : (getValue() == 0 ? "" : String.valueOf(getValue())));
        return getNom() + " : " + target_name.getValue() + " = " + variable;
    }
}
