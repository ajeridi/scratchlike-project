package scratch.model;

public class Turn_Left extends Turn {

    public Turn_Left() {
        super("Tourner à gauche de");
    }

    @Override
    void turnCursor(Cursor cursor) {
        cursor.setDirection(-1 * (getValue()));
    }
}
