package scratch.view;

import com.sun.tools.javac.Main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import scratch.model.Catalogue;
import scratch.viewmodel.CatalogueViewModel;
import scratch.viewmodel.MainViewModel;

public class MainView extends VBox {

    private final CatalogueView catalogueView;
    private final ProgramView programView;
    private final SceneView sceneView;
    private final MainViewModel vm;
    private final MenuBar menuBar = new MenuBar();
    private final HBox mainContent = new HBox();

    public MainView(MainViewModel vm){
        this.vm = vm;
        this.catalogueView = new CatalogueView(vm.getCatalogueViewModel());
        this.programView = new ProgramView(vm.getProgramViewModel());
        this.sceneView = new SceneView(vm.getSceneViewModel());


        setMargins();
        setMenu();

        mainContent.getChildren().addAll(catalogueView, programView, sceneView);
        this.getChildren().addAll(menuBar, mainContent);

    }

    private void setMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(newFile, open, saveAs, exit);
        menuBar.getMenus().add(fileMenu);
        VBox.setVgrow(menuBar, Priority.ALWAYS);
    }

    private void setMargins(){
        this.setSpacing(20);
        setMargin(mainContent, new Insets(20, 20, 20, 20));
        mainContent.setSpacing(20);
        setMargin(catalogueView, new Insets(20, 20, 20, 20));
        setMargin(programView, new Insets(20, 20, 20, 20));
        setMargin(sceneView, new Insets(20, 20, 20, 20));
    }

    /*




     */
}
