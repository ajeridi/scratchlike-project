package scratch.viewmodel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import scratch.model.*;
import scratch.model.actions.Action;

import java.util.List;

public class SceneViewModel {
    private final Scene scene;
    private final MainViewModel mainViewModel;
    private final IntegerProperty currentActionIndex = new SimpleIntegerProperty();
    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty programChanged = new SimpleBooleanProperty(false);
    private final IntegerProperty refreshCounter = new SimpleIntegerProperty(0); // compteur de refresh, qui va donner le signal a la vue de refresh le canvas
    private final BooleanProperty isExecuted = new SimpleBooleanProperty(false);
    private final BooleanProperty isAtLastAction = new SimpleBooleanProperty();
    private final BooleanProperty isValidProgram = new SimpleBooleanProperty();
    private BooleanProperty automaticModeSelected = new SimpleBooleanProperty(false);
    private BooleanProperty manualModeSelected = new SimpleBooleanProperty(false);
    private Action faultyAction;


    //execution automatique
    private final IntegerProperty fireSignal = new SimpleIntegerProperty(0); // sert de signal pour "cliquer sur le bouton Execute virtuellement
    private final Timeline timeline = new Timeline();
    private final DoubleProperty sliderSpeed = new SimpleDoubleProperty(); // propriete de la vitesse d'execution, qui sera binde au slider de la vue
    private final BooleanProperty programIsRunning = new SimpleBooleanProperty(false); // va permettre de savoir si le programme est en cours d'execution ou pas
    private final BooleanProperty runtimeError = new SimpleBooleanProperty(false);



    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();

    public SceneViewModel(Scene scene, MainViewModel mainViewModel) {
        this.scene = scene;
        this.mainViewModel = mainViewModel;

        currentActionIndex.bindBidirectional(scene.currentIndexProperty()); // pour les repeats
        addListeners();
    }

    public Cursor getCursor() {
        return scene.getCursor();
    }

    public List<MyLine> getLines() {
        return scene.getLines();
    }

    public ReadOnlyObjectProperty<Action> getCurrentSelectedActionProperty() {
        return mainViewModel.getCurrentSelectedActionProperty();
    }

    public ReadOnlyBooleanProperty isLoadedProperty() {
        return isLoaded;
    }


    public void addListeners() {
        ObservableList<Action> actions = mainViewModel.getActionsList();

        // si la liste d'actions du programme est modifiee, isLoaded = false
        // pour ca que si les actions on un target, on rajoute un observable sur le target
        actions.addListener((ListChangeListener<? super Action>) change -> {
            while (change.next()){
                programChanged.set(true); //on notifie que le programme a change
                isLoaded.set(false); // le programme doit etre recharge
            }
        });

        isValidProgram.bind(mainViewModel.isValidProgramProperty());
/*
                Bindings.createBooleanBinding(
                () -> {
                    Action current_pen_action = new Pen_Down();
                    for (Action action : actions){
                        if (action.getClass().equals(current_pen_action.getClass())){
                            return false; // deux action du meme type se suivent
                        }
                        // s'ils ne sont pas du meme type et que action est de type Pen
                        if (action instanceof Pen)
                            current_pen_action = action;
                    }
                    return true;
                },
                actions
*/

        isAtLastAction.bind(isAtLastAction());

        isAtLastAction.addListener(
                ((obs, o, n) -> {
                    if (n) // si on arrive en fin de programme
                        isExecuted.set(false); // fin de l'execution
                })
        );

        // listener sur le changement de valeur de vitesse, pour creer un nouveau keyframe et l'ajouter a la timeline
        sliderSpeed.addListener(
                ((observableValue, number, t1) -> {
                    var newKeyFrame = createKeyFrame();
                    // si on est en plein milieu d'execution
                    if (timeline.getStatus().equals(Animation.Status.RUNNING)){
                        timeline.stop(); // on pause l'execution
                        var keyframes = timeline.getKeyFrames();
                        keyframes.clear(); // on supprime le keyframe existant
                        keyframes.add(newKeyFrame);
                        timeline.play();
                    }
                })
        );
    }

    public void loadProgram() {
        currentActionIndex.bindBidirectional(mainViewModel.getSelectedIndexProperty()); // va binder l'index a celui de programView
        currentSelectedAction.bind(mainViewModel.getCurrentSelectedActionProperty());
        isLoaded.set(true);
        resetProgram();
    }

    public void resetProgram() {
        // reinitialiser la scene
        scene.reset();
        programChanged.set(false);
        isExecuted.set(false);
        currentActionIndex.set(-1); // forcer le changement d'index
        Variable_Table.getVariables().clear(); // clear la table de variables
        runtimeError.set(false); // runtime error cleared
        if (faultyAction != null) {
            faultyAction.setHasError(false); // on dit que l'action n'a plus d'erreur
            faultyAction = null;
        }
    }

    public void newProgram() {
        resetProgram();
        isLoaded.set(false); // dechargement
        programChanged.set(false); // changement de programme
        isExecuted.set(false); // pas d'execution
        refreshCounter.set(refreshCounter.get()+1);
    }

    public void executeProgram() {
        Variable_Table.getVariables().clear(); // clear la table de variables
        currentActionIndex.set(0);
        //isExecuting.set(true);
        isExecuted.set(true); // debut de l'execution
    }

