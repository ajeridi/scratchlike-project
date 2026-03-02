package scratch.model;

public class Pen_Down extends Pen {

    public Pen_Down() {
        super("Abaisser stylo");
    }

    @Override
    public void movePen(Cursor cursor) {
        cursor.setWriting(true);
    }
}
