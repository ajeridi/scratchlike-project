package scratch.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ActionList {

    private final ObservableList<Action> actions =
            FXCollections.observableArrayList(
                    action -> new Observable[]{action.valueProperty()}
            );

    public boolean add(Action action) {
        return actions.add(action);
    }

    public ObservableList<Action> getActions() {
        return actions;
    }
}
