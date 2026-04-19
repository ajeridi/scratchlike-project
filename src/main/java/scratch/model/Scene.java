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


/*
    // a refactoriser dans ExecutionState
    private final IntegerProperty currentIndex = new SimpleIntegerProperty();
    private final Stack<Integer> currentLoopIndexes = new Stack<>(); // va contenir les index de chaque debut de boucle
    private final Stack<Action> loopActions = new Stack<>(); // pile de chaque repeat, le dernier de la pile est celui de la derniere boucle courante
    private final Stack<Integer> loopCounters = new Stack<>(); // pile des compteurs de boucles


    public int getCurrentIndex() {
        return currentIndex.get();
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public Stack<Action> getLoopActions() {
        return loopActions;
    }


    public Stack<Integer> getLoopCounters() {
        return loopCounters;
    }


    public Stack<Integer> getCurrentLoopIndexes() {
        return currentLoopIndexes;
    }

*/
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
