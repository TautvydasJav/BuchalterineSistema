package system.controllers;

import system.model.AccountingOS;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCategoryControl {
    @FXML
    public TextField newName;
    @FXML
    public Button saveBtn;
    @FXML
    public TextField newDesc;

    private AccountingOS accountingOS;
    String id;

    public void setAccountingOS(AccountingOS accountingOS){
        this.accountingOS = accountingOS;
    }
    public void setId(String id){
        this.id = id;
    }

    public void saveChanges() throws Exception {
        if(!isNewNameFieldEmpty()){
            accountingOS.editNameOfCategory(id, newName.getText());
        }
        if(!isNewDescFieldEmpty()){
            accountingOS.editDescriptionOfCategory(id, newDesc.getText());
        }
        if(isNewDescFieldEmpty() && isNewNameFieldEmpty()){
            alertPopUp();
        }
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public boolean isNewNameFieldEmpty() {         // legal person (company)
        if(newName.getText().isEmpty())
            return true;
        return false;
    }
    public boolean isNewDescFieldEmpty() {         // legal person (company)
        if(newDesc.getText().isEmpty())
            return true;
        return false;
    }
    public void alertPopUp() {         // legal person (company)
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action invalid");
        alert.setHeaderText("Please enter at least one field");
        alert.showAndWait();
    }
}
