package scratch.model.actions;

import scratch.model.Scene;

public class Var_Declaration extends Action {

    private static final String DEFAULT_VAR_DECLARATION_COLOR = "cyan";
    private static final String DEFAULT_VAR_DECLARATION_NAME = "Declaration Variable";
    private static final double DEFAULT_VAR_DECLARATION_VALUE = 0;

    public Var_Declaration() {
        super(DEFAULT_VAR_DECLARATION_NAME, DEFAULT_VAR_DECLARATION_COLOR);
    }

    public Var_Declaration(double value, String nom, String color){
        super(value, nom, color);
    }

    public Var_Declaration(Var_Declaration varDeclaration) {
        this(varDeclaration.getValue(), varDeclaration.getNom(), varDeclaration.getColor());
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
        return new Var_Declaration(DEFAULT_VAR_DECLARATION_VALUE, DEFAULT_VAR_DECLARATION_NAME, DEFAULT_VAR_DECLARATION_COLOR);
    }

    @Override
    public Action copy() {
        return new Var_Declaration(this);
    }

    @Override
    public String valueUnit() {
        return "";
    }

    @Override
    public String getType() {
        return "VAR_DECLARATION";
    }

    @Override
    public String getDetailString() {
        return "Declaration de la variable";
    }

    @Override
    public String toString() {
        return "Declaration de la variable " + variableName();
    }
}
