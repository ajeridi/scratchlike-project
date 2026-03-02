package scratch.view;

import com.sun.tools.javac.Main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import scratch.model.Catalogue;

public class MainView extends HBox {

    private final CatalogueView catalogueView = new CatalogueView();
    private final ProgramView programView = new ProgramView();
    private final SceneView sceneView = new SceneView();

    public MainView(){
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
