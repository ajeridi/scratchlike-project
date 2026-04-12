package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import scratch.model.*;
import scratch.model.actions.Action;
import scratch.model.actions.Pen;
import scratch.model.actions.Pen_Down;

import java.util.List;

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



    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();

    public SceneViewModel(Scene scene, MainViewModel mainViewModel) {
        this.scene = scene;
        this.mainViewModel = mainViewModel;


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
        scene.getVariables().clear();
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

}
