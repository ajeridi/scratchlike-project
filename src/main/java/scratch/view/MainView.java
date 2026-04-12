package scratch.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scratch.viewmodel.MainViewModel;

import java.io.File;
import java.io.IOException;

public class MainView extends VBox {

    private final CatalogueView catalogueView;
    private final ProgramView programView;
    private final SceneView sceneView;
    private final InformationView informationView;
    private final MainViewModel mainViewModel;
    private final Stage stage;
    private final MenuBar menuBar = new MenuBar();
    private final Menu fileMenu = new Menu("File");
    private final MenuItem newFile = new MenuItem("New");
    private final MenuItem open = new MenuItem("Open");
    private final MenuItem saveAs = new MenuItem("Save As");
    private final MenuItem exit = new MenuItem("Exit");
    private final HBox mainContent = new HBox();
    private final VBox sceneAndInformation = new VBox();
    private final FileChooser fileChooser = new FileChooser();


    public MainView(MainViewModel vm, Stage stage){
        this.mainViewModel = vm;
        this.stage = stage;
        this.catalogueView = new CatalogueView(vm.getCatalogueViewModel());
        this.programView = new ProgramView(vm.getProgramViewModel());
        this.sceneView = new SceneView(vm.getSceneViewModel());
        this.informationView = new InformationView(vm.getInformationViewModel());

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Scratch files", "*.scr")
        );

        setMargins();
        setMenu();

        setMenuActions();
        sceneAndInformation.setSpacing(2);
        sceneAndInformation.getChildren().addAll(sceneView, informationView);

        mainContent.getChildren().addAll(catalogueView, programView, sceneAndInformation);
        this.getChildren().addAll(menuBar, mainContent);

    }

    private void setMenuActions() {
        newFile.setOnAction(actionEvent -> {
            mainViewModel.newFile();
        });

        open.setOnAction(
                actionEvent -> {
                    try {
                        openFile();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());;
                    }
                }
        );

        saveAs.setOnAction(
                actionEvent -> {
                    try {
                        saveFile();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());;
                    }
                }
        );

        exit.setOnAction( actionEvent -> {
            Platform.exit();
        });


    }

    private void setMenu() {
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

    public void openFile() throws IOException {
        // transferer le fichier au VM
        File chosenFile = fileChooser.showOpenDialog(stage);
        if (chosenFile != null)
            mainViewModel.openFile(chosenFile);
    }

    public void saveFile() throws IOException {
        File fileToSaveTo = fileChooser.showSaveDialog(stage);
        if (fileToSaveTo != null)
            mainViewModel.saveFile(fileToSaveTo);
    }
}
