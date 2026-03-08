package scratch.model;

import javafx.scene.shape.Line;

public class MyLine extends Line {

    public MyLine(Point origine, Point arrivee) {
        setStart(origine);
        setEnd(arrivee);
    }

    private void setStart(Point origine) {
        this.setStartX(origine.getX());
        this.setStartY(origine.getY());
    }

    private void setEnd(Point arrivee){
        this.setEndX(arrivee.getX());
        this.setEndY(arrivee.getY());
    }

    public Point getStart(){
        return new Point(getStartX(), getStartY());
    }

    public Point getEnd(){
        return new Point(getEndX(), getEndY());
    }

    @Override
    public String toString() {
        return "Line starts at " + getStart() + " and ends at " + getEnd();
    }
}
