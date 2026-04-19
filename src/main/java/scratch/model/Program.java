package scratch.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;
import scratch.model.actions.Pen;
import scratch.model.actions.Pen_Down;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Program {

    private final ActionList actionList = new ActionList();
    private final Stack<ActionList> loops = new Stack<>();

    public boolean isIsValidProgram() {
        return isValidProgram.get();
    }

    public BooleanProperty isValidProgramProperty() {
        return isValidProgram;
    }

    private final BooleanProperty isValidProgram = new SimpleBooleanProperty();

    public Program() {
        getActions().addListener( (ListChangeListener<Action>)change -> {
            isValidProgram.set(compileProgram());
        });
    }

    private boolean compileProgram() {
        // methode qui va proceder "compiler" le programme et dire s'il est chargeable ou pas
        // on doit verifier que les declarations de variables sont bien en debut de programme
        // on suppose que la valeur des variables est ok
        if (getActions().isEmpty())
            return false;
        Set<String> variables = new HashSet();
        Stack<Action> openLoops = new Stack<>();
        int i = 0;
        while (i < getActions().size() && getActions().get(i).getType().equals("VAR_DECLARATION")){ // tant qu'on est dans lz zone de de declaration de variable,
            var action = getActions().get(i);
            if (action.getVariable().getName().isEmpty()) // si aucune variable declaree
                return false;
            variables.add(getActions().get(i).variableName());  //on ajoute les noms des variables a un set, pour pouvoir verifier l'existance des variables plus tard
            i++;
        }

        Action current_pen_action = new Pen_Down(); // pour verifier si on a deux Action pen successifs
        while(i < getActions().size()){ // on continue la suite apres avoir depasse la zone de declaration
            Action action = getActions().get(i);
            String actionType = action.getType();
            // si on a une declaration, pas bon
            if (actionType.equals("VAR_DECLARATION"))
                return false;

            // si l'action a une variable non declaree
            if (action.getVariable().hasName() && !variables.contains(action.getVariable().getName()))
                return false;
            // si on a une action avec une variable cible, celle ci doit exister aussi
            if (actionType.equals("VAR_ASSIGNMENT") || actionType.equals("INCREMENT_VARIABLE")){
                if (!variables.contains(action.getTarget()))
                    return false;
            }

            if (actionType.equals(current_pen_action.getType())){
                return false; // deux action pen du meme type se suivent
            }
            // s'ils ne sont pas du meme type et que action est de type Pen
            if (action instanceof Pen)
                current_pen_action = action;

            // pour les boucles, chaque fois qu'on rencontre un repeat on rentre dans une boucle, donc on l'ajoute dans la pile de boucle ouvertes
            if (actionType.equals("REPEAT")){
                openLoops.push(action);
            }
            // si l'action est un end_repeat, on est cense ferme une boucle, donc si aucune boucle est ouverte, retourner false, sinon refermer la boucle
            if (actionType.equals("END_REPEAT")){
                if (openLoops.isEmpty()) // si aucune boucle a ferme, retourner faux
                    return false;
                else // sinon fermer la boucle en enlevant de la file une boucle ouverte
                    openLoops.pop();
            }

            // si l'action est un increment ou assign, si il est dans une boucle et que le target est le meme que la variable de la boucle, return false
            if ((actionType.equals("INCREMENT_VARIABLE") || actionType.equals("VAR_ASSIGNMENT")) && !openLoops.isEmpty()){
                // si j'ai un target action dans une boucle imbriquee, meme si je suis au dans la boucle la plus profonde, je ne peux pas modifier la variable de la boucle englobante
                // mon target action aura une certaine profondeur, donc je dois verifier pour les repeat qui ont une profondeur <= celle de mon target action
                for (int n = 0; n < action.getLoopDepth(); n++){
                    if (openLoops.get(n).getVariable().getName().equals(action.getTarget())){
                        System.out.println("Inc loop depth " + action.getLoopDepth());
                        return false;
                    }
                }
            }
            i++;
        }
        // si il nous reste des boucles ouvertes, retourner faux
        return openLoops.isEmpty();

    }

    public ObservableList<Action> getActions() {
        return actionList.getActions();
    }

    public IntegerBinding getSizeProperty() {
        return actionList.getSizeProperty();
    }

    public BooleanBinding actionsIsEmptyProperty() {
        return actionList.isEmptyProperty();
    }

    public boolean addToActionList(Action action, String from, int indexToAdd){
        Action actionToAdd = createActionToAdd(action, from);
        // ON va separer l'ajout au milieu et l'ajout en fin
        // si on a selectionne un index, on ne peut pas juste l'ajouter apres
        // si on ajoute une action apres un repeat, il faut ajouter la bonne indentation
        // techniquement ca va marcher comme un down
        // donc on prends le depth de l'action qui precede, si c'est un repeat on ajoute + 1 au depth, sinon on garde le meme depth
        //System.out.println("Index to add " + indexToAdd);
        if (indexToAdd > 0 && indexToAdd != getActions().size()){ // si index est selectionne
            addToMiddleOfProgram(indexToAdd, actionToAdd);
            return true;
        }
        return addToEndOfProgram(actionToAdd);
    }

    private Action createActionToAdd(Action action, String from) {
        Action actionToAdd;
        // creer une action avec une valeur par defaut, si on ajoute depuis le catalogue
        if (from.equals("from-catalogue"))
            actionToAdd = action.create_default();
        else //sinon on ajoute l'action (par ex depuis file)
            actionToAdd = action;
        return actionToAdd;
    }

    private boolean addToEndOfProgram(Action actionToAdd) {
        // si l'action est un end_repeat, on retire le dernier element de la boucle courante
        if (actionToAdd.getType().equals("END_REPEAT") && !loops.isEmpty()){
            loops.pop();
        }

        if (!loops.isEmpty()){ // si on a une boucle courante
            // on ajoute l'action dans la boucle courante (donc la liste d'action contenue dans le Repeat courant)
            var currentLoop = loops.peek();
            currentLoop.add(actionToAdd);
        }

        // on defini la profondeur de l'action, qui va nous servir pour l'indentation
        actionToAdd.setLoopDepth(loops.size());

        // si l'action est un repeat, on defini la boucle courante comme etant la liste qu'elle contient
        if (actionToAdd.getType().equals("REPEAT")){
            loops.push(actionToAdd.getActions());
        }


        return actionList.add(actionToAdd);
    }

    private void addToMiddleOfProgram(int indexToAdd, Action actionToAdd) {
        var upperAction = getActions().get(indexToAdd -1);
        var upperActionDepth = upperAction.getLoopDepth();
        String upperActionType = upperAction.getType();

        // cas du end_repeat
        if (actionToAdd.getType().equals("END_REPEAT")){
            // si j'ajoute apres un repeat je garde le meme depth, vu que je ferme la boucle
            if (upperActionType.equals("REPEAT")){
                actionToAdd.setLoopDepth(upperActionDepth);
            }
            // sinon si la profondeur de l'action du dessus est > 0 (donc dans une boucle), on fait depth -1 (on ferme la boucle)
            else if (upperActionDepth > 0) {
                actionToAdd.setLoopDepth(upperActionDepth-1);
            }
        }
        // si l'action n'est pas un repeat
        else {
            // si on ajoute apres une action, on doit garder la meme profondeur sauf si
            if (upperActionType.equals("REPEAT")){
                 // si ajout apres repeat, on rentre dans une boucle donc on augmente la profondeur
                actionToAdd.setLoopDepth(upperActionDepth+1);
            }
            else {
                actionToAdd.setLoopDepth(upperActionDepth);
            }
        }
        getActions().add(indexToAdd, actionToAdd);
    }

    public void swapActions(int index1, int index2){
        // echange la position de deux actions dans la liste
        actionList.swap(index1, index2);
    }

    public void up(int selectedIndex) {
        // monter la liste d'actions
        // Avec l'introduction de Repeat, il faut qu'on puisse garder les bonnes indentations
        // L'action montee, doit avoir l'indentation de l'action qu'elle remplace
        // A l'exception de End_Repeat. Si je monte une action, et qu'elle prends la place de End_Repeat, elle rentre dans la boucle
        // et donc elle aura comme indentation, celle de END_REPEAT + 1
        // A noter : si on sort on entre dans une boucle, faudrait update les liste, je pense que ca va me servir pour les checks de compilations
        var list = actionList.getActions();
        Action upperAction = list.get(selectedIndex-1);
        Action currentAction = list.get(selectedIndex);
        int upperActionDepth = upperAction.getLoopDepth();
        if (upperAction.getType().equals("END_REPEAT")){
            // si l'action du dessus est un END_REPEAT, on rentre dans une boucle et donc on aura comme profondeur celle de END_REPEAT + 1
            currentAction.setLoopDepth(upperActionDepth + 1);
        }
        else {
            currentAction.setLoopDepth(upperActionDepth);
        }
        actionList.swap(selectedIndex, selectedIndex - 1);
    }

    public void down(int selectedIndex){
        //  descendre la liste d'actions
         //meme principe que UP, mais au lieu de END_REPEAT c'est REPEAT
        var list = actionList.getActions();
        Action lowerAction = list.get(selectedIndex+1);
        Action currentAction = list.get(selectedIndex);
        int lowerActionDepth = lowerAction.getLoopDepth();

        // cas end_repeat
        if (currentAction.getType().equals("END_REPEAT")){
            // si end_repeat descends en dessous d'un repeat, il prends sa profondeur (il referme le repeat)
            if (lowerAction.getType().equals("REPEAT") || lowerAction.getType().equals(currentAction.getType())){
                currentAction.setLoopDepth(lowerActionDepth);
            }
        }
        // cas du repeat
        else if (currentAction.getType().equals("REPEAT")){
            if (lowerAction.getType().equals("END_REPEAT")) // si je descends en dessous d'un end_repeat je prends sa profondeur
                currentAction.setLoopDepth(lowerActionDepth);
            else if (lowerAction.getType().equals("REPEAT"))
                currentAction.setLoopDepth(lowerActionDepth+1); // si je descends en dessous d'un repeat je rentre dans sa boucle
        }
        // les autres cas
        else {
            if (lowerAction.getType().equals("REPEAT"))
                // si l'action du dessous est un REPEAT, on rentre dans une boucle et donc on aura comme profondeur celle de REPEAT + 1
                currentAction.setLoopDepth(lowerActionDepth + 1);
            else
                currentAction.setLoopDepth(lowerActionDepth);
        }

        actionList.swap(selectedIndex, selectedIndex+1);
    }

    public void duplicate(Action action){
        //dupliquer une action
        Action copy = action.copy();
        actionList.add(copy);
    }

    public void delete(Action action){
        // supprimer une action
        actionList.delete(action);
    }

    public void deleteAll() {
        // supprimer toutes les actions
        actionList.deleteAll();
        loops.clear();
    }

    public void newFile() {
        // nettoie la liste
    }

    public void open(File file) throws IOException {
        List<String> fileContent = new ArrayList<>();
        if (file.exists())
            fileContent = Files.readAllLines(file.toPath());

        convertFileContentToActionList(fileContent);
    }

    private void convertFileContentToActionList(List<String> fileContent){
        actionList.deleteAll();
        for (String line : fileContent){
            Action action = ActionFactory.mapStringToAction(line);
            //actionList.add(action);
            addToActionList(action, "from-file", -1);
        }
    }

    public void saveFile(File file) throws IOException {
        List<String> newFileContent = ActionFactory.mapActionsToString(actionList);
        for (String action : newFileContent){
            Files.write(file.toPath(), newFileContent);
        }
    }

    public BooleanBinding isValid() {
        // Boolean binding sur la compilation du programme, executee a chaque changement sur la liste d'actions
        return Bindings.createBooleanBinding(
                () -> {
                    var ok = compileProgram();
                    System.out.println("Program compiled = " + ok);
                    return ok;
                },
                getActions()
        );
    }
}
