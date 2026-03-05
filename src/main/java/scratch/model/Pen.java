package scratch.model;

public  abstract class Pen extends Action {

    public Pen(String nom) {
        super(nom, "green");
    }

    abstract public void movePen(Cursor cursor);

    @Override
    public MyLine execute(Cursor cursor) {
        movePen(cursor);
        return new MyLine(cursor.getOrigine(), cursor.getOrigine(), cursor.isWriting()); // sur place
    }

    @Override
    public String valueUnit() {
        return "";
    }
}
