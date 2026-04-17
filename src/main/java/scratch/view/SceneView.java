package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    private final double CANVAS_DIMENSION = 400;

    private final Label titleLbl = new Label("Scène");

    private final StackPane canvasHolder = new StackPane(); // va tenir le canvas

    private final Canvas sceneCanvas = new Canvas(CANVAS_DIMENSION, CANVAS_DIMENSION);

    private final Button loadBtn = new Button("Charger");

    private final Button executeBtn = new Button("Exécuter");

    private final SceneViewModel sceneViewModel;


    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty atLastAction = new SimpleBooleanProperty();
    private final BooleanProperty programChanged = new SimpleBooleanProperty();
    private final IntegerProperty refreshCounter = new SimpleIntegerProperty(); // sert de recepteur de signal de refresh de la part du VM
    private final BooleanProperty isExecuted = new SimpleBooleanProperty();
    private final BooleanProperty isValidProgram = new SimpleBooleanProperty();

    // a ajouter plus tard a une vue ExecutionModeView
    private final CheckBox automaticModeCheckBox = new CheckBox("Execution automatique");
    private final CheckBox manualModeCheckBox = new CheckBox("Execution manuelle");
    private final HBox executionModeBox = new HBox();
    private final Slider automaticExecutionSpeedSlider = new Slider(0.01, 5, 0.25);

    private final IntegerProperty nextActionSignal = new SimpleIntegerProperty(0); // va binder au view model pour signaler l'appui sur le bouton execute
    private final Button stopBtn = new Button("Arreter");



    public SceneView(SceneViewModel sceneViewModel) {
        this.sceneViewModel = sceneViewModel;
        HBox buttons = makeButtonsHbox();
        setBindings();
        addListeners();
        setCanvas();
        setExecutionModeBox();
        setAutomaticExecutionSpeedSlider();
        this.getChildren().addAll(titleLbl, canvasHolder,executionModeBox, buttons, automaticExecutionSpeedSlider);
        setActions();
        buttons.setAlignment(Pos.CENTER);
    }

    private void setAutomaticExecutionSpeedSlider() {
        automaticExecutionSpeedSlider.setShowTickLabels(true);
        automaticExecutionSpeedSlider.setShowTickMarks(true);
        automaticExecutionSpeedSlider.setMajorTickUnit(1);

        automaticExecutionSpeedSlider.visibleProperty().bind(automaticModeCheckBox.selectedProperty());
        automaticExecutionSpeedSlider.setPadding(new Insets(5));
        automaticExecutionSpeedSlider.setValue(automaticExecutionSpeedSlider.getMin() + 0.01); // on set pour envoyer la valeur au VM a l'initalisation, car il commencait a 0;
    }

    private void setExecutionModeBox() {
        executionModeBox.setSpacing(10);
        executionModeBox.getChildren().addAll(automaticModeCheckBox, manualModeCheckBox);
        executionModeBox.setAlignment(Pos.BASELINE_CENTER);
        executionModeBox.setPadding(new Insets(10));
    }

    private void setCanvas() {
        canvasHolder.getChildren().add(sceneCanvas);
        setMargin(canvasHolder, new Insets(10, 0, 10, 0));
        refreshCanvas();
    }

    private void setBindings() {

        isLoaded.bind(sceneViewModel.isLoadedProperty());
        isExecuted.bind(sceneViewModel.isExecutedProperty());
        atLastAction.bind(sceneViewModel.isAtLastAction());
        programChanged.bind(sceneViewModel.programChangedProperty());
        refreshCounter.bind(sceneViewModel.getRefreshCounterProperty());
        isValidProgram.bind(sceneViewModel.isValidProgramProperty());

        loadBtn.textProperty().bind(Bindings.createStringBinding(
                () -> {
                   return  isLoaded.get() ? "Re-initialiser" : "Charger";
                },
                isLoaded
        ));

        executeBtn.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    return isExecuted.not().get() ? "Executer" : "Suivant";
                },
                isExecuted
        ));

        // Logique applicative a bouger dans le VM
/*
        executeBtn.disableProperty().bind(isLoaded.not().or(isLoaded.and(sceneViewModel.isAtLastAction())).or(isValidProgram.not()));
        loadBtn.disableProperty().bind(sceneViewModel.isActionsListEmpty().or(isValidProgram.not()));
*/
        automaticModeCheckBox.selectedProperty().bindBidirectional(sceneViewModel.automaticModeSelectedProperty());
        manualModeCheckBox.selectedProperty().bindBidirectional(sceneViewModel.manualModeSelectedProperty());
        // correction iteration 1
        executeBtn.disableProperty().bind(sceneViewModel.cannotExecute());
        loadBtn.disableProperty().bind(sceneViewModel.canLoad().not());


        nextActionSignal.bind(sceneViewModel.fireSignalProperty()); // signal d'execution de la prochaine action, envoye par la VM pour signaler l'execution de la prochaine Action
        automaticExecutionSpeedSlider.valueProperty().bindBidirectional(sceneViewModel.sliderSpeedProperty()); // binding entre la valeur de la vitesse de la vue et celle dans le VM

        // le bouton stop doit etre visible uniquement si on est en mode execution automatique
        stopBtn.visibleProperty().bind(automaticModeCheckBox.selectedProperty());
        // bouton stop actif uniquement si le program est en cours d'execution automatique
        stopBtn.disableProperty().bind(programIsRunning().not());
    }

    private BooleanProperty programIsRunning() {
        // retourne si le programme est en cours d'execution (donc si timeline is running)
        return sceneViewModel.programIsRunningProperty();

    }

    private void addListeners() {
        refreshCounter.addListener(
                ((observableValue, number, t1) -> {
                    refreshCanvas();
                })
        );

        automaticModeCheckBox.selectedProperty().addListener(
                ((observableValue, aBoolean, t1) -> {
                    // si l'autre mode est deselectionne, on le deselectionne
                    if (manualModeCheckBox.isSelected()) {
                        manualModeCheckBox.setSelected(false);
                    }
                })
        );

        manualModeCheckBox.selectedProperty().addListener(
                ((observableValue, aBoolean, t1) -> {
                    // si l'autre mode est selectionne, on le deselectionne
                    if (automaticModeCheckBox.isSelected()){
                        automaticModeCheckBox.setSelected(false);
                    }
                })
        );

        nextActionSignal.addListener(
                // signal recu par le view model pour appuyer sur le bouton execute lors de l'execution automatique
                ((observableValue, number, t1) -> {
                    nextAction();
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

            if (automaticModeCheckBox.isSelected() ){
                // execution automatique
                try {
                    sceneViewModel.automaticExecution();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                if (!isExecuted.get())
                    executeProgram();
                else
                    nextAction();
            }
        });

        stopBtn.setOnAction( // stopper le programme
                actionEvent -> {
                    sceneViewModel.stopProgram();
                }
        );
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
        return new HBox(loadBtn, executeBtn, stopBtn);
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
