package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Variable_Table {
    // map qui va contenir toutes les variable, sera dans ExecutionState
    private static final ObservableMap<String, StringProperty> variables = FXCollections.observableHashMap();

    public  static double getValue(String name) {
        return Double.parseDouble(variables.get(name).get().replace(",", "."));
    }

    public static ObservableMap<String, StringProperty> getVariables() {
        return variables;
    }


    public static void setValue(String target, double value) {
        variables.get(target).setValue(String.format("%.0f", value));
    }

    public static void putVariable(String s, SimpleStringProperty simpleStringProperty) {
        variables.put(s, simpleStringProperty);
    }
}
