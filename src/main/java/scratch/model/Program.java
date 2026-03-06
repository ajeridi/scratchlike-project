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
        // faire une copie de l'action
        var copy = action.copy();
        return actionList.add(copy);
    }

    public void swapActions(int index1, int index2){
        actionList.swap(index1, index2);
    }

    public void up() {
        // monter la liste d'actions
    }

    public void down(){
        //  descendre la liste d'actions
    }

    public void duplicate(){
        //dupliquer une action
    }

    public void delete(){
        // supprimer une action
    }

    public void deleteAll() {
        // supprimer toutes les actions
    }
}
