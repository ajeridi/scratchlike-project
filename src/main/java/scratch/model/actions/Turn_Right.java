package scratch.model.actions;

import scratch.model.Cursor;

public class Turn_Right extends Turn {

    private static final String DEFAULT_TURN_RIGHT_NAME = "Tourner à droite de";

    public Turn_Right() {
        super(DEFAULT_TURN_RIGHT_NAME);
    }

    public Turn_Right(Turn_Right original){
        super (original);
    }

    public Turn_Right(double value){
        super(value, DEFAULT_TURN_RIGHT_NAME);
    }

    public Turn_Right(double value, String nom, String color){
        super(value, nom, color);
    }

    public Turn_Right(String value){
        super(value, DEFAULT_TURN_RIGHT_NAME);
    }

    @Override
    public Turn_Right create_default() {
        return new Turn_Right(getDefaultRotation(), this.getNom(), this.getColor());
    }

    @Override
    void turnCursor(Cursor cursor) {
        double currentDirection = cursor.getDirection();
        cursor.setDirection(currentDirection - getValue());
    }

    @Override
    public Action copy() {
        return new Turn_Right(this);
    }

    @Override
    public String getType() {
        return "TURN_RIGHT";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }
}
