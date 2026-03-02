package scratch.model;
import java.lang.Math;

public class Move_Forward extends Action{

    private static final double DEFAULT_MOVE_FORWARD_VALUE = 30;

    public Move_Forward() {
        super(DEFAULT_MOVE_FORWARD_VALUE, "Avancer de");
    }

    @Override
    public MyLine execute(Cursor cursor) {
        Point start = cursor.getOrigine();
        Point end = createEnd(cursor);
        cursor.setOrigine(end);
        return new MyLine(start, end, cursor.isWriting());
    }

    private Point createEnd(Cursor cursor) {
        double angle = Math.toRadians(cursor.getDirection());
        Point origin = cursor.getOrigine();
        double x = origin.getX() + getValue() * Math.cos(angle);
        double y = origin.getY() + getValue() * Math.sin(angle);
        return new Point(x, y);
    }
}
