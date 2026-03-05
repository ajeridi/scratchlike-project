package scratch.model;
import java.lang.Math;

public class Move_Forward extends Action{

    private static final double DEFAULT_MOVE_FORWARD_VALUE = 30;
    private static final String DEFAULT_MOVE_FORWARD_COLOR = "blue";

    public Move_Forward() {
        super("Avancer de", DEFAULT_MOVE_FORWARD_COLOR);
    }

    public Move_Forward(Move_Forward original){
        super(DEFAULT_MOVE_FORWARD_VALUE, original.getNom(), original.getColor());
    }

    @Override
    public MyLine execute(Cursor cursor) {
        Point start = cursor.getOrigine();
        Point end = createEnd(cursor);
        cursor.setOrigine(end);
        return new MyLine(start, end, cursor.isWriting());
    }

    @Override
    public Action copy() {
        return new Move_Forward(this);
    }

    private Point createEnd(Cursor cursor) {
        double angle = Math.toRadians(cursor.getDirection());
        Point origin = cursor.getOrigine();
        double dx = getValue() * Math.cos(angle);
        double dy = getValue() * Math.sin(angle);
        return origin.translate(dx, dy);
    }
}
