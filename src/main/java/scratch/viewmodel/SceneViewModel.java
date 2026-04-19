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

    public SceneViewModel(Scene scene, MainViewModel mainViewModel) {
        this.scene = scene;
        this.mainViewModel = mainViewModel;
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


    public void loadProgram() {
        resetProgram();
    }

    public void resetProgram() {
        // reinitialiser la scene
        scene.reset();
    }

    public void newProgram() {
        resetProgram();

    }
}
