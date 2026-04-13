package scratch.model.actions;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import scratch.model.ActionList;
import scratch.model.Scene;
import scratch.model.Variable;

public abstract class Action {
    private final String nom;
   // private final SimpleDoubleProperty value = new SimpleDoubleProperty();
    private static final double DEFAULT_ACTION_VALUE = 0;
    private final Variable variable;
    private  String color;
    private int loopDepth = 0;

    public int getLoopDepth() {
        return loopDepth;
    }

    public void setLoopDepth(int loopDepth) {
        this.loopDepth = loopDepth;
    }


    public Action(double value, String nom, String color) {
        this.variable = new Variable(value);
        //this.value.set(value);
        this.nom = nom;
        this.color = color;
    }

    public Action(String value, String nom, String color){
        this.variable = new Variable(value);
        this.nom = nom;
        this.color = color;
    }

    public Action(String nom, String color) {
        this(DEFAULT_ACTION_VALUE, nom, color);
    }

    public String getNom() {
        return nom;
    }


    public double getValue() {

        //return valueProperty().getValue();
        return variable.getValue();
    }

    public SimpleDoubleProperty valueProperty() {
        return variable.valueProperty();
    }

    public SimpleStringProperty variableNameProperty() {
        return variable.nameProperty();
    }

    public boolean variableHasName() {
        return variable.hasName();
    }

    public boolean isValidVarName(String name) {
        return variable.validateName(name).isEmpty();
    }

    public void setVarName(String name) {
        variable.setName(name);
    }

    public String variableName() {
        return variableNameProperty().getValue();
    }

    public void setValue(double value) {
        if (isValidValue(value))
            variable.setValue(value);
            //valueProperty().set(value);
    }

    abstract public void execute(Scene scene);

    abstract public boolean isValidValue(double value);

    abstract public Action create_default();
    abstract public Action copy();

    abstract public String valueUnit();
    abstract public String getType();
    abstract public String getDetailString();

    public void setColor(String color) {
        this.color = color;
    };

    public String getColor(){
        return color;
    }

    @Override
    public String toString() {
        return nom + " "  + (variable.getValue());
    }


    public ActionList getActions() {
        return null;
    }

    public String getIndentation() {
        String indentations = "";
        for (int i = 0; i < loopDepth; ++i){
            indentations += "\t";
        }
        return indentations;
    }

}