    public void nextAction() {
        // executer l'action
        Action action = currentSelectedAction.get();
        if (action != null){
            // pendant l'execution il faut verifier la validite de l'action dans le cas ou elle a une variable entiere non variable
            if (!action.getType().equals("VAR_DECLARATION") && !action.isValidValue(action.getValue())) { // si la valeur de l'action n'est pas valide (on saute si c'est une var declaration)
                // on arrete le programme
                stopProgram();
                runtimeError.set(true); // on dit qu'on a une runtime error
                action.setHasError(true);
                faultyAction = action;
                return; // return pour ne pas executer l'action et passer a la suivante
            }
            currentSelectedAction.get().execute(scene);
        }
        // passer a l'action suivante
        currentActionIndex.set(currentActionIndex.get() + 1);
        //System.out.println("Next index = " + currentActionIndex.get());
    }

    public BooleanProperty runtimeErrorProperty(){
        return runtimeError;
    }

    public BooleanBinding isAtLastAction() {
        return Bindings.createBooleanBinding(
                () -> {
                    return currentActionIndex.get() == mainViewModel.getActionsSizeProperty().get();
                },
                currentActionIndex
        );
    }

    public ReadOnlyBooleanProperty programChangedProperty() {
        return programChanged;
    }

    public ReadOnlyIntegerProperty getCurrentActionIndexProperty(){
        return currentActionIndex;
    }

    public BooleanBinding isActionsListEmpty() {
        return mainViewModel.isActionsListEmpty();
    }

    public ReadOnlyIntegerProperty getRefreshCounterProperty() {
        return refreshCounter;
    }

    public ReadOnlyBooleanProperty isExecutedProperty() {
        return isExecuted;
    }

    public ReadOnlyBooleanProperty isValidProgramProperty() {
        return isValidProgram;
    }

    // correction iteration 1
    public BooleanBinding cannotExecute() {
        return Bindings.createBooleanBinding(
                () -> {
                    // ne peut pas s'executer si non charge, si charge et a la derniere action ou si programme non valide, si runtime error, si aucun mode  d'execution selectionne
                    return !isLoaded.get() || (isLoaded.get() && isAtLastAction.get()) || (!isValidProgram.get()) || runtimeError.get() || (!automaticModeSelected.get()  && !manualModeSelected.get()) ;
                },
                isLoaded, isAtLastAction, isValidProgram, runtimeError, automaticModeSelected, manualModeSelected
        );
    }


    public BooleanBinding canLoad() {
        return Bindings.createBooleanBinding(
                () -> {
                    return isValidProgram.get(); // on verifie si le programme est vide dans la validation du programme
                    //!isActionsListEmpty().get() && isValidProgram.get();
                },
                isActionsListEmpty(), isValidProgram
        );
    }

    public void automaticExecution() throws InterruptedException { // https://docs.oracle.com/javase/8/javafx/api/javafx/animation/Timeline.html
        if (!programIsRunning.get()) // si le programme n'est pas en cours d'execution, on l'execute
            executeProgram(); // on debute l'execution
        /*
        * On cree une timeline, qui va contenir un keyFrame, c'est a dire une une boucle qui va s'executer puis se reexecuter apres une certaine duree
        * Lors de l'execution automatique, ce qu'on fait c'est on appuie virtuellement sur le bouton suivant dans la vue, en envoyant un signal a la vue d'executer next action
        * une fois qu'on arrive a la derniere action, on arrete la timeline
        *
        * Comme on ne peut pas directement modifier la duration de la timeline, lorsqu'on modifie la valeur du slider, il faut generer un nouveau KeyFrame
        * et l'ajouter a la timeline
        * https://stackoverflow.com/questions/19549852/javafx-binding-timelines-duration-to-a-property
        * */
        // tant qu'on est dans la boucle, on appuie sur le bouton virtuellement avec fire
        timeline.getKeyFrames().add(createKeyFrame());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        programIsRunning.set(true);
    }

    private KeyFrame createKeyFrame(){
        return new KeyFrame(
                Duration.seconds(getSliderSpeed()),
                actionEvent -> {
                    fireSignal.set(fireSignal.get() + 1);
                    if (isAtLastAction.get()){
                        timeline.stop();
                    }
                }
        );
    }


    private void refreshView() {
        fireSignal.add(1);
    }


    public int getFireSignal() {
        return fireSignal.get();
    }

    public IntegerProperty fireSignalProperty() {
        return fireSignal;
    }

    public double getSliderSpeed() {
        return sliderSpeed.get();
    }

    public DoubleProperty sliderSpeedProperty() {
        return sliderSpeed;
    }


    public boolean isProgramIsRunning() {
        return programIsRunning.get();
    }

    public BooleanProperty programIsRunningProperty() {
        return programIsRunning;
    }

    public void stopProgram() {
        timeline.stop(); // on arrete la timeline
        programIsRunningProperty().set(false); // on que le programme est arrete
    }

    public BooleanProperty automaticModeSelectedProperty() {
        return automaticModeSelected;
    }

    public BooleanProperty manualModeSelectedProperty(){
        return manualModeSelected;
    }
}
