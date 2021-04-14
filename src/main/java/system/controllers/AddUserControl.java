package system.controllers;

import system.enums.Role;
import system.model.AccountingOS;
import system.model.users.Employee;
import system.model.users.LegalPerson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserControl implements Initializable {
    @FXML
    public TextField loginNameEmpField;
    @FXML
    public PasswordField passwordEmpField;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public ChoiceBox accessEmpLevel;
    @FXML
    public Button addEmployeeBtn;
    @FXML
    public TextField loginNameCompField;
    @FXML
    public PasswordField passwordCompField;
    @FXML
    public TextField companyNameField;
    @FXML
    public ChoiceBox accessLevelComp;
    @FXML
    public Button addLegalBtn;

    private AccountingOS accountingOS;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillChoiceBoxes();
    }

    public void setAccountingOS(AccountingOS accountingOS){
        this.accountingOS = accountingOS;
    }

    public void fillChoiceBoxes(){
        accessEmpLevel.getItems().addAll(Role.LOW, Role.MEDIUM, Role.ADMIN);
        accessLevelComp.getItems().addAll(Role.LOW, Role.MEDIUM, Role.ADMIN);
    }

    public void addEmployee(){
        if(checkIfEmptyEmployee())
            showAlertFailed("Please enter all fields");
        else{
            if(!accountingOS.checkIfUserNameExists(loginNameEmpField.getText())){

                int accessLevel = getAccessLevel(accessEmpLevel);

                Employee newUser = new Employee(firstNameField.getText(),
                                        lastNameField.getText(),
                                        passwordEmpField.getText(),
                                        loginNameEmpField.getText(),
                                        accessLevel,
                                        accountingOS);
                try {
                    accountingOS.addUserEmp(newUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                showAlertFailed("Login name already exists");
        }
        Stage stage = (Stage) addEmployeeBtn.getScene().getWindow();
        stage.close();
    }

    public void addCompany(){
        if(checkIfEmptyCompany())
            showAlertFailed("Please enter all fields");
        else{
            if(!accountingOS.checkIfUserNameExists(loginNameCompField.getText())){

                int accessLevel = getAccessLevel(accessLevelComp);

                LegalPerson newUser = new LegalPerson(companyNameField.getText(),
                        passwordCompField.getText(),
                        loginNameCompField.getText(),
                        accessLevel);
                try {
                    accountingOS.addUserLegalPerson(newUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                showAlertFailed("Login name already exists");
        }
        Stage stage = (Stage) addLegalBtn.getScene().getWindow();
        stage.close();
    }

    public boolean checkIfEmptyEmployee() {           // employee
        if(loginNameEmpField.getText().isEmpty())
            return true;
        if(passwordEmpField.getText().isEmpty())
            return true;
        if(firstNameField.getText().isEmpty())
            return true;
        if(lastNameField.getText().isEmpty())
            return true;
        if(accessEmpLevel.getSelectionModel().isEmpty())
            return true;
        return false;
    }

    public boolean checkIfEmptyCompany() {         // legal person (company)
        if(loginNameCompField.getText().isEmpty())
            return true;
        if(passwordCompField.getText().isEmpty())
            return true;
        if(companyNameField.getText().isEmpty())
            return true;
        if(accessLevelComp.getSelectionModel().isEmpty())
            return true;
        return false;
    }

    public int getAccessLevel(ChoiceBox accessString) {  // convert access level from string to integer
        String accessLvl = accessString.getValue().toString();
        if(accessLvl.equals(Role.LOW.toString()))
            return 1;
        if(accessLvl.equals(Role.MEDIUM.toString()))
            return 2;
        if(accessLvl.equals(Role.ADMIN.toString()))
            return 3;
        return 0;
    }

    public void showAlertFailed(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid information");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
