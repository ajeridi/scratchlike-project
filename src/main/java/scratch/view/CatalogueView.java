package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import scratch.model.Action;
import scratch.viewmodel.CatalogueViewModel;

public class CatalogueView extends VBox {
    private final Label titleLbl = new Label("Palette d'actions");

    private final ListView<Action> actionListView = new ListView<>();

    private final Button addBtn = new Button("Ajouter au programme");

    private final CatalogueViewModel vm;

    public CatalogueView(CatalogueViewModel vm){
        this.vm = vm;
        configureListView(vm);
        setMargins();
        this.getChildren().addAll(titleLbl, actionListView, addBtn);
    }

    private void setMargins() {
        setMargin(actionListView, new Insets(10, 0, 10, 0));
    }

    private void configureListView(CatalogueViewModel vm) {
        actionListView.setItems(vm.getCatalogue());
        setCellFactory();
    }


    private void setCellFactory(){
        actionListView.setCellFactory(list -> new ListCell<Action>() {
            @Override
            protected void updateItem(Action action, boolean empty) {
                super.updateItem(action, empty);

                if (empty || action == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(action.toString());
                    setStyle("-fx-text-fill: " + action.getColor() + ";");
                }
            }
        });
    }

}
