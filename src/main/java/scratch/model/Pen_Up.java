package scratch.model;

public class Pen_Up extends Pen {

    public Pen_Up() {
        super("Lever stylo");
    }

    public Pen_Up(Pen_Up original){
        super(original.getNom());
    }

    @Override
    public void movePen(Cursor cursor) {
        cursor.setWriting(false);
    }

    @Override
    public Action copy() {
        return new Pen_Up(this);
    }
}
