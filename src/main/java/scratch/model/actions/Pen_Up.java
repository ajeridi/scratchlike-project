package scratch.model.actions;

import scratch.model.Cursor;

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
    public Pen_Up create_default() {
        return new Pen_Up();
    }

    @Override
    public Action copy() {
        return new Pen_Up(this);
    }

    @Override
    public String getType() {
        return "PEN_UP";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }
}
