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

    public Variable(String varString){
        if (isNumber(varString)){
            this.name.set("");
            this.value.set(Double.parseDouble(varString));
        }
        else
            this.name.set(varString);
    }

    private boolean isNumber(String varString) {
        boolean isNumber = false;
        try{
            Double.parseDouble(varString);
            isNumber = true;
        } catch (Exception _){
        }
        return isNumber;
    }

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value){
        // si je set une value, le nom disparait car la variable devient litterale
        this.value.set(value);
        setName("");
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name){
        this.name.set(name);
    }

    // methode qui va verifier si la variable a un nom (entiere) ou pas (litterale)
    public boolean hasName(){
        return !name.getValue().isEmpty();
    }


    public static String validateName(String name){
        if (!validateChars(name))
            return "Name can only contain letters, digits, or the _ symbol";
        return "";
    }

    private static boolean validateChars(String s){
        s = s.toLowerCase();
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            System.out.println(c);
            if (i == 0 && isNumeric(c)) { // si on commence par un chiffre
                System.out.println("first letter is numeric");
                return false;
            }

            else if (!isAlpha(c) && !isNumeric(c) && c != '_') { // si on est ni une lettre, ni un chiffre ni _
                System.out.print("letter is not valid");
                return false;
            }
        }
        return true;
    }

    private static boolean isNumeric(char c){
        return c >= '0' && c <= '9';
    }

    private static boolean isAlpha(char c){
        return c >= 'a' && c <= 'z';
    }

    public double getValue() {
        if (hasName())
            return Variable_Table.getValue(getName());
        return value.get();
    }


}
