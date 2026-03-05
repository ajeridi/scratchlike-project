package scratch.viewmodel;

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
        this.sceneViewModel = new SceneViewModel(scene);
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
}
