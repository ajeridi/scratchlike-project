package scratch.viewmodel;

import javafx.collections.ObservableList;
import scratch.model.actions.Action;
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

    public boolean addToProgram(Action selectedAction, String from){
        return mainViewModel.addToProgram(selectedAction, "from-catalogue");
    }



}
