package scratch.model;
import java.lang.Math;

public class Move_Forward extends Action{

    private static final double DEFAULT_MOVE_FORWARD_VALUE = 30;
    private static final String DEFAULT_MOVE_FORWARD_COLOR = "blue";

    public Move_Forward() {
        super("Avancer de", DEFAULT_MOVE_FORWARD_COLOR);
    }

    public Move_Forward(Move_Forward original){
        super(original.getValue(), original.getNom(), original.getColor());
    }

    public Move_Forward(double value, String nom, String color){
        super(value, nom, color);
    }

    @Override
    public void execute(Scene scene) {
        Cursor cursor = scene.getCursor();

        Point end = createEnd(cursor);

        scene.createLine(end);
        cursor.setOrigine(end);
    }

    @Override
    public boolean isValidValue(double value) {
        return value > 0 && value <= 100;
    }

    @Override
    public Move_Forward create_default() {
        return new Move_Forward(DEFAULT_MOVE_FORWARD_VALUE, this.getNom(), this.getColor());
    }

    @Override
    public Action copy() {
        return new Move_Forward(this);
    }

    @Override
    public String valueUnit() {
        return "pixels";
    }

    private Point createEnd(Cursor cursor) {
        double angle = Math.toRadians(cursor.getDirection()) +  Math.PI / 2;;
        Point origin = cursor.getOrigine();
        double dx = getValue() * Math.cos(angle);
        double dy = getValue() * Math.sin(angle);
        return origin.translate(dx, dy);
    }
}
