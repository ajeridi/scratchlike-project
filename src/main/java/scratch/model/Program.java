package scratch.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.ObservableList;
import scratch.model.actions.Action;
import scratch.model.actions.Repeat;

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

    public boolean addToActionList(Action action, String from){
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
        return actionList.add(actionToAdd);
    }

    public void swapActions(int index1, int index2){
        // echange la position de deux actions dans la liste
        actionList.swap(index1, index2);
    }

    public void up(int selectedIndex) {
        // monter la liste d'actions
        actionList.swap(selectedIndex, selectedIndex - 1);
    }

    public void down(int selectedIndex){
        //  descendre la liste d'actions
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
            addToActionList(action, "from-file");
        }
    }

    public void saveFile(File file) throws IOException {
        List<String> newFileContent = ActionFactory.mapActionsToString(actionList);
        for (String action : newFileContent){
            Files.write(file.toPath(), newFileContent);
        }
    }
}
