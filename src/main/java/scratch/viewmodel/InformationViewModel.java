package scratch.viewmodel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import scratch.model.Cursor;
import scratch.model.Scene;

import java.util.AbstractMap;
import java.util.Map;

public class InformationViewModel extends VBox {

    private final Cursor cursor;
    private final Scene scene;

    private final DoubleProperty cursorX = new SimpleDoubleProperty();
    private final DoubleProperty cursorY = new SimpleDoubleProperty();
    private final DoubleProperty cursorDirection = new SimpleDoubleProperty();


    // Map.Entry est une liste de paire cle,valeur dans une liste, comme un table
    private  ObservableList<Map.Entry<String, StringProperty>> observableListOfMap; // https://codingtechroom.com/question/-populate-tableview-hashmap-javafx

    public InformationViewModel(Scene scene){
        this.scene = scene;
        this.cursor = scene.getCursor();

        setObservableListOfMap(scene);

        setBindings();
    }

    private void setObservableListOfMap(Scene scene) {
        // creation d'une observable list qui va contenir les elements de la map de variables
        this.observableListOfMap = FXCollections.observableArrayList(scene.getVariables().entrySet());

        scene.getVariables().addListener(
                (MapChangeListener<String, StringProperty>) change -> {
                    if (change.wasAdded()){
                        // si on ajoute un element a la map
                        if (!observableListOfMap.contains(change.getKey())){
                            observableListOfMap.add(new AbstractMap.SimpleEntry<>(change.getKey(), change.getValueAdded()));
                        }
                    }
                    else if (change.wasRemoved()){
                        observableListOfMap.removeIf(entry -> entry.getKey().equals(change.getKey())); // parcours le Map.Entry, et lorsque le row contient la meme cle que l'element supprime de la map, il sera supprime de la liste
                    }
                }
        );
    }

    private void setBindings() {
        cursorX.bind(cursor.getXProperty());
        cursorY.bind(cursor.getYProperty());
        cursorDirection.bind(cursor.getDirectionProperty());
    }

    public double getCursorX() {
        return cursorX.get();
    }

    public DoubleProperty cursorXProperty() {
        return cursorX;
    }

    public double getCursorY() {
        return cursorY.get();
    }

    public DoubleProperty cursorYProperty() {
        return cursorY;
    }

    public double getCursorDirection() {
        return cursorDirection.get();
    }

    public DoubleProperty cursorDirectionProperty() {
        return cursorDirection;
    }


    public ObservableList<Map.Entry<String, StringProperty>> getObservableListOfMap() {
        return observableListOfMap;
    }
}
