package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;
import scratch.model.Program;
import scratch.model.actions.Target_Action;
import scratch.model.actions.Var_Declaration;

public class ProgramViewModel {

    private final Program program;
    private final IntegerProperty selectedIndex = new SimpleIntegerProperty();
    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();
    private final BooleanProperty isValidValue = new SimpleBooleanProperty();
    private final StringProperty variableInput = new SimpleStringProperty(); // valeur de la variable entree par le user
    private final StringProperty targetInput = new SimpleStringProperty();


    public ProgramViewModel(Program program) {
        this.program = program;

        variableInput.addListener(
                // au changement de user input, valider l'entree, puis modifier la variable
                ((observableValue, s, t1) -> {
                    if (isValidValue.get()) {
                        var action = getCurrentSelectedAction();
                        var input = variableInput.get();
                        // si la valeur est un nom, on update le nom de la variable, sinon on update la valeur
                        if (isDouble(input)){
                            action.setValue(Double.parseDouble(input));
                        }
                        else {
                            action.setVarName(input);
                        }
                    }
//                        getCurrentSelectedAction().setValue(Double.parseDouble(variableInput.get()));
                })
        );

        targetInput.addListener(
                // si target input est modifie, on valide l'input puis on modifie la valeur de target de l'action
                // il faut que le nom de la variable existe sinon on ne peut pas l'ajouter
                ((observableValue, s, t1) -> {
                    var action = getCurrentSelectedAction();
                    if (action instanceof Target_Action){
                        ((Target_Action) action).setTarget(targetInput.getValue());
                    }
                })
        );
        setBindings();
    }

    private void setBindings() {
        isValidValue.bind(Bindings.createBooleanBinding(
                // des que variableInput est modifie, on valide la valeur entree
                () -> {

                    double value = 0;
                    boolean ok = false;
                    if (getCurrentSelectedAction() != null) {
                        if (isDouble(variableInput.getValue())){ // si l'input est un double, on valide le double
                            ok = getCurrentSelectedAction().isValidValue(Double.parseDouble(variableInput.getValue()));
                        }
                        else { // sinon on valide le nom
                            ok = getCurrentSelectedAction().isValidVarName(variableInput.getValue());
                        }
                    }
                    return ok;
                },
                variableInput
        ));
    }

    public boolean isDouble(String s) {
        // verifie sur un string est un double
        boolean result;
        try {
            Double.parseDouble(variableInput.getValue());
            result = true;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            result = false;
        }
        return result;
    }

    public boolean addToActionList(Action selectedAction){
        return program.addToActionList(selectedAction);
    }

    public ObservableList<Action> getActions() {
        return program.getActions();
    }

    public IntegerBinding getActionsSizeProperty(){
        return program.getSizeProperty();
    }

    public BooleanBinding actionIsEmptyProperty() {
        return program.actionsIsEmptyProperty();
    }

    public void swapActions(int index1, int index2){
        program.swapActions(index1, index2);
    }

    public void duplicate(Action action){
        program.duplicate(action);
    }

    public void deleteAction(Action action){
        program.delete(action);
    }

    public void deleteAllActions(){
        program.deleteAll();
    }

    public void moveUp(int selectedIndex){
        program.up(selectedIndex);
    }

    public void moveDown(int selectedIndex){
        program.down(selectedIndex);
    }



    public IntegerProperty getSelectedIndexProperty() {
        return selectedIndex;
    }


    public ObjectProperty<Action> getCurrentSelectedActionProperty() {
        return currentSelectedAction;
    }

    public Action getCurrentSelectedAction() {
        return currentSelectedAction.get();
    }

    public StringProperty getVariableInputProperty() {
        return variableInput;
    }
    public StringProperty getTargetInputProperty() {
        return targetInput;
    }

    public ReadOnlyBooleanProperty isValidValueProperty() {
        return isValidValue;
    }

    public void updateUserInput() {
        // va update le contenu de variableInput
        String newValue = "";
        if (getCurrentSelectedAction() != null) {
            var action = getCurrentSelectedAction();
            // on regarde si la variable est litterale ou entiere
            // si la variable est litterale on affiche la valeur, si elle est entiere on affiche le nom
            if (action.variableHasName() || action instanceof Var_Declaration){
                newValue = action.variableName();
            }
            else{
                newValue = String.valueOf(action.getValue());
            }
        }
        variableInput.setValue(newValue);
    }

    public void updateTargetInput() {
        var action = getCurrentSelectedAction();
        if (action instanceof Target_Action){
            targetInput.setValue(((Target_Action) action).getTargetted());
        }
    }


    public void newProgram() {
        program.deleteAll();
    }

    public BooleanBinding isTargetAction() {
        return Bindings.createBooleanBinding(
                () -> {
                    return getCurrentSelectedAction() instanceof Target_Action;
                },
                currentSelectedAction
        );
    }

    public BooleanBinding isActionWithValue() {
        return Bindings.createBooleanBinding(
                () -> {
                    var action = getCurrentSelectedAction();
                    if (action == null) {
                        return false;
                    }
                    return switch (action.getType()) {
                        case "PEN_UP", "PEN_DOWN", "END_REPEAT" -> false;
                        default -> true;
                    };
                },
                currentSelectedAction
        );
    }
}
