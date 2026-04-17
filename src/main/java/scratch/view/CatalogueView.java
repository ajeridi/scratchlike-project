package scratch.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import scratch.model.actions.Action;
import scratch.viewmodel.CatalogueViewModel;

public class CatalogueView extends VBox {
    private final Label titleLbl = new Label("Palette d'actions");

    private final ListView<Action> actionListView = new ListView<>();

    private final Button addBtn = new Button("Ajouter au programme");

    private final CatalogueViewModel catalogueViewModel;

    public CatalogueView(CatalogueViewModel vm){
        this.catalogueViewModel = vm;
        configureListView(vm);
        setMargins();
        setActions();
        this.getChildren().addAll(titleLbl, actionListView, addBtn);
    }

    private void setActions() {
        addBtn.setOnAction(event -> addToProgram(getSelectedAction()));

        actionListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2){
                    addToProgram(getSelectedAction());
                }
            }
        });
    }

    private boolean addToProgram(Action selectedAction){
        return catalogueViewModel.addToProgram(selectedAction, "from-catalogue");
    }

    private Action getSelectedAction() {
        return actionListView.getSelectionModel().getSelectedItem();
    }

    private void setMargins() {
        setMargin(actionListView, new Insets(10, 0, 10, 0));
    }

    private void configureListView(CatalogueViewModel vm) {
        actionListView.setItems(vm.getCatalogue());
        setCellFactory();
    }


    // definition de l'affichage de chaque action dans la liste
    private void setCellFactory(){
        actionListView.setCellFactory(list -> new ListCell<Action>() {
            @Override
            protected void updateItem(Action action, boolean empty) {
                super.updateItem(action, empty);

                if (empty || action == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(action.getNom());
                    setStyle("-fx-text-fill: " + action.getColor() + ";");
                }
            }
        });
    }

}
