package scratch.model;

public class Turn_Left extends Turn {

    public Turn_Left() {
        super("Tourner à gauche de");
    }

    @Override
    void turnCursor(Cursor cursor) {
        double currentDirection = cursor.getDirection();
        cursor.setDirection(currentDirection + getValue());
    }
}
