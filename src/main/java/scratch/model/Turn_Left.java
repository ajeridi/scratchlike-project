package scratch.model;

public class Turn_Left extends Turn {

    public Turn_Left() {
        super("Tourner à gauche de");
    }

    public Turn_Left(Turn_Left original){
        super(original);
    }

    @Override
    void turnCursor(Cursor cursor) {
        double currentDirection = cursor.getDirection();
        cursor.setDirection(currentDirection + getValue());
    }

    @Override
    public Action copy() {
        return new Turn_Left(this);
    }
}
