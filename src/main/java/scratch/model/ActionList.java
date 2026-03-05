package scratch.model;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
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

    public IntegerBinding getSizeProperty() {
        return Bindings.size(actions);
    }

    public BooleanBinding isEmptyProperty(){
        return Bindings.createBooleanBinding(
                () -> {
                    return actions.isEmpty();
                },
                actions
        );
    }
}
