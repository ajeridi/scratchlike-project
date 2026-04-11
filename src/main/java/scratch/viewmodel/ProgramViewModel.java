package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;
import scratch.model.Program;

public class ProgramViewModel {

    private final Program program;
    private final IntegerProperty selectedIndex = new SimpleIntegerProperty();
    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();
    private final BooleanProperty isValidValue = new SimpleBooleanProperty();
    private final StringProperty userInput = new SimpleStringProperty();


    public ProgramViewModel(Program program) {
        this.program = program;

        userInput.addListener(
                ((observableValue, s, t1) -> {
                    if (isValidValue.get())
                        getCurrentSelectedAction().setValue(Double.parseDouble(userInput.get()));
                })
        );
        setBindings();
    }

    private void setBindings() {
        isValidValue.bind(Bindings.createBooleanBinding(
                () -> {

                    double value;
                    boolean ok = false;
                    if (getCurrentSelectedAction() != null) {
                        try {
                            value = Double.parseDouble(userInput.getValue());
                            ok = getCurrentSelectedAction().isValidValue(value);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    return ok;
                },
                userInput
        ));
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

    public StringProperty getUserInputProperty() {
        return userInput;
    }

    public ReadOnlyBooleanProperty isValidValueProperty() {
        return isValidValue;
    }

    public void updateUserInput() {
        String newValue = "";
        if (getCurrentSelectedAction() != null) {
            double valueOfSelectedAction = currentSelectedAction.get().getValue();
            newValue = String.valueOf(valueOfSelectedAction);
        }
        userInput.setValue(newValue);
    }

    public void newProgram() {
        program.deleteAll();
    }
}
