package scratch.model;

public abstract class Turn extends Action {

    private static final double DEFAULT_ROTATION  = 90;

    public Turn(String nom) {
        super(DEFAULT_ROTATION, nom);
    }

    abstract void turnCursor(Cursor cursor);

    @Override
    public MyLine execute(Cursor cursor) {
        turnCursor(cursor);
        return new MyLine(cursor.getOrigine(), cursor.getOrigine());
    }
}
