package scratch.model;

public class Turn_Left extends Turn {

    public Turn_Left(String nom) {
        super(nom);
    }

    @Override
    void turnCursor(Cursor cursor) {
        cursor.setDirection(-1 * (getValue()));
    }
}
