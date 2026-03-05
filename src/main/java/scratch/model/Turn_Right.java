package scratch.model;

public class Turn_Right extends Turn {

    public Turn_Right() {
        super("Tourner à droite de");
    }

    public Turn_Right(Turn_Right original){
        super (original);
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
}
