package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ExecutionView extends VBox {
    /*
    * Classe qui va contenir les boutons ainsi que le speed slider, tout ce qui est en relation avec l'execution du programme
    */

    private final ExecutionViewModel executionViewModel;

    private final Button loadBtn = new Button("Charger");
    private final Button executeBtn = new Button("Exécuter");


    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty atLastAction = new SimpleBooleanProperty();
    private final BooleanProperty programChanged = new SimpleBooleanProperty();
    private final IntegerProperty refreshCounter = new SimpleIntegerProperty(); // sert de recepteur de signal de refresh de la part du VM
    private final BooleanProperty isExecuted = new SimpleBooleanProperty();
    private final BooleanProperty isValidProgram = new SimpleBooleanProperty();


    private final CheckBox automaticModeCheckBox = new CheckBox("Execution automatique");
    private final CheckBox manualModeCheckBox = new CheckBox("Execution manuelle");
    private final HBox executionModeBox = new HBox();
    private final Slider automaticExecutionSpeedSlider = new Slider(0.01, 5, 0.25);

    private final IntegerProperty nextActionSignal = new SimpleIntegerProperty(0); // va binder au view model pour signaler l'appui sur le bouton execute
    private final Button stopBtn = new Button("Arreter");


    public ExecutionView(ExecutionViewModel executionViewModel){
        this.executionViewModel = executionViewModel;
        HBox buttons = makeButtonsHbox();
        setBindings();
        addListeners();
        setExecutionModeBox();
        setAutomaticExecutionSpeedSlider();
        setActions();
        buttons.setAlignment(Pos.CENTER);

        this.getChildren().addAll(executionModeBox, buttons, automaticExecutionSpeedSlider);
    }



    public HBox makeButtonsHbox() {
        return new HBox(loadBtn, executeBtn, stopBtn);
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


    private void setBindings() {
        isLoaded.bind(executionViewModel.isLoadedProperty());
        isExecuted.bind(executionViewModel.isExecutedProperty());
        atLastAction.bind(executionViewModel.isAtLastAction());
        programChanged.bind(executionViewModel.programChangedProperty());

        isValidProgram.bind(executionViewModel.isValidProgramProperty());

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

        automaticModeCheckBox.selectedProperty().bindBidirectional(executionViewModel.automaticModeSelectedProperty());
        manualModeCheckBox.selectedProperty().bindBidirectional(executionViewModel.manualModeSelectedProperty());
        // correction iteration 1
        executeBtn.disableProperty().bind(executionViewModel.cannotExecute());
        loadBtn.disableProperty().bind(executionViewModel.canLoad().not());


        nextActionSignal.bind(executionViewModel.fireSignalProperty()); // signal d'execution de la prochaine action, envoye par la VM pour signaler l'execution de la prochaine Action
        automaticExecutionSpeedSlider.valueProperty().bindBidirectional(executionViewModel.sliderSpeedProperty()); // binding entre la valeur de la vitesse de la vue et celle dans le VM

        // le bouton stop doit etre visible uniquement si on est en mode execution automatique
        stopBtn.visibleProperty().bind(automaticModeCheckBox.selectedProperty());
        // bouton stop actif uniquement si le program est en cours d'execution automatique
        stopBtn.disableProperty().bind(programIsRunning().not());

    }


    private BooleanProperty programIsRunning() {
        // retourne si le programme est en cours d'execution (donc si timeline is running)
        return executionViewModel.programIsRunningProperty();

    }



    private void addListeners() {


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

}
