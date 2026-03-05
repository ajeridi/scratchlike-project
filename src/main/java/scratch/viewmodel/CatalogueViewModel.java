package scratch.viewmodel;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import scratch.model.Action;
import scratch.model.Catalogue;

public class CatalogueViewModel {

    private final Catalogue catalogue;
    private final MainViewModel mainViewModel;

    public CatalogueViewModel(Catalogue catalogue, MainViewModel mainViewModel){
        this.catalogue = catalogue;
        this.mainViewModel = mainViewModel;
    }

    public ObservableList<Action> getCatalogue() {
        return catalogue.getCatalogue();
    }

    public boolean addToProgram(Action selectedAction){
        return mainViewModel.addToProgram(selectedAction);
    }

}
