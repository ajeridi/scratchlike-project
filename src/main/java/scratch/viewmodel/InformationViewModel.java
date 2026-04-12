package scratch.viewmodel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.VBox;
import scratch.model.Cursor;

public class InformationViewModel extends VBox {

    private final Cursor cursor;

    private final DoubleProperty cursorX = new SimpleDoubleProperty();
    private final DoubleProperty cursorY = new SimpleDoubleProperty();
    private final DoubleProperty cursorDirection = new SimpleDoubleProperty();

    public InformationViewModel(Cursor cursor){
        this.cursor = cursor;

        setBindings();
    }

    private void setBindings() {
        cursorX.bind(cursor.getXProperty());
        cursorY.bind(cursor.getYProperty());
        cursorDirection.bind(cursor.getDirectionProperty());
    }

    public double getCursorX() {
        return cursorX.get();
    }

    public DoubleProperty cursorXProperty() {
        return cursorX;
    }

    public double getCursorY() {
        return cursorY.get();
    }

    public DoubleProperty cursorYProperty() {
        return cursorY;
    }

    public double getCursorDirection() {
        return cursorDirection.get();
    }

    public DoubleProperty cursorDirectionProperty() {
        return cursorDirection;
    }
}
