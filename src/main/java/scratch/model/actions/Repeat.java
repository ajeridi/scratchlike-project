package scratch.model.actions;

import scratch.model.ActionList;
import scratch.model.Scene;

public class Repeat extends Action {
    private static final String DEFAULT_REPEAT_NAME = "Repeter";
    private static final String DEFAULT_REPEAT_COLOR = "orange";


    private final ActionList actions = new ActionList();

    public Repeat(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Repeat(String nom, String color) {
        super(nom, color);
    }

    public Repeat() {
        super(DEFAULT_REPEAT_NAME, DEFAULT_REPEAT_COLOR);
    }

    @Override
    public void execute(Scene scene) {
        var currentLoopIndexes = scene.getCurrentLoopIndexes();
        var repeat_index = scene.currentIndexProperty().get();
        currentLoopIndexes.add(repeat_index);
        int counter = 1;
        var loopCounters = scene.getLoopCounters();
        loopCounters.push(counter);
        scene.getLoopActions().push(this);
    }

    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public Action create_default() {
        return new Repeat(0, DEFAULT_REPEAT_NAME, DEFAULT_REPEAT_COLOR);
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public String valueUnit() {
        return "fois";
    }

    @Override
    public String getType() {
        return "REPEAT";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }

    @Override
    public String toString() {
        return super.toString() + " fois";
    }

    @Override
    public ActionList getActions() {
        return actions;
    }
}
