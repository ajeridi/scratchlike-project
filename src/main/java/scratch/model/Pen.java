package scratch.model;

public  abstract class Pen extends Action {

    private static final double DEFAULT_PEN_VALUE = 0;

    public Pen(String nom) {
        super(DEFAULT_PEN_VALUE, nom);
    }

    abstract public void movePen(Cursor cursor);

    @Override
    public MyLine execute(Cursor cursor) {
        movePen(cursor);
        return new MyLine(cursor.getOrigine(), cursor.getOrigine(), cursor.isWriting()); // sur place
    }
}
