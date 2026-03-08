package scratch.model;

public class Turn_Right extends Turn {

    public Turn_Right() {
        super("Tourner à droite de");
    }

    public Turn_Right(Turn_Right original){
        super (original);
    }

    public Turn_Right(double value){
        super(value, "Tourner à droite de");
    }

    public Turn_Right(double value, String nom, String color){
        super(value, nom, color);
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
}
