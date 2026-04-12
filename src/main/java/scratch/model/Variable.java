package scratch.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Variable {
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleDoubleProperty value = new SimpleDoubleProperty();

    public Variable(String name, double value){
        this.name.set(name);
        this.value.set(value);
    }

    // constructeur pour les variables litterale
    public Variable(double value){
        this("", value);
    }

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    // methode qui va verifier si la variable a un nom (entiere) ou pas (litterale)
    public boolean hasName(){
        return !name.getValue().isEmpty();
    }


    public String validateName(String name){
        if (!validateChars(name))
            return "Name can only contain letters, digits, or the _ symbol";
        return "";
    }

    private boolean validateChars(String s){
        s = s.toLowerCase();
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (i == 0 && isNumeric(c))
                return false;
            else if (!isAlpha(c) || !isNumeric(c) || c != '_') {
                return false;
            }
        }
        return true;
    }

    private boolean isNumeric(char c){
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c){
        return c >= 'a' && c <= 'z';
    }


}
