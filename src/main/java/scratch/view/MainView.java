package scratch.view;

import com.sun.tools.javac.Main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import scratch.model.Catalogue;
import scratch.viewmodel.CatalogueViewModel;
import scratch.viewmodel.MainViewModel;

public class MainView extends HBox {

    private final CatalogueView catalogueView;
    private final ProgramView programView;
    private final SceneView sceneView;
    private final MainViewModel vm;

    public MainView(MainViewModel vm, CatalogueView catalogueView, ProgramView programView, SceneView sceneView){
        this.vm = vm;
        this.catalogueView = catalogueView;
        this.programView = programView;
        this.sceneView = sceneView;
        setMargins();
        this.getChildren().addAll(catalogueView, programView, sceneView);
    }

    private void setMargins(){
        setMargin(catalogueView, new Insets(20, 20, 20, 20));
        setMargin(programView, new Insets(20, 20, 20, 20));
        setMargin(sceneView, new Insets(20, 20, 20, 20));
    }

    /*




     */
}
