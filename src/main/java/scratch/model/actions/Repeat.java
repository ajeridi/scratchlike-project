package scratch.model.actions;

import scratch.model.ActionList;
import scratch.model.Scene;

import java.util.Stack;

public class Repeat extends Action {
    private static final String DEFAULT_REPEAT_NAME = "Repeter";
    private static final String DEFAULT_REPEAT_COLOR = "orange";


    private final ActionList actions = new ActionList();

    public Repeat(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Repeat(String nom, String color) {
        super(nom, color);
    }

    public Repeat() {
        super(DEFAULT_REPEAT_NAME, DEFAULT_REPEAT_COLOR);
    }

    public Repeat(String value){
        super(value, DEFAULT_REPEAT_NAME, DEFAULT_REPEAT_COLOR);
    }

    @Override
    public void execute(Scene scene) {
        /*
         * Quand on entre dans une boucle repeat, on a besoin d'un compteur et d'une reference vers l'action repeat (pour savoir y retourner si notre compteur n'est pas egal a la valeur de repeat
         * donc on a besoin d'un stack de compteurs (on push quand on rentre dans une boucle et on pop une fois qu'on en sort)
         * */
        Stack<Integer> currentLoopIndexes = scene.getCurrentLoopIndexes(); // pile des index des repeat
        int repeat_index = scene.currentIndexProperty().get(); // index du repeat
        currentLoopIndexes.add(repeat_index); // on ajoute l'index du repeat a la pile des index des repeats, pour pouvoir y retourner
        int counter = 1; // initalisation du compteur a 1
        var loopCounters = scene.getLoopCounters(); // pile des compteurs
        loopCounters.push(counter); // on ajoute le compteur en fin de pile
        scene.getLoopActions().push(this); // on ajoute l'action repeat a la pile pour pouvoir retrouver sa valeur dans le end_repeat
    }

    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public Action create_default() {
        return new Repeat(4, DEFAULT_REPEAT_NAME, DEFAULT_REPEAT_COLOR);
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public String valueUnit() {
        return "fois";
    }

    @Override
    public String getType() {
        return "REPEAT";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }

    @Override
    public String toString() {
        return getNom() + " " + getVariable().getStringValue() + " fois";
    }

    @Override
    public ActionList getActions() {
        return actions;
    }

    @Override
    public String getTarget() {
        return "";
    }
}
