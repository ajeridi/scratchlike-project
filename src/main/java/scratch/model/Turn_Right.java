package scratch.model;

public class Turn_Right extends Turn {

    public Turn_Right() {
        super("Tourner à droite de");
    }

    @Override
    void turnCursor(Cursor cursor) {
        cursor.setDirection(getValue());
    }
}
