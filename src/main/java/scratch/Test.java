package scratch;

import scratch.model.*;

public class Test {

    // classe pour tester les modèles
    public static void main(String[] args) {
        // creation du curseur
        Cursor cursor = new Cursor(new Point(1, 0));
        System.out.println(cursor);

        // les actions
        Move_Forward moveForward = new Move_Forward();
        Turn_Right turnRight = new Turn_Right();
        Turn_Left turnLeft = new Turn_Left();
        Pen_Up penUp = new Pen_Up();
        Pen_Down penDown = new Pen_Down();

        // avancer
        moveForward.setValue(78);
        System.out.println(moveForward.execute(cursor));

        // turn right
        System.out.println(turnRight.execute(cursor));
        System.out.println(cursor); // 0°

        //turn left
        System.out.println(turnLeft.execute(cursor));
        System.out.println(cursor); // 90°


    }
}
