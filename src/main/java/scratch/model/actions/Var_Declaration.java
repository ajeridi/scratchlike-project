package scratch.model.actions;

import scratch.model.Scene;

public class Var_Declaration extends Action {

    private static final String DEFAULT_VAR_DECLARATION_COLOR = "cyan";
    private static final String DEFAULT_VAR_DECLARATION_NAME = "Declaration Variable";


    public Var_Declaration() {
        super(DEFAULT_VAR_DECLARATION_NAME, DEFAULT_VAR_DECLARATION_COLOR);
    }

    @Override
    public void execute(Scene scene) {

    }

    @Override
    public boolean isValidValue(double value) {
        return false;
    }

    @Override
    public Action create_default() {
        return null;
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public String valueUnit() {
        return "";
    }

    @Override
    public String getType() {
        return "";
    }
}
