package scratch.viewmodel;

import javafx.beans.property.*;
import scratch.model.Action;
import scratch.model.Cursor;
import scratch.model.Scene;

public class SceneViewModel {
    private final Scene scene;
    private final MainViewModel mainViewModel;
    private final IntegerProperty currentActionIndex = new SimpleIntegerProperty();
    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty isExecuting = new SimpleBooleanProperty(false);


    private final ObjectProperty<Action> currentSelectedAction = new SimpleObjectProperty<>();

    public SceneViewModel(Scene scene, MainViewModel mainViewModel) {
        this.scene = scene;
        this.mainViewModel = mainViewModel;
    }

    public Cursor getCursor() {
        return scene.getCursor();
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

    public void loadProgram() {
        currentActionIndex.bindBidirectional(mainViewModel.getSelectedIndexProperty()); // va binder l'index a celui de programView
        currentSelectedAction.bind(mainViewModel.getCurrentSelectedActionProperty());
        currentActionIndex.set(-1); // forcer le changement d'index
        isLoaded.set(true);
    }
    public void executeProgram() {
        currentActionIndex.set(0);
        isExecuting.set(true);
    }
}
