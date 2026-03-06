package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
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
    private final HBox buttons = new HBox();
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

        configureButtonsBox();

        createAccordion();

        setActionsListView();

        setMargins();

        setBindings();

        setActions();

        this.getChildren().addAll(titleLbl, actions, buttons, accordion);

    }

    private void configureButtonsBox() {
        buttons.getChildren().addAll(upBtn, downBtn, duplicateBtn, deleteBtn, deleteAllBtn);
        buttons.setSpacing(20);
    }

    private void setActions() {
        actions.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                bindTextFieldToActionValue();
            }
        });

        upBtn.setOnAction(actionEvent -> {
            moveAction("up");
        });

        downBtn.setOnAction(actionEvent -> {
            moveAction("down");
        });

        duplicateBtn.setOnAction(actionEvent -> {
            duplicateAction(getSelectedAction()); // ajouter l'action selectionnee a la liste d'action
            selectAction(getActionsSizeProperty().intValue() - 1); // diriger l'index vers le dernier element de la liste
            bindTextFieldToActionValue(); // binder l'action avec le text area
        });

        deleteBtn.setOnAction(actionEvent -> {
            deleteAction(getSelectedAction());
        });
    }

    private void selectAction(int index){
        actions.getSelectionModel().select(index);
    }

    private void moveAction(String way) {
        int selectedIndex = getSelectedIndex();
        int increment = 0;
        switch (way){
            case "up" :
                programViewModel.moveUp(selectedIndex);
                increment--;
                break;
            case "down" :
                programViewModel.moveDown(selectedIndex);
                increment++;
                break;
        }
        selectAction(selectedIndex + increment); // selectionner le nouvel index de l'action
    }

    private void duplicateAction(Action action){
        programViewModel.duplicate(action);
    }

    private void deleteAction(Action action){
        programViewModel.deleteAction(action);
    }

    private void bindTextFieldToActionValue() {
        // converter pour rendre la property de l'action compatible avec la property de textField
        StringConverter<Number> doubleConverter = new NumberStringConverter();
        // afin de ne pas binder tous les actions au meme textfield, il faut unbind le precedent
        if (currentBindedAction != null)
            Bindings.unbindBidirectional(detailActionValueTextField.textProperty(), currentBindedAction.valueProperty());
        currentBindedAction = getSelectedAction();
        Bindings.bindBidirectional(detailActionValueTextField.textProperty(), getSelectedAction().valueProperty(), doubleConverter );
    }

    private void setBindings() {
        setDetailActionLabelBindings();
        setDetailActionUnitLabelBindings();
        setDetailActionValueTextFieldBindings();

        upBtn.disableProperty().bind(isSelectedProperty().not().or(getActionsSelectedIndexProperty().isEqualTo(0))); // desactivation du bouton Monter si on ne selectionne rien, ou si on est tout en haut de la liste
        downBtn.disableProperty().bind(isSelectedProperty().not().or(getActionsSelectedIndexProperty().isEqualTo(getActionsSizeProperty().subtract(1)))); // desactivation du bouton descendre si on ne selectionne rien ou bien si on est tout en bas de la liste
        duplicateBtn.disableProperty().bind(isSelectedProperty().not()); // desactivation du bouton dupliquer si rien n'est selectionne
        deleteBtn.disableProperty().bind(isSelectedProperty().not()); // desactivation du bouton supprimer si rien n'est selectionne
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

    private void setDetailActionUnitLabelBindings() { // fait le binding entre le label de l'unite de la valeur de l'action et le string valueUNit de action
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

    private ReadOnlyIntegerProperty getActionsSelectedIndexProperty() {
        return actions.getSelectionModel().selectedIndexProperty();
    }

    private int getSelectedIndex(){
        return actions.getSelectionModel().getSelectedIndex();
    }

    private IntegerBinding getActionsSizeProperty() {
        return programViewModel.getActionsSizeProperty();
    }

    private BooleanBinding actionsIsEmptyProperty(){
        return programViewModel.actionIsEmptyProperty();
    }

    private BooleanBinding isSelectedProperty() {
        return Bindings.createBooleanBinding(
                () -> {
                    return getSelectedIndex() >= 0 && getSelectedIndex() <= getActionsSizeProperty().getValue();
                },
                getActionsSelectedIndexProperty()
        );
    }


    private void setMargins() {
        setMargin(actions, new Insets(10, 0, 10, 0));
        setMargin(buttons, new Insets(10,10,20,10));
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
