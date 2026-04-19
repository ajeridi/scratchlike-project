package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import scratch.model.actions.Action;

import java.util.Stack;

public class ExecutionState {
    /*
    * Classe qui s'occupe de garder l'etat d'execution
    * */

    private final Scene scene;

    // a refactoriser dans ExecutionState
    private final IntegerProperty currentIndex = new SimpleIntegerProperty();
    private final Stack<Integer> currentLoopIndexes = new Stack<>(); // va contenir les index de chaque debut de boucle
    private final Stack<Action> loopActions = new Stack<>(); // pile de chaque repeat, le dernier de la pile est celui de la derniere boucle courante
    private final Stack<Integer> loopCounters = new Stack<>(); // pile des compteurs de boucles


    public ExecutionState(Scene scene){
        this.scene = scene;
    }

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

    public Cursor getCursor() {
        return scene.getCursor();
    }

    public void createLine(Point end) {
        scene.createLine(end);
    }
}
