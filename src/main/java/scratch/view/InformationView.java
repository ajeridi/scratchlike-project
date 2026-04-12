package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import scratch.viewmodel.InformationViewModel;

import java.util.Map;

public class InformationView extends VBox {

    // Info du curseur
    private final Label cursorInformation = new Label();
    private final InformationViewModel informationViewModel;
    private final DoubleProperty cursorX = new SimpleDoubleProperty();
    private final DoubleProperty cursorY = new SimpleDoubleProperty();
    private final DoubleProperty cursorDirection = new SimpleDoubleProperty();

    // Info des variables
    private final Label variableTitleLabel = new Label("Variables :");
    private final TableView<Map.Entry<String, StringProperty>> variableTable = new TableView(); // https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
    private final TableColumn<Map.Entry<String, StringProperty>, String> varNameCol = new TableColumn("Nom");
    private final TableColumn<Map.Entry<String, StringProperty>, String> varValueCol = new TableColumn("Valeur");


    public InformationView(InformationViewModel vm){
        this.informationViewModel = vm;

        setBindings();

        this.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10px;");

        setTableView();

        this.getChildren().addAll(cursorInformation, variableTitleLabel, variableTable);

    }

    private void setTableView() {
        variableTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        variableTable.setItems(informationViewModel.getObservableListOfMap());
        varNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        varValueCol.setCellValueFactory(cellData -> cellData.getValue().getValue());
        variableTable.getColumns().addAll(varNameCol, varValueCol);
    }

    private void setBindings() {
        cursorX.bind(informationViewModel.cursorXProperty());
        cursorY.bind(informationViewModel.cursorYProperty());
        cursorDirection.bind(informationViewModel.cursorDirectionProperty());
        cursorInformation.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    return "Tortue : x = " + String.format("%.2f", cursorX.get() * -1) + ", y = " + String.format("%.2f", cursorY.get() * -1) + ", direction = " + String.format("%.2f", cursorDirection.get())  + "°";
                },
                cursorX, cursorY, cursorDirection
        ));
    }
}
