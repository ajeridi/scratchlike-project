package scratch.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class Catalogue {
    //private final Set<Action> catalogue = new HashSet<>();

    private final ActionList catalogue = new ActionList();

    public Catalogue(){
        catalogue.add(new Move_Forward());
        catalogue.add(new Turn_Left());
        catalogue.add(new Turn_Right());
        catalogue.add(new Pen_Up());
        catalogue.add(new Pen_Down());
    }

    public boolean add(Action action){
        return catalogue.add(action);
    }

    public ObservableList<Action> getCatalogue() {
        return catalogue.getActions();
    }

    public boolean addToProgram(){
        return true;
    }
}
