package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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
import scratch.model.Cursor;
import scratch.model.MyLine;
import scratch.model.Point;
import scratch.viewmodel.SceneViewModel;

public class SceneView extends VBox {

    private final double TURTLE_SIZE = 20;
    private final double TURTLE_HEAD_SIZE = 7;
    private final double CANVAS_DIMENSION = 500;

    private final Label titleLbl = new Label("Scène");

    private final StackPane canvasHolder = new StackPane(); // va tenir le canvas

    private final Canvas sceneCanvas = new Canvas(CANVAS_DIMENSION, CANVAS_DIMENSION);

    private final Button loadBtn = new Button("Charger");

    private final Button executeBtn = new Button("Exécuter");

    private final SceneViewModel sceneViewModel;


    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty isExecuting = new SimpleBooleanProperty(false);
    private final BooleanProperty atLastAction = new SimpleBooleanProperty();
    private final BooleanProperty programChanged = new SimpleBooleanProperty();
    private final IntegerProperty refreshCounter = new SimpleIntegerProperty();



    public SceneView(SceneViewModel sceneViewModel) {
        this.sceneViewModel = sceneViewModel;
        HBox buttons = makeButtonsHbox();
        setBindings();
        addListeners();
        setCanvas();
        this.getChildren().addAll(titleLbl, canvasHolder, buttons);


        setActions();
        buttons.setAlignment(Pos.CENTER);
    }

    private void setCanvas() {
        canvasHolder.getChildren().add(sceneCanvas);
        setMargin(canvasHolder, new Insets(10, 0, 10, 0));
        refreshCanvas();
    }

    private void setBindings() {

        isLoaded.bind(sceneViewModel.isLoadedProperty());
        isExecuting.bind(sceneViewModel.isExecutingProperty());
        atLastAction.bind(sceneViewModel.isAtLastAction());
        programChanged.bind(sceneViewModel.programChangedProperty());
        refreshCounter.bind(sceneViewModel.getRefreshCounterProperty());

        loadBtn.textProperty().bind(Bindings.createStringBinding(
                () -> {
                   return  isLoaded.get() ? "Re-initialiser" : "Charger";
                },
                isLoaded
        ));

        executeBtn.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    return isLoaded.get() && currentActionIndexProperty().get() == -1 || isExecuting.not().get() ? "Executer" : "Suivant";
                },
                isExecuting
        ));

        executeBtn.disableProperty().bind(atLastAction.or(programChanged));
        loadBtn.disableProperty().bind(sceneViewModel.isActionsListEmpty());
    }

    private void addListeners() {
        refreshCounter.addListener(
                ((observableValue, number, t1) -> {
                    refreshCanvas();
                })
        );
    }

    private void setActions() {
        loadBtn.setOnAction(actionEvent -> {
            if (!isLoaded.get())
                loadProgram();
            // sinon on  reinitialise
            resetProgram();
        });

        executeBtn.setOnAction(actionEvent -> {
            if (!isExecuting.get())
                executeProgram();
            else
                nextAction();
        });
    }

    private void loadProgram() {
        sceneViewModel.loadProgram();
    }

    private void executeProgram() {
        sceneViewModel.executeProgram();
    }

    private void resetProgram() {
        sceneViewModel.resetProgram();

        refreshCanvas();
    }


    private void nextAction() {
       sceneViewModel.nextAction();
       refreshCanvas();
    }

    private ReadOnlyIntegerProperty currentActionIndexProperty() {
        return sceneViewModel.getCurrentActionIndexProperty();
    }


    private void refreshCanvas() {
        // graphics context
        GraphicsContext gc = sceneCanvas.getGraphicsContext2D();

        gc.clearRect(0,0, CANVAS_DIMENSION, CANVAS_DIMENSION);

        // dessin du quadrillage
        drawGrid(gc);
        // dessin des lignes
        drawLines(gc);
        // dessine turtle
        drawTurtle(gc);

        canvasHolder.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");
    }

    private void drawLines(GraphicsContext gc){
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);

        double offset = CANVAS_DIMENSION / 2;
        for (MyLine line : sceneViewModel.getLines()){
            gc.strokeLine(line.getStartX() + offset, line.getStartY() + offset, line.getEndX() + offset, line.getEndY() + offset);
        }
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

    private void drawTurtle(GraphicsContext gc) {
        Cursor cursor = sceneViewModel.getCursor();
        Point origin = cursor.getOrigine();
        double x = origin.getX() + CANVAS_DIMENSION/2;
        double y = origin.getY() + CANVAS_DIMENSION/2;
        double angle = cursor.getDirection();
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
