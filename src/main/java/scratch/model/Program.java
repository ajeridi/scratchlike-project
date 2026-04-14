package scratch.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Program {

    private final ActionList actionList = new ActionList();
    private final Stack<ActionList> loops = new Stack<>();

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
        Action actionToAdd;


        // creer une action avec une valeur par defaut, si on ajoute depuis le catalogue
        if (from.equals("from-catalogue"))
            actionToAdd = action.create_default();
        else //sinon on ajoute l'action (par ex depuis file)
            actionToAdd = action;


        // si l'action est un end_repeat, on retire le dernier element de la boucle courante
        if (actionToAdd.getType().equals("END_REPEAT")){
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

        // si on a selectionne un index, on ne peut pas juste l'ajouter apres
        // si on ajoute une action apres un repeat, il faut ajouter la bonne indentation
        // techniquement ca va marcher comme un down
        // donc on prends le depth de l'action qui precede, si c'est un repeat on ajoute + 1 au depth, sinon on garde le meme depth
        // la prise en compte de l'ajout au bon index dans la liste contenue dans repeat se fera plus tard, ici on se preoccupe uniquement de l'ajout dans la liste programme
        System.out.println("Index to add " + indexToAdd);
        if (indexToAdd > 0 && indexToAdd != getActions().size()){ // si index est selectionne
            var upperAction = getActions().get(indexToAdd-1);
            var upperActionDepth = upperAction.getLoopDepth();
            if (upperAction.getType().equals("REPEAT"))
                actionToAdd.setLoopDepth(upperActionDepth+1); // on fait plus 1 au depth
            else
                actionToAdd.setLoopDepth(upperActionDepth);
            // si l'action a ajoute est un end repeat, on fait -1 au depth
            if (actionToAdd.getType().equals("END_REPEAT"))
                actionToAdd.setLoopDepth(upperActionDepth-1);
            getActions().add(indexToAdd, actionToAdd);
            return true;
        }

        return actionList.add(actionToAdd);
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
        if (lowerAction.getType().equals("REPEAT")){
            // si l'action du dessous est un REPEAT, on rentre dans une boucle et donc on aura comme profondeur celle de REPEAT + 1
            currentAction.setLoopDepth(lowerActionDepth + 1);
        }
        else
            currentAction.setLoopDepth(lowerActionDepth);

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
}
