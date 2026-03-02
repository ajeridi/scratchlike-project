package scratch.model;

import javafx.scene.shape.Line;

public class MyLine extends Line {
    private boolean toAdd = true;

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


    public boolean isToAdd() {
        return toAdd;
    }

    public void setToAdd(boolean toAdd) {
        this.toAdd = toAdd;
    }
}
