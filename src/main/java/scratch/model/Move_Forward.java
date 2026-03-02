package scratch.model;
import java.lang.Math;

public class Move_Forward extends Action{

    public Move_Forward(double value, String nom) {
        super(value, nom);
    }

    @Override
    public MyLine execute(Cursor cursor) {
        Point start = cursor.getOrigine();
        Point end = createEnd(cursor);
        return new MyLine(start, end);
    }

    private Point createEnd(Cursor cursor) {
        double angle = Math.toRadians(cursor.getDirection());
        double cursorNorm = cursor.getNorm();
        double x = cursorNorm * Math.cos(angle) + getValue() * Math.cos(angle);
        double y = cursorNorm * Math.sin(angle) + getValue() * Math.sin(angle);
        return new Point(x, y);
    }
}
