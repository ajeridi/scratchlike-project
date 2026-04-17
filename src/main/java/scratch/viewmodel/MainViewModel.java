package scratch.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;
import scratch.model.Catalogue;
import scratch.model.Program;
import scratch.model.Scene;

import java.io.File;
import java.io.IOException;

public class MainViewModel {
    private final CatalogueViewModel catalogueViewModel;
    private final ProgramViewModel programViewModel;
    private final SceneViewModel sceneViewModel;
    private final InformationViewModel informationViewModel;
    private final Catalogue catalogue;
    private final Program program;
    private final Scene scene;

    private final BooleanProperty runtimeError = new SimpleBooleanProperty(false);


    public MainViewModel(Catalogue catalogue, Program program, Scene scene) {
        this.catalogueViewModel = new CatalogueViewModel(catalogue, this);
        this.programViewModel = new ProgramViewModel(program, this);
        this.sceneViewModel = new SceneViewModel(scene, this);
        this.informationViewModel = new InformationViewModel(scene);
        this.catalogue = catalogue;
        this.program = program;
        this.scene = scene;

        runtimeError.bind(sceneViewModel.runtimeErrorProperty());
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

    public boolean addToProgram(Action selectedAction, String from){
        return programViewModel.addToActionList(selectedAction, from);
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

    public void openFile(File file) throws IOException {
        if (file.exists())
            program.open(file);
    }

    public void saveFile(File file) throws IOException {
            program.saveFile(file);
    }

    public InformationViewModel getInformationViewModel() {
        return informationViewModel;
    }

    public BooleanProperty isValidProgramProperty() {
        return programViewModel.isValidProgramProperty();
    }

    public BooleanProperty runtimeErrorProperty() {
        return runtimeError;
    }
}
