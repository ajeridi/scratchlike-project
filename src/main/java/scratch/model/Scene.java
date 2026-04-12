package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.shape.Line;

import java.util.List;

public class Scene {
    private final Cursor cursor = new Cursor(new Point(0,0));
    private final LineList lines = new LineList();


    // map qui va contenir toutes les variabls
    private final ObservableMap<String, StringProperty> variables = FXCollections.observableHashMap();

    public ObservableMap<String, StringProperty> getVariables() {
        return variables;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public List<MyLine> getLines() {
        return lines.getLines();
    }




    public void createLine(Point end) {
        if (cursor.isWriting()){
            MyLine newLine = new MyLine(cursor.getOrigine(), end);
            lines.add(newLine);
        }
    }

    public void reset() {
        lines.clear();
        cursor.reset();
    }

    // a ajouter : les fonctions d'ajouts au lines et actions
    // les composants visuels de Scene comme Canvas et les methodes repaint etc sont la responsabilite de la vue
}
