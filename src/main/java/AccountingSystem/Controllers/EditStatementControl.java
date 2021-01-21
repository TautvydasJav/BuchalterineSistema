package AccountingSystem.Controllers;

import AccountingSystem.Model.AccountingOS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditStatementControl {
    @FXML
    public TextField newName;
    @FXML
    public Button saveBtn;
    @FXML
    public TextField newAmount;

    private AccountingOS accountingOS;
    String catId;
    String statementId;
    String type;

    public void setUp(AccountingOS OS, String catId, String statementId, String type){
        this.accountingOS = OS;
        this.catId = catId;
        this.statementId = statementId;
        this.type = type;
    }

    public void saveChanges(ActionEvent actionEvent) throws Exception {
        String newName = "";
        String newAmount = "";
        if(!isNewNameFieldEmpty()){
            newName = this.newName.getText();
        }
        if(!isNewAmountFieldEmpty()){
            if(areAllNumbers())
                newAmount = this.newAmount.getText();
            else
                AlertPopUp("Please fill amount using only integers");
        }
        if(isNewAmountFieldEmpty() && isNewNameFieldEmpty() == true){
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
        if(newName.getText().isEmpty())
            return true;
        return false;
    }

    public boolean isNewAmountFieldEmpty() {
        if(newAmount.getText().isEmpty())
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
        if (newAmount.getText().matches("[0-9]+"))
            return true;
        return false;
    }
}