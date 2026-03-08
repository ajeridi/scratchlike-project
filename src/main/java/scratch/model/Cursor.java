package scratch.model;

public class Cursor {

    private final double DEFAULT_CURSOR_HEAD_ANGLE = 0;

    private Point origine;
    private double direction = DEFAULT_CURSOR_HEAD_ANGLE;
    private boolean writing = true;

    public Cursor(Point origine){ // constructeur par défaut
        this.origine = origine;
    }

    // peut etre inutile, a voir
    public Cursor(Point origine, double direction, boolean writing) {
        this(origine);
        this.direction = direction;
        this.writing = writing;
    }

    public Point getOrigine() {
        return origine;
    }

    public void setOrigine(Point origine) {
        this.origine = origine;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public boolean isWriting() {
        return writing;
    }

    public void setWriting(boolean writing) {
        this.writing = writing;
    }

    public double getNorm() {
        double x = origine.getX();
        double y = origine.getY();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public String toString() {
        return "Cursor is at position " + this.origine + " with a direction of " + this.direction + " degrees and a norm of " + getNorm();
    }
}
