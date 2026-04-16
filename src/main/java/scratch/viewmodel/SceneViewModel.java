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
import scratch.model.actions.Pen;
import scratch.model.actions.Pen_Down;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SceneViewModel {
    private final Scene scene;
    private final MainViewModel mainViewModel;
    private final IntegerProperty currentActionIndex = new SimpleIntegerProperty();
    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty programChanged = new SimpleBooleanProperty(false);
    private final IntegerProperty refreshCounter = new SimpleIntegerProperty(0);
    private final BooleanProperty isExecuted = new SimpleBooleanProperty(false);
    private final BooleanProperty isAtLastAction = new SimpleBooleanProperty();
    private final BooleanProperty isValidProgram = new SimpleBooleanProperty();


    //execution automatique
    private final IntegerProperty fireSignal = new SimpleIntegerProperty(0); // sert de signal pour "cliquer sur le bouton Execute virtuellement
    private final Timeline timeline = new Timeline();
    private final DoubleProperty sliderSpeed = new SimpleDoubleProperty(); // propriete de la vitesse d'execution, qui sera binde au slider de la vue


    private final BooleanProperty programIsRunning = new SimpleBooleanProperty(false); // va permettre de savoir si le programme est en cours d'execution ou pas



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
        actions.addListener((ListChangeListener<? super Action>) change -> {
            while (change.next()){
                programChanged.set(true);
                isLoaded.set(false);
            }
        });

        isValidProgram.bind(Bindings.createBooleanBinding(
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
        ));

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
        Variable_Table.getVariables().clear();
    }

    public void newProgram() {
        resetProgram();
        isLoaded.set(false); // dechargement
        programChanged.set(false); // changement de programme
        isExecuted.set(false); // pas d'execution
        refreshCounter.set(refreshCounter.get()+1);
    }

    public void executeProgram() {
        currentActionIndex.set(0);
        //isExecuting.set(true);
        isExecuted.set(true); // debut de l'execution
    }

    public void nextAction() {
        // executer l'action
        if (currentSelectedAction.get() != null)
            currentSelectedAction.get().execute(scene);
        // passer a l'action suivante
        currentActionIndex.set(currentActionIndex.get() + 1);
        System.out.println("Next index = " + currentActionIndex.get());
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
    public BooleanBinding canExecute() {
        return Bindings.createBooleanBinding(
                () -> {
                    return !isLoaded.get() || (isLoaded.get() && isAtLastAction.get()) || (!isValidProgram.get());
                },
                isLoaded, isAtLastAction, isValidProgram
        );
    }

    public BooleanBinding canLoad() {
        return Bindings.createBooleanBinding(
                () -> {
                    return isActionsListEmpty().get() || !isValidProgram.get();
                },
                isActionsListEmpty(), isValidProgram
        );
    }

    public void automaticExecution() throws InterruptedException { // https://docs.oracle.com/javase/8/javafx/api/javafx/animation/Timeline.html
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
}
