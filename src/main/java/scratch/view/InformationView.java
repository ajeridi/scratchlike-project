package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import scratch.viewmodel.InformationViewModel;

public class InformationView extends VBox {

    // Info du curseur
    private final Label cursorInformation = new Label();
    private final InformationViewModel informationViewModel;
    private final DoubleProperty cursorX = new SimpleDoubleProperty();
    private final DoubleProperty cursorY = new SimpleDoubleProperty();
    private final DoubleProperty cursorDirection = new SimpleDoubleProperty();


    public InformationView(InformationViewModel vm){
        this.informationViewModel = vm;

        setBindings();

        this.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        this.getChildren().addAll(cursorInformation);

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
