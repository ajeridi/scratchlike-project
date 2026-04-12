package scratch.model.actions;

import scratch.model.Cursor;
import scratch.model.Scene;

public  abstract class Pen extends Action {

    public Pen(String nom) {
        super(nom, "green");
    }

    abstract public void movePen(Cursor cursor);

    @Override
    public void execute(Scene scene) {
        Cursor cursor = scene.getCursor();
        movePen(cursor);
    }

    @Override
    public String valueUnit() {
        return "";
    }


    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public String toString() {
        return this.getNom();
    }
}
