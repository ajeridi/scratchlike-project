package scratch.model;

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
}
