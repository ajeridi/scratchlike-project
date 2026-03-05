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

    public LineList getLines() {
        return lines;
    }

    public ActionList getActions() {
        return actions;
    }

    // a ajouter : les fonctions d'ajouts au lines et actions
    // les composants visuels de Scene comme Canvas et les methodes repaint etc sont la responsabilite de la vue
}
