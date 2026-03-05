package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scratch.model.Action;
import scratch.viewmodel.ProgramViewModel;

public class ProgramView extends VBox {

    private final Label titleLbl = new Label("Programme");
    private final ListView<Action> actions = new ListView<>();
    private final Button upBtn = new Button("Monter");
    private final Button downBtn = new Button("Descendre");
    private final Button duplicateBtn = new Button("Dupliquer");
    private final Button deleteBtn = new Button("Supprimer");
    private final Button deleteAllBtn = new Button("Vider tout");
    private final Accordion accordion = new Accordion();

    private final ProgramViewModel vm;

    public ProgramView(ProgramViewModel vm) {
        this.vm = vm;
        createAccordion();
        setMargin(actions, new Insets(10, 0, 10, 0));
        actions.setMinWidth(500);
        this.getChildren().addAll(titleLbl, actions, accordion);

    }

    private void createAccordion(){
        TitledPane pane1 = new TitledPane("Detail de l'action" , new HBox(new Label("(aucune action sélectionnée)"), new TextField()));
        accordion.getPanes().add(pane1);
        accordion.setExpandedPane(pane1);
    }
}
