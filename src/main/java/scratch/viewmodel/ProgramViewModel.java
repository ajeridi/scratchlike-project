package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import scratch.model.Action;
import scratch.model.Program;

public class ProgramViewModel {

    private final Program program;


    public ProgramViewModel(Program program) {
        this.program = program;
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
        addToActionList(action);
    }

    public void deleteAction(Action action){
        program.delete(action);
    }

    public void moveUp(int selectedIndex){
        program.up(selectedIndex);
    }

    public void moveDown(int selectedIndex){
        program.down(selectedIndex);
    }
}
