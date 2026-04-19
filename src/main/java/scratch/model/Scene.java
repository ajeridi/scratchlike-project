package scratch.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import scratch.model.actions.Action;

import java.util.List;
import java.util.Stack;

public class Scene {
    private final Cursor cursor = new Cursor(new Point(0,0));
    private final LineList lines = new LineList();

    public Cursor getCursor() {
        return cursor;
    }

    public List<MyLine> getLines() {
        return lines.getLines();
    }

    public void createLine(Point end) {
        // methode qui s'occupe de creer une ligne et de l'ajouter a la liste de ligne, si le cursor et en mode ecriture
        if (cursor.isWriting()){
            MyLine newLine = new MyLine(cursor.getOrigine(), end);
            lines.add(newLine);
        }
    }

    public void reset() {
        lines.clear();
        cursor.reset();
    }
}
