package scratch.model;

import scratch.model.actions.*;

import java.util.ArrayList;
import java.util.List;

public class ActionFactory {

    public static Action mapStringToAction(String line){
        // prends un String et le converti en Action
        String[] split = line.split(";");
        //System.out.println(split[0] + " " + split[1]);
        //separtion type et valeur
        String type = split[0];
        //double value = Double.parseDouble(split[1]);


        return switch (type) {
            case "MOVE_FORWARD" -> new Move_Forward(split[1]);
            case "PEN_DOWN" -> new Pen_Down();
            case "PEN_UP" -> new Pen_Up();
            case "TURN_LEFT" -> new Turn_Left(split[1]);
            case "TURN_RIGHT" -> new Turn_Right(split[1]);
            case "REPEAT" -> new Repeat(split[1]);
            case "END_REPEAT" -> new End_Repeat();
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
