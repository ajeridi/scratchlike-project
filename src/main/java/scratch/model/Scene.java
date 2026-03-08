package scratch.model;

import javafx.scene.shape.Line;

import java.util.List;

public class Scene {
    private final Cursor cursor = new Cursor(new Point(0,0));
    private final LineList lines = new LineList();
    private final ActionList actions = new ActionList();


    public Cursor getCursor() {
        return cursor;
    }

    public List<MyLine> getLines() {
        return lines.getLines();
    }

    public ActionList getActions() {
        return actions;
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
