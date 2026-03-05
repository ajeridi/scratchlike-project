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

    public CatalogueViewModel(Catalogue catalogue){
        this.catalogue = catalogue;
    }

    public ObservableList<Action> getCatalogue() {
        return catalogue.getCatalogue();
    }

}
