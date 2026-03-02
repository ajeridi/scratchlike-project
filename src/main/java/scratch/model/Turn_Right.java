package scratch.model;

public class Turn_Right extends Turn {

    public Turn_Right() {
        super("Tourner à droite de");
    }

    @Override
    void turnCursor(Cursor cursor) {
        double currentDirection = cursor.getDirection();
        cursor.setDirection(currentDirection - getValue());
    }
}
