package scratch.model;

public class Pen_Down extends Pen {

    public Pen_Down() {
        super("Abaisser stylo");
    }

    public Pen_Down(Pen_Down original){
        super(original.getNom());
    }

    @Override
    public void movePen(Cursor cursor) {
        cursor.setWriting(true);
    }

    @Override
    public Action copy() {
        return new Pen_Down(this);
    }
}
