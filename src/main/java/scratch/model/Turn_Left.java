package scratch.model;

public class Turn_Left extends Turn {

    public Turn_Left() {
        super("Tourner à gauche de");
    }

    public Turn_Left(Turn_Left original){
        super(original);
    }

    public Turn_Left(double value){
        super(value, "Tourner a gauche de");
    }

    public Turn_Left(double value, String nom, String color){
        super(value, nom, color);
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
}
