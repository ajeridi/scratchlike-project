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

    private final double CANVAS_DIMENSION = 600;

    private final Label titleLbl = new Label("Scène");

    private final StackPane canvasHolder = new StackPane(); // va tenir le canvas

    private final Canvas sceneCanvas = new Canvas(CANVAS_DIMENSION, CANVAS_DIMENSION);

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
        // graphics context
        GraphicsContext gc =
                sceneCanvas.getGraphicsContext2D();
        // dessin du quadrillage
        drawGrid(gc);

        canvasHolder.getChildren().add(sceneCanvas);
        canvasHolder.setStyle("-fx-background-color: white");
    }

    private void drawGrid(GraphicsContext gc) {
        double step = CANVAS_DIMENSION / 10; // largeur entre deux lignes
        // Set a stroke color, and width
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        // dessine les lignes pour avoir 10
        for (double row = 0; row <= CANVAS_DIMENSION; row += step){
            gc.strokeLine(0, row, CANVAS_DIMENSION, row);
        }
        // dessine les colonnes
        for (double col = 0; col <= CANVAS_DIMENSION; col += step){
            gc.strokeLine(col, 0, col, CANVAS_DIMENSION);
        }
    }

    public HBox makeButtonsHbox() {
        return new HBox(loadBtn, executeBtn);
    }
}
