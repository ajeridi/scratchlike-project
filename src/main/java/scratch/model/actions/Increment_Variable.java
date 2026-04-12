package scratch.model.actions;

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
        // incrementation / decrementation de la variable target
        var varTable = scene.getVariables();
        double value;
        // A reflechir au cas ou variable a pour valeur une variable qui a pour valeur une variable qui a pour valeur une variable
        // au lieu d'avoir un Map<String, StringProperty> Faire un Map<String, Variable> et comme ca le Variable.getValue, on aura si c'est Litteral alors retourne la valeur directement, sinon retourne la valeur recherchee dans la table
        if (variableHasName())
            value = Double.parseDouble(varTable.get(variableName()).getValue().replace(",", "."));
        else
            value = getValue();

        // incrementation
        double targetValue = Double.parseDouble(varTable.get(getTargetted()).getValue().replace(",", ".")) + value;

        varTable.get(getTargetted()).setValue(String.format("%.2f", targetValue));

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
