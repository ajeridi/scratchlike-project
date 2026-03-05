package scratch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scratch.model.Catalogue;
import scratch.model.Program;
import scratch.view.CatalogueView;
import scratch.view.MainView;
import scratch.view.ProgramView;
import scratch.view.SceneView;
import scratch.viewmodel.CatalogueViewModel;
import scratch.viewmodel.MainViewModel;
import scratch.viewmodel.ProgramViewModel;
import scratch.viewmodel.SceneViewModel;

public class App extends Application {


    @Override
    public void start(Stage primaryStage) {

        // modeles
        var catalogue = new Catalogue();
        var program = new Program();
        var sceneModel = new scratch.model.Scene();

        // vm
        var catalogueViewModel = new CatalogueViewModel(catalogue);
        var programViewModel = new ProgramViewModel(program);
        var sceneViewModel = new SceneViewModel(sceneModel);

        // vues
        var catalogueView = new CatalogueView(catalogueViewModel);
        var programView = new ProgramView(programViewModel);
        var sceneView = new SceneView(sceneViewModel);


        var mainViewModel = new MainViewModel(catalogueViewModel, programViewModel, sceneViewModel);

        MainView mainView = new MainView(mainViewModel, catalogueView, programView, sceneView);


        Scene scene = new Scene(mainView, 1600, 900 );
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();

       /*
        Label label = new Label("Hello ANC4 2526");
        label.setFont(new Font(25));
        Pane pane = new StackPane();
        pane.getChildren().add(label);
        Scene scene = new Scene(pane, 640, 480);
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();
        */


    }

    public static void main(String[] args) {
        launch(args);
    }
}