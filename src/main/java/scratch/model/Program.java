package scratch.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;

public class Program {

    private final ActionList actionList = new ActionList();

    public ObservableList<Action> getActions() {
        return actionList.getActions();
    }

    public IntegerBinding getSizeProperty() {
        return actionList.getSizeProperty();
    }

    public BooleanBinding actionsIsEmptyProperty() {
        return actionList.isEmptyProperty();
    }

    public boolean addToActionList(Action action){
        // creer une action avec une valeur par defaut
        Action default_action = action.create_default();
        return actionList.add(default_action);
    }

    public void swapActions(int index1, int index2){
        actionList.swap(index1, index2);
    }

    public void up(int selectedIndex) {
        // monter la liste d'actions
        actionList.swap(selectedIndex, selectedIndex - 1);
    }

    public void down(int selectedIndex){
        //  descendre la liste d'actions
        actionList.swap(selectedIndex, selectedIndex+1);
    }

    public void duplicate(Action action){
        //dupliquer une action
        Action copy = action.copy();
        actionList.add(copy);
    }

    public void delete(Action action){
        // supprimer une action
        actionList.delete(action);
    }

    public void deleteAll() {
        // supprimer toutes les actions
    }
}
