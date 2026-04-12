package scratch.model;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;
import scratch.model.actions.Target_Action;

public class ActionList {

    private final ObservableList<Action> actions =
            FXCollections.observableArrayList(
                    // si une action est de type Target_Action, on rajoute une observation sur target_name
                    action -> {
                        if (action instanceof Target_Action ta) {
                            return new Observable[]{
                                    action.valueProperty(),
                                    action.variableNameProperty(),
                                    ta.getTargetNameProperty()
                            };
                        }
                        return new Observable[]{
                                action.valueProperty(),
                                action.variableNameProperty(),
                        };
                    }
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

    public void swap(int index1, int index2){
        Action temp = actions.get(index1);
        actions.set(index1, actions.get(index2));
        actions.set(index2, temp);
    }

    public void delete(Action action){
        actions.remove(action);
    }

    public void deleteAll() {
        actions.clear();
    }
}
