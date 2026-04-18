package scratch.model.actions;

import scratch.model.Scene;

import java.util.Stack;

public class End_Repeat extends Action{

    private static final String DEFAULT_END_REPEAT_NAME = "Fin repeter";
    private static final String DEFAULT_END_REPEAT_COLOR = "orange";

    public End_Repeat(double value, String nom, String color) {
        super(value, nom, color);
    }
    public End_Repeat() {
        super(DEFAULT_END_REPEAT_NAME, DEFAULT_END_REPEAT_COLOR);
    }

    public End_Repeat(End_Repeat endRepeat) {
        super(endRepeat.getValue(), endRepeat.getNom(), endRepeat.getColor());
    }

    @Override
    public void execute(Scene scene) {
        /*
        * Idee du end repeat.
        * On compare la valeur du compteur de la boucle courante (a la fin de la pile de compteurs) avec la valeur du repeat (a la fin de la pile de Repeats)
        * Si la valeur du compteur est en dessous de de la valeur du repeat, on retourne a la premiere action de la boucle (a la fin de la pile des index)
        *
        * */
        Stack<Integer> loopCounters = scene.getLoopCounters(); // pile des compteurs
        int counter = loopCounters.peek();
        Stack<Action> repeats = scene.getLoopActions();
        Stack<Integer> loopIndexes = scene.getCurrentLoopIndexes();
        Action currentLoopAction = repeats.peek();
        if (counter < currentLoopAction.getValue()){
            scene.currentIndexProperty().set(loopIndexes.peek()); // on retourne en debut de boucle
            loopCounters.pop(); // on retire le compteur de la pile
            counter++; // on incremente le compteur
            loopCounters.push(counter); // on le rajoute
        }
        else { // si on a fini la boucle
            loopCounters.pop(); // on retire le compteur de la pile
            loopIndexes.pop(); // 0n retire l'index de la pile
            // VOICI L'ERREUR DU BUG, J'AI OUBLIE DE SUPPRIMER LE DERNIER REPEAT DE LA PILE DE REPEAT, ET DONC ON FAIT QUE COMPARER AVEC LE REPEAT LE PLUS PROFOND
            repeats.pop(); // on retire le repeat de la pile de repeats !!!!!!!!!!!!!
        }
    }

    @Override
    public boolean isValidValue(double value) {
        return true;
    }

    @Override
    public Action create_default() {
        return new End_Repeat(0, DEFAULT_END_REPEAT_NAME, DEFAULT_END_REPEAT_COLOR);
    }

    @Override
    public Action copy() {
        return new End_Repeat(this);
    }

    @Override
    public String valueUnit() {
        return "";
    }

    @Override
    public String getType() {
        return "END_REPEAT";
    }

    @Override
    public String getDetailString() {
        return getNom();
    }

    @Override
    public String toString() {
        return getNom();
    }

    @Override
    public String getTarget() {
        return "";
    }
}
