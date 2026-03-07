package scratch.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import scratch.viewmodel.SceneViewModel;

public class SceneView extends VBox {

    private final double TURTLE_SIZE = 20;
    private final double TURTLE_HEAD_SIZE = 7;
    private final double CANVAS_DIMENSION = 600;

    private final Label titleLbl = new Label("Scène");

    private final StackPane canvasHolder = new StackPane(); // va tenir le canvas

    private final Canvas sceneCanvas = new Canvas(CANVAS_DIMENSION, CANVAS_DIMENSION);

    private final Button loadBtn = new Button("Charger");

    private final Button executeBtn = new Button("Exécuter");

    private final SceneViewModel vm;

    public SceneView(SceneViewModel vm) {
        this.vm = vm;
        HBox buttons = makeButtonsHbox();
        drawCanvas();
        this.getChildren().addAll(titleLbl, canvasHolder, buttons);
        setMargin(canvasHolder, new Insets(10, 0, 10, 0));
        buttons.setAlignment(Pos.CENTER);
    }

    private void drawCanvas() {
        // graphics context
        GraphicsContext gc = sceneCanvas.getGraphicsContext2D();
        // dessin du quadrillage
        drawGrid(gc);
        // dessine turtle
        drawTurtle(gc, CANVAS_DIMENSION/2, CANVAS_DIMENSION/2, 0);
        canvasHolder.getChildren().add(sceneCanvas);
        canvasHolder.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");
    }

    private void drawGrid(GraphicsContext gc) {
        double step = CANVAS_DIMENSION / 10; // largeur entre deux lignes
        // Set a stroke color, and width
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.0);

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

    private void drawTurtle(GraphicsContext gc, double x, double y, double angle) {
        double xP1 = x + TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.cos(Math.toRadians(angle + 90)),
                yP1 = y - TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.sin(Math.toRadians(angle + 90)),
                xP2 = x - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle)),
                yP2 = y + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle)),
                xP3 = x + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle)),
                yP3 = y - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle));

        double[] xPoints = {xP1, xP2, xP3};
        double[] yPoints = {yP1, yP2, yP3};
        double radius = TURTLE_HEAD_SIZE / 2;

        gc.setFill(Color.DEEPSKYBLUE);
        gc.setStroke(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
        gc.strokePolygon(xPoints, yPoints, 3);

        gc.setFill(Color.DARKGREEN);
        gc.fillOval(xP1-radius, yP1-radius, radius*2, radius*2);
        gc.strokeOval(xP1-radius, yP1-radius, radius*2, radius*2);
    }
}
