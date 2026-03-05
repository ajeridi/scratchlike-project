package scratch.viewmodel;

public class MainViewModel {
    private final CatalogueViewModel catalogueViewModel;
    private final ProgramViewModel programViewModel;
    private final SceneViewModel sceneViewModel;

    public MainViewModel(CatalogueViewModel catalogueViewModel, ProgramViewModel programViewModel, SceneViewModel sceneViewModel) {
        this.catalogueViewModel = catalogueViewModel;
        this.programViewModel = programViewModel;
        this.sceneViewModel = sceneViewModel;
    }
}
