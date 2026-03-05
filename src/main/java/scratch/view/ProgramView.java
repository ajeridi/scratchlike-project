package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import scratch.model.Action;
import scratch.model.Pen;
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

    private final TitledPane detailActionPane = new TitledPane();
    private final Label detailActionLabel = new Label();
    private final Label detailActionUnitLabel = new Label();
    private final TextField detailActionValueTextField = new TextField();

    private Action currentBindedAction;


    private final ProgramViewModel programViewModel;

    public ProgramView(ProgramViewModel vm) {
        this.programViewModel = vm;

        createAccordion();

        setActionsListView();

        setMargins();

        setBindings();

        setActions();

        this.getChildren().addAll(titleLbl, actions, accordion);

    }

    private void setActions() {
        actions.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // converter pour rendre la property de l'action compatible avec la property de textField
                StringConverter<Number> doubleConverter = new NumberStringConverter();
                // afin de ne pas binder tous les actions au meme textfield, il faut unbind le precedent
                if (currentBindedAction != null)
                    Bindings.unbindBidirectional(detailActionValueTextField.textProperty(), currentBindedAction.valueProperty());
                currentBindedAction = getSelectedAction();
                Bindings.bindBidirectional(detailActionValueTextField.textProperty(), getSelectedAction().valueProperty(), doubleConverter );
            }
        });
    }

    private void setBindings() {
        setDetailActionLabelBindings();
        setDetailActionUnitLabelBindings();
        setDetailActionValueTextFieldBindings();

    }

    private void setDetailActionValueTextFieldBindings() {
        detailActionValueTextField.disableProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    if (getSelectedAction() == null || getSelectedAction() instanceof Pen)
                        return true;
                    return false;
                },
                actions.getSelectionModel().selectedIndexProperty()
        ));
    }

    private void setDetailActionUnitLabelBindings() {
        detailActionUnitLabel.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    var selectedAction = getSelectedAction();
                    if (selectedAction != null)
                        return selectedAction.valueUnit();
                    return "";
                },
                actions.getSelectionModel().selectedIndexProperty()
        ));
    }

    private void setDetailActionLabelBindings() {
        detailActionLabel.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    var selectedAction = getSelectedAction();
                    if (selectedAction != null)
                        return selectedAction.getNom();
                    return "aucune action selectionnee";
                },
                actions.getSelectionModel().selectedIndexProperty()
        ));
    }

    private Action getSelectedAction() {
        return actions.getSelectionModel().getSelectedItem();
    }




    private void setMargins() {
        setMargin(actions, new Insets(10, 0, 10, 0));
    }


    private void setActionsListView() {
        actions.setItems(programViewModel.getActions());
        setCellFactory();
        actions.setMinWidth(500);
    }

    private void createAccordion(){
        HBox actionPaneBox = new HBox();

        setActionPaneBox(actionPaneBox);

        detailActionPane.setText("Detail de l'action");
        detailActionValueTextField.setPrefWidth(40);

       actionPaneBox.getChildren().addAll(detailActionLabel, detailActionValueTextField, detailActionUnitLabel);
        detailActionPane.setContent(actionPaneBox);
        detailActionPane.setAlignment(Pos.BASELINE_LEFT);
        accordion.getPanes().add(detailActionPane);
        accordion.setExpandedPane(detailActionPane);
    }

    private static void setActionPaneBox(HBox actionPaneBox) {
        actionPaneBox.setSpacing(10.0);
        actionPaneBox.setAlignment(Pos.CENTER_LEFT);
    }


    // definition de l'affichage de chaque action dans la liste
    private void setCellFactory(){
        actions.setCellFactory(list -> new ListCell<Action>() {
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
