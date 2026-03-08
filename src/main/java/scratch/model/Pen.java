package scratch.model;

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
}
