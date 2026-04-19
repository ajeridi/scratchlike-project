package scratch.model.actions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import scratch.model.ExecutionState;
import scratch.model.Scene;
import scratch.model.Variable_Table;

public class Var_Assignment extends Target_Action {

    private static final String DEFAULT_VAR_ASSIGNMENT_NAME = "Assignation";
    private static final String DEFAULT_VAR_ASSIGNMENT_COLOR = "cyan";

    public Var_Assignment(String target, double value,String nom, String color){
        super(target, value, nom,  color);
    }

    public Var_Assignment(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Var_Assignment(Var_Assignment varAssignment) {
        this(varAssignment.getTargetted(), varAssignment.getValue(), varAssignment.getNom(), varAssignment.getColor());
    }

    public Var_Assignment() {
        super(DEFAULT_VAR_ASSIGNMENT_NAME, DEFAULT_VAR_ASSIGNMENT_COLOR);
    }


    public Var_Assignment(String targetVar, String value) {
        // constructeur pour file -> Action
        super(targetVar, value, DEFAULT_VAR_ASSIGNMENT_NAME, DEFAULT_VAR_ASSIGNMENT_COLOR);
    }


    @Override
    public void execute(ExecutionState executionState) {
        // modifier la valeur de la variable 'target_name'
        Variable_Table.setValue(getTargetted(), getValue());
        //variables.get(getTargetted()).setValue(getVariableValue());
    }

    private String getVariableValue() {
        if (variableHasName())
            return variableName();
        return String.format("%.2f", getValue());
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

/*
    @Override
    public String toString() {
        //String variable = (variableHasName() ? variableName() : (getValue() == 0 ? "" : String.valueOf(getValue())));
        return getNom() + " : " + getTargetted() + " = " + getStringValue();
    }
*/
}
