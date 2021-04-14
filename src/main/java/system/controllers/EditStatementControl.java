package system.controllers;

import system.model.AccountingOS;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditStatementControl {

    @FXML
    public Button saveBtn;
    @FXML
    public TextField newNameField;
    @FXML
    public TextField newAmountField;

    private AccountingOS accountingOS;
    String catId;
    String statementId;
    String type;

    public void setUp(AccountingOS accountingOS, String catId, String statementId, String type){
        this.accountingOS = accountingOS;
        this.catId = catId;
        this.statementId = statementId;
        this.type = type;
    }

    public void saveChanges() throws Exception {
        String newName = "";
        String newAmount = "";
        if(!isNewNameFieldEmpty()){
            newName = this.newNameField.getText();
        }
        if(!isNewAmountFieldEmpty()){
            if(areAllNumbers())
                newAmount = this.newAmountField.getText();
            else
                AlertPopUp("Please fill amount using only integers");
        }
        if(isNewAmountFieldEmpty() && isNewNameFieldEmpty()){
            AlertPopUp("Please enter at least one field");
            return;
        }

        if(type.equals("Income"))
            accountingOS.editIncome(catId, statementId, newName, newAmount); // if the string is empty he wll not change the old string
        if(type.equals("Expense"))
            accountingOS.editExpense(catId, statementId, newName, newAmount);

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public boolean isNewNameFieldEmpty() {
        if(newNameField.getText().isEmpty())
            return true;
        return false;
    }

    public boolean isNewAmountFieldEmpty() {
        if(newAmountField.getText().isEmpty())
            return true;
        return false;
    }

    public void AlertPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action invalid");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public boolean areAllNumbers() {
        if (newAmountField.getText().matches("[0-9]+"))
            return true;
        return false;
    }
}