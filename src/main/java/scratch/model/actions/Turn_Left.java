package scratch.model.actions;

import scratch.model.Cursor;

public class Turn_Left extends Turn {

    private final static String DEFAULT_TURN_LEFT_NAME = "Tourner à gauche de";

    public Turn_Left() {
        super(DEFAULT_TURN_LEFT_NAME);
    }

    public Turn_Left(Turn_Left original){
        super(original);
    }

    public Turn_Left(double value){
        super(value, DEFAULT_TURN_LEFT_NAME);
    }

    public Turn_Left(double value, String nom, String color){
        super(value, nom, color);
    }

    public Turn_Left(String value){
        super(value, DEFAULT_TURN_LEFT_NAME);
    }

    @Override
    void turnCursor(Cursor cursor) {
        double currentDirection = cursor.getDirection();
        cursor.setDirection(currentDirection + getValue());
    }

    @Override
    public Turn_Left create_default() {
        return new Turn_Left(getDefaultRotation(), this.getNom(), this.getColor());
    }

    @Override
    public Action copy() {
        return new Turn_Left(this);
    }

    @Override
    public String getType() {
        return "TURN_LEFT";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }
}
