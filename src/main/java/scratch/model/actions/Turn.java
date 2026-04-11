package scratch.model.actions;

import scratch.model.Cursor;
import scratch.model.Scene;

public abstract class Turn extends Action {

    private static final double DEFAULT_ROTATION  = 90;
    private static final String DEFAULT_TURN_COLOR  = "red";

    public Turn(String nom) {
        super(nom, DEFAULT_TURN_COLOR);
    }

    public Turn(Turn original){
        super(original.getValue(), original.getNom(), DEFAULT_TURN_COLOR);
    }

    public Turn(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Turn(double value, String nom){
        super(value, nom, DEFAULT_TURN_COLOR);
    }

    abstract void turnCursor(Cursor cursor);

    @Override
    public void execute(Scene scene) {
        Cursor cursor = scene.getCursor();
        turnCursor(cursor);
    }

    @Override
    public String valueUnit() {
        return "degres";
    }

    public static double getDefaultRotation() {
        return DEFAULT_ROTATION;
    }

    @Override
    public boolean isValidValue(double value) {
        return value >= 1 && value <= 180;
    }
}
