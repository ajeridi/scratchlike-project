package scratch.model;

import javafx.scene.shape.Line;

import java.util.List;

public class Scene {
    private final Cursor cursor;
    private final LineList lines;
    private final ActionList actions;

    public Scene(Cursor cursor, LineList lines, ActionList actions) {
        this.cursor = cursor;
        this.lines = lines;
        this.actions = actions;
    }

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
