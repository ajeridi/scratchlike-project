package scratch.model.actions;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public abstract class Target_Action extends Action {

    private final SimpleStringProperty target_name = new SimpleStringProperty("");


    public Target_Action(double value, String nom, String color) {
        super(value, nom, color);
    }

    public Target_Action(String name, String color) {
        super(name, color);
    }

    public Target_Action(String target, double value, String nom, String color) {
        this(value, nom, color);
        target_name.setValue(target);
    }

    public Target_Action(String target, String value, String nom, String color){
        // constructeur qui prends target et la valeur en string, utile pour les conversion file -> action
        super(value, nom, color);
        target_name.setValue(target);
    }

    public ReadOnlyStringProperty getTargetNameProperty() {
        return target_name;
    }

     public String getTargetted() {
        return target_name.getValue();
     }

     public void setTarget(String name){
        target_name.setValue(name);
     }

    @Override
    public String toString() {
        //String variable = (variableHasName() ? variableName() : (getValue() == 0 ? "" : String.valueOf(getValue())));
        return getNom() + " : " + getTargetted() + " = " + getStringValue();
    }

    @Override
    public String getTarget() {
        return getTargetted();
    }
}
