package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.util.List;

public class SceneViewModel {
    private final Scene scene;
    private final MainViewModel mainViewModel;
    private final IntegerProperty currentActionIndex = new SimpleIntegerProperty();
    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty isExecuting = new SimpleBooleanProperty(false);
    private final BooleanProperty programChanged = new SimpleBooleanProperty(false);



    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();

    public SceneViewModel(Scene scene, MainViewModel mainViewModel) {
        this.scene = scene;
        this.mainViewModel = mainViewModel;

        isExecuting.bind(programChanged.not().and(isAtLastAction().not()));

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

    public ReadOnlyBooleanProperty isExecutingProperty() {
        return isExecuting;
    }

    public void addListeners() {
        ObservableList<Action> actions = mainViewModel.getActionsList();

        // si la liste d'actions du programme est modifiee, isLoaded = false et isExecuting = false
        actions.addListener((ListChangeListener<? super Action>) change -> {
            while (change.next()){
                programChanged.set(true);
                isLoaded.set(false);
            }
        });
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
        currentActionIndex.set(-1); // forcer le changement d'index
    }

    public void executeProgram() {
        currentActionIndex.set(0);
        isExecuting.set(true);
    }

    public void nextAction() {
        // executer l'action
        if (currentSelectedAction.get() != null)
            currentSelectedAction.get().execute(scene);
        // passer a l'action suivante
        currentActionIndex.set(currentActionIndex.get() + 1);
        //fin de l'execution
//        if (isAtLastAction().get())
//            isExecuting.set(false);
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


}
