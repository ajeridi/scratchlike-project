package scratch.model;

public class Pen_Up extends Pen {

    public Pen_Up() {
        super("Lever stylo");
    }

    @Override
    public void movePen(Cursor cursor) {
        cursor.setWriting(false);
    }
}
