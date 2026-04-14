package scratch.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import scratch.model.actions.Action;
import scratch.model.actions.Pen;
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

    // detail de l'action
    private final TitledPane detailActionPane = new TitledPane();
    private final Label detailActionLabel = new Label();
    private final Label detailActionUnitLabel = new Label();
    private final TextField detailActionValueTextField = new TextField();
    private final Label valueErrorLabel = new Label("Erreur valeur");
    private final HBox targetHbox = new HBox();
    private final TextField detailTargetTextField = new TextField();
    private final HBox valueIncrementDecrementBox = new HBox(); // box qui va contenir les boutons + et - pour augmenter la valeur des valeur literale
    private final Button plus = new Button("+");
    private final Button minus = new Button("-");

    private Action currentBindedAction;
    private final IntegerProperty indexToSelect = new SimpleIntegerProperty(); // va servir lors de l'execution du programme
    private final BooleanProperty isValidValue = new SimpleBooleanProperty();


    private final ProgramViewModel programViewModel;

    public ProgramView(ProgramViewModel vm) {
        this.programViewModel = vm;

        configureButtonsBox();

        createAccordion();

        setActionsListView();

        setMargins();

        setBindings();

        setActions();
        setListeners();

        this.getChildren().addAll(titleLbl, actions, buttons, accordion);

    }

    private void configureButtonsBox() {
        buttons.getChildren().addAll(upBtn, downBtn, duplicateBtn, deleteBtn, deleteAllBtn);
        buttons.setSpacing(20);
    }

    private void setActions() {

        getSelectedItemProperty().addListener( // binding vers l'action selectionnee
                (obs, o, n) -> {
                    updateTextField();
                }
        );

        upBtn.setOnAction(actionEvent -> {
            moveAction("up");
        });

        downBtn.setOnAction(actionEvent -> {
            moveAction("down");
        });

        duplicateBtn.setOnAction(actionEvent -> {
            duplicateAction(getSelectedAction()); // ajouter l'action selectionnee a la liste d'action
        });

        deleteBtn.setOnAction(actionEvent -> {
            deleteAction(getSelectedAction());
        });

        deleteAllBtn.setOnAction(actionEvent -> {
            deleteAllActions();
        });

        // pour le bouton plus et le bouton moins, quand on clique dessus, on incremente la valeur contenue dans detailActionValueTextField
        plus.setOnAction( actionEvent -> {
            incrementValue();
        });
        minus.setOnAction(actionEvent -> {
            decrementValue();
        });
    }

    private void decrementValue() {
        programViewModel.decrementValue();
    }

    private void incrementValue() {
        programViewModel.incrementValue();
    }

    private void setListeners() {
        indexToSelect.addListener(
                ((observableValue, number, t1) -> {
                    selectAction(indexToSelect.getValue()); // selection depuis la scene permet de selectionner ici
                })
        );
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
        selectAction(getActionsSizeProperty().intValue() - 1); // diriger l'index vers le dernier element de la list
    }

    private void deleteAction(Action action){
        programViewModel.deleteAction(action);
    }

    private void deleteAllActions(){
        programViewModel.deleteAllActions();
    }


    private void setBindings() {

        indexToSelect.bind(programViewModel.getSelectedIndexProperty());

        programViewModel.getCurrentSelectedActionProperty().bind(getSelectedItemProperty());

        isValidValue.bind(programViewModel.isValidValueProperty());
        valueErrorLabel.visibleProperty().bind(isValidValue.not().and(getSelectedItemProperty().isNotNull()));
        detailActionValueTextField.textProperty().bindBidirectional(programViewModel.getVariableInputProperty());
        detailTargetTextField.textProperty().bindBidirectional(programViewModel.getTargetInputProperty());

        targetHbox.visibleProperty().bind(programViewModel.isTargetAction());
        targetHbox.managedProperty().bind(targetHbox.visibleProperty()); // si invisible, espace rempli par d'autres composants

        // la box contenant le plus et le moins apparait uniquement si la valeur contenue dans le textfield est un nombre, et si le textfield value est visible
        valueIncrementDecrementBox.visibleProperty().bind(programViewModel.isValueNumeric().and(detailActionValueTextField.visibleProperty()));
        valueIncrementDecrementBox.managedProperty().bind(valueIncrementDecrementBox.visibleProperty()); // si invisible, espace rempli par d'autres composants

        setDetailElementsBindings();
        setButtonsBindings();
    }

    private void setDetailElementsBindings() {
        setDetailActionLabelBindings();
        setDetailActionUnitLabelBindings();
        setDetailActionValueTextFieldBindings();
    }

    private void setButtonsBindings() {
        upBtn.disableProperty().bind(isSelectedProperty().not().or(getActionsSelectedIndexProperty().isEqualTo(0))); // desactivation du bouton Monter si on ne selectionne rien, ou si on est tout en haut de la liste
        downBtn.disableProperty().bind(isSelectedProperty().not().or(getActionsSelectedIndexProperty().isEqualTo(getActionsSizeProperty().subtract(1)))); // desactivation du bouton descendre si on ne selectionne rien ou bien si on est tout en bas de la liste
        duplicateBtn.disableProperty().bind(isSelectedProperty().not()); // desactivation du bouton dupliquer si rien n'est selectionne
        deleteBtn.disableProperty().bind(isSelectedProperty().not()); // desactivation du bouton supprimer si rien n'est selectionne
        deleteAllBtn.disableProperty().bind(actionsIsEmptyProperty()); // desactivation du bouton deleteAll si liste vide
    }

    private void setDetailActionValueTextFieldBindings() {
        detailActionValueTextField.disableProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    return actionsIsEmptyProperty().getValue() || getSelectedAction() == null || getSelectedAction() instanceof Pen;
                },
                getSelectedItemProperty()
        ));

        detailActionValueTextField.visibleProperty().bind(programViewModel.isActionWithValue());
    }

    private void setDetailActionUnitLabelBindings() { // fait le binding entre le label de l'unite de la valeur de l'action et le string valueUNit de action
        detailActionUnitLabel.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    var selectedAction = getSelectedAction();
                    if (selectedAction != null)
                        return selectedAction.valueUnit();
                    return "";
                },
                getSelectedItemProperty()
        ));
    }

    private void setDetailActionLabelBindings() {
        detailActionLabel.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    var selectedAction = getSelectedAction();
                    if (selectedAction != null)
                        return selectedAction.getDetailString();
                    return "aucune action selectionnee";
                },
                getSelectedItemProperty()
        ));
    }

    private Action getSelectedAction() {
        return actions.getSelectionModel().getSelectedItem();
    }

    private ReadOnlyIntegerProperty getActionsSelectedIndexProperty() {
        return actions.getSelectionModel().selectedIndexProperty();
    }

    private ReadOnlyObjectProperty<Action> getSelectedItemProperty() {
        return actions.getSelectionModel().selectedItemProperty();
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
        VBox actionPaneBox = new VBox();

        HBox topSection = new HBox(detailActionLabel, targetHbox, detailActionValueTextField, valueIncrementDecrementBox, detailActionUnitLabel);

        Label de = new Label("de");
        targetHbox.getChildren().addAll(detailTargetTextField, de);
        targetHbox.setAlignment(Pos.CENTER_LEFT);
        targetHbox.setSpacing(5);
        valueErrorLabel.setTextFill(Color.RED);
        setActionPaneBox(topSection);

        detailActionPane.setText("Detail de l'action");
        detailActionValueTextField.setPrefWidth(45);
        detailTargetTextField.setPrefWidth(45);

        valueIncrementDecrementBox.getChildren().addAll(plus, minus);

       actionPaneBox.getChildren().addAll(topSection, valueErrorLabel);
        detailActionPane.setContent(actionPaneBox);
        detailActionPane.setAlignment(Pos.BASELINE_LEFT);
        accordion.getPanes().add(detailActionPane);
        accordion.setExpandedPane(detailActionPane);
    }

    private static void setActionPaneBox(HBox actionPaneBox) {
        actionPaneBox.setSpacing(10.0);
        actionPaneBox.setAlignment(Pos.CENTER_LEFT);
    }

    private void updateTextField() {
        programViewModel.updateUserInput();
        programViewModel.updateTargetInput();
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
                    // on ajoute autant d'indentation que
                    System.out.println(action.toString());
                    setText(action.getIndentation() + action.toString());
                    setStyle("-fx-text-fill: " + action.getColor() + ";");
                }
            }
        });
    }
}
