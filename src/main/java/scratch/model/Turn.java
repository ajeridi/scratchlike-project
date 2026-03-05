package scratch.model;

public abstract class Turn extends Action {

    private static final double DEFAULT_ROTATION  = 90;
    private static final String DEFAULT_TURN_COLOR  = "red";

    public Turn(String nom) {
        super(nom, DEFAULT_TURN_COLOR);
    }

    public Turn(Turn original){
        super(DEFAULT_ROTATION, original.getNom(), DEFAULT_TURN_COLOR);
    }

    abstract void turnCursor(Cursor cursor);

    @Override
    public MyLine execute(Cursor cursor) {
        turnCursor(cursor);
        return new MyLine(cursor.getOrigine(), cursor.getOrigine(), cursor.isWriting());
    }
}
