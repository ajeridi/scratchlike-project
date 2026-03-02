package scratch.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SceneView extends VBox {

    private final Label titleLbl = new Label("Scène");

    private final StackPane canvasHolder = new StackPane();

    private final Canvas sceneCanvas = new Canvas(500, 500);

    private final Button loadBtn = new Button("Charger");

    private final Button executeBtn = new Button("Exécuter");

    public SceneView() {
        HBox buttons = makeButtonsHbox();
        makeSceneCanvas();

        this.getChildren().addAll(titleLbl, canvasHolder, buttons);


        setMargin(canvasHolder, new Insets(10, 0, 10, 0));
        buttons.setAlignment(Pos.CENTER);
    }

    private void makeSceneCanvas() {
        // les dessins sont à des fins de tests, ils seront supprimés

        // graphics context
        GraphicsContext graphics_context =
                sceneCanvas.getGraphicsContext2D();

        // set fill for rectangle
        graphics_context.setFill(Color.RED);
        graphics_context.fillRect(20, 20, 70, 70);

        // set fill for oval
        graphics_context.setFill(Color.BLUE);
        graphics_context.fillOval(30, 30, 70, 70);


        canvasHolder.getChildren().add(sceneCanvas);
        canvasHolder.setStyle("-fx-background-color: white");


    }

    public HBox makeButtonsHbox() {
        return new HBox(loadBtn, executeBtn);
    }
}
