package scratch.viewmodel;

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
}
