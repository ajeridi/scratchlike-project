package scratch.model;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ActionFactory {

    public static Action mapStringToAction(String line){
        String[] split = line.split(";");
        System.out.println(split[0] + " " + split[1]);
        //separtion type et valeur
        String type = split[0];
        double value = Double.parseDouble(split[1]);

        return switch (type) {
            case "MOVE_FORWARD" -> new Move_Forward(value);
            case "PEN_DOWN" -> new Pen_Down();
            case "PEN_UP" -> new Pen_Up();
            case "TURN_LEFT" -> new Turn_Left(value);
            case "TURN_RIGHT" -> new Turn_Right(value);
            default -> null;
        };
    }

    public static List<String> mapActionsToString(ActionList actions){
        List<String> newContent = new ArrayList<>();
        for (Action action : actions.getActions()){
            newContent.add(action.getType() + ";" + action.getValue());
        }
        return newContent;
    }
}
