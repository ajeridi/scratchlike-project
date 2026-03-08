package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import scratch.model.Action;
import scratch.model.Catalogue;
import scratch.model.Program;
import scratch.model.Scene;

public class MainViewModel {
    private final CatalogueViewModel catalogueViewModel;
    private final ProgramViewModel programViewModel;
    private final SceneViewModel sceneViewModel;

    public MainViewModel(Catalogue catalogue, Program program, Scene scene) {
        this.catalogueViewModel = new CatalogueViewModel(catalogue, this);
        this.programViewModel = new ProgramViewModel(program);
        this.sceneViewModel = new SceneViewModel(scene, this);
    }

    public CatalogueViewModel getCatalogueViewModel() {
        return catalogueViewModel;
    }

    public ProgramViewModel getProgramViewModel() {
        return programViewModel;
    }

    public SceneViewModel getSceneViewModel() {
        return sceneViewModel;
    }

    public boolean addToProgram(Action selectedAction){
        return programViewModel.addToActionList(selectedAction);
    }

    public IntegerProperty getSelectedIndexProperty() {
        return programViewModel.getSelectedIndexProperty();
    }

    public ReadOnlyObjectProperty<Action> getCurrentSelectedActionProperty() {
        return programViewModel.getCurrentSelectedActionProperty();
    }

    public IntegerBinding getActionsSizeProperty() {
        return programViewModel.getActionsSizeProperty();
    }

    public ObservableList<Action> getActionsList() {
        return programViewModel.getActions();
    }

    public BooleanBinding isActionsListEmpty() {
        return programViewModel.actionIsEmptyProperty();
    }

    public void newFile() {
        sceneViewModel.newProgram();
        programViewModel.newProgram();
    }
}
