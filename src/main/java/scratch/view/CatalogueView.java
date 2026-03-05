package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
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

        setMargin(actionListView, new Insets(10, 0, 10, 0));
        this.getChildren().addAll(titleLbl, actionListView, addBtn);


    }

}
