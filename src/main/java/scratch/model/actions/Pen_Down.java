package scratch.model.actions;

import scratch.model.Cursor;

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
    public Pen_Down create_default() {
        return new Pen_Down();
    }

    @Override
    public Action copy() {
        return new Pen_Down(this);
    }

    @Override
    public String getType() {
        return "PEN_DOWN";
    }


}
