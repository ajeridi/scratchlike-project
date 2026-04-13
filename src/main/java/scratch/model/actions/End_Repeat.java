package scratch.model.actions;

import scratch.model.Scene;

public class End_Repeat extends Action{

    private static final String DEFAULT_END_REPEAT_NAME = "Fin repeter";
    private static final String DEFAULT_END_REPEAT_COLOR = "orange";

    public End_Repeat(double value, String nom, String color) {
        super(value, nom, color);
    }
    public End_Repeat() {
        super(DEFAULT_END_REPEAT_NAME, DEFAULT_END_REPEAT_COLOR);
    }

    public End_Repeat(End_Repeat endRepeat) {
        super(endRepeat.getValue(), endRepeat.getNom(), endRepeat.getColor());
    }

    @Override
    public void execute(Scene scene) {
        var loopCounters = scene.getLoopCounters();
        var counter = loopCounters.peek();
        var loopIndexes = scene.getCurrentLoopIndexes();
        Action currentLoopAction = scene.getLoopActions().peek();
        if (counter != currentLoopAction.getValue()){
            scene.currentIndexProperty().set(loopIndexes.peek());
            loopCounters.pop();
            counter++;
            loopCounters.push(counter);
        }
        else {
            loopCounters.pop();
            loopIndexes.pop();
        }
    }

    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public Action create_default() {
        return new End_Repeat(0, DEFAULT_END_REPEAT_NAME, DEFAULT_END_REPEAT_COLOR);
    }

    @Override
    public Action copy() {
        return new End_Repeat(this);
    }

    @Override
    public String valueUnit() {
        return "";
    }

    @Override
    public String getType() {
        return "END_REPEAT";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }

    @Override
    public String toString() {
        return getNom();
    }
}
