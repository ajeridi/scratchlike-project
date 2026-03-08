package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import scratch.model.Action;
import scratch.model.Program;

public class ProgramViewModel {

    private final Program program;
    private final IntegerProperty selectedIndex = new SimpleIntegerProperty();
    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();


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
}
