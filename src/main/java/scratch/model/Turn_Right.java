package scratch.model;

public class Turn_Right extends Turn {

    public Turn_Right(String nom) {
        super(nom);
    }

    @Override
    void turnCursor(Cursor cursor) {
        cursor.setDirection(getValue());
    }
}
