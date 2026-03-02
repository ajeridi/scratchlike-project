package scratch.model;

import javafx.scene.shape.Line;

public class MyLine extends Line {
    private boolean toAdd = true;

    public MyLine(Point origine, Point arrivee, boolean toAdd) {
        setStart(origine);
        setEnd(arrivee);
        this.toAdd = toAdd;
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

    public boolean isToAdd() {
        return toAdd;
    }

    public void setToAdd(boolean toAdd) {
        this.toAdd = toAdd;
    }

    @Override
    public String toString() {
        return "Line starts at " + getStart() + " and ends at " + getEnd();
    }
}
