package system.controllers;

import system.enums.Role;
import system.enums.StatementType;
import system.model.AccountingOS;
import system.model.statements.Expense;
import system.model.statements.Income;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import static system.constants.URLConstants.*;

public class StatementsControl implements Initializable {
    @FXML
    public ListView expenseList;
    @FXML
    public Button editExpenseBtn;
    @FXML
    public Button deleteExpenseBtn;
    @FXML
    public ListView incomeList;
    @FXML
    public Button editIncomeBtn;
    @FXML
    public Button deleteIncomeBtn;
    @FXML
    public TextField amountField;
    @FXML
    public TextField nameField;
    @FXML
    public ChoiceBox type;
    @FXML
    public Button addStatementBtn;
    @FXML
    public Button exitBtn;

    private AccountingOS accountingOS;
    private String id;
    private String selectedExpenseId = "";
    private String selectedIncomeId = "";

    public static final String ALERT =  "Select statement first";

    public void setUp(AccountingOS accountingOS, String id){
        this.accountingOS = accountingOS;
        this.id = id;
        fillExpenseList();
        fillIncomeList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillChoiceBox();
    }

    public void fillChoiceBox(){
        type.getItems().addAll("Expense", "Income");
    }

    public void fillExpenseList(){
        expenseList.getItems().clear();
        List<Expense> expenses;
        expenses = accountingOS.getExpenseListById(id);
        for(Expense exp: expenses){
            expenseList.getItems().add(" ID: " + exp.getId() + ", " + exp.getName() + " (" + exp.getAmount() + ")");
        }
    }
    public void fillIncomeList(){
        incomeList.getItems().clear();
        List<Income> income;
        income = accountingOS.getIncomeListById(id);
        for(Income inc: income){
            incomeList.getItems().add(" ID: " + inc.getId() + ", " + inc.getName() + " (" + inc.getAmount() + ")");
        }
    }

    public void editExpense() throws IOException {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(selectedExpenseId.isEmpty()){
            showAlertFailed(ALERT);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_STATEMENT_URL));
        Parent root = loader.load();

        EditStatementControl control = loader.getController();
        control.setUp(accountingOS,id, selectedExpenseId, StatementType.Expense.toString());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        fillExpenseList();
    }

    public void editIncome() throws IOException {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(selectedIncomeId.isEmpty()){
            showAlertFailed(ALERT);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_STATEMENT_URL));
        Parent root = loader.load();

        EditStatementControl control = loader.getController();
        control.setUp(accountingOS,id, selectedIncomeId, StatementType.Income.toString());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        fillIncomeList();
    }

    public void deleteExpense() throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(selectedExpenseId.isEmpty()){
            showAlertFailed(ALERT);
            return;
        }
        accountingOS.deleteExpense(id, selectedExpenseId);
        fillExpenseList();
    }

    public void deleteIncome() throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(selectedIncomeId.isEmpty()){
            showAlertFailed(ALERT);
            return;
        }
        accountingOS.deleteIncome(id, selectedIncomeId);
        fillIncomeList();
    }

    public void addStatement() throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(checkIfAddFieldsAreEmpty()){
            showAlertFailed("Please fill all fields");
        }else{
            if(areAllNumbers()) {
                if (type.getValue().toString().equals(StatementType.Expense))
                    accountingOS.addExpense(id, nameField.getText(), amountField.getText());
                else if (type.getValue().toString().equals(StatementType.Income)) {
                    accountingOS.addIncome(id, nameField.getText(), amountField.getText());
                }
            }
            else
                showAlertFailed("Please fill amount using only integers");
        }
        fillExpenseList();
        fillIncomeList();
    }

    public boolean areAllNumbers() {
        if (amountField.getText().matches("[0-9]+"))
            return true;
        return false;

    }

    public boolean checkIfAddFieldsAreEmpty() {
        if(amountField.getText().isEmpty())
            return true;
        if(nameField.getText().isEmpty())
            return true;
        if(type.getSelectionModel().isEmpty())
            return true;
        return false;
    }

    public void showAlertFailed(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid information");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void getSelectedExpenseItem( ) {
        String listItemText;
        if(expenseList.getSelectionModel().isEmpty()) // if no items selected return
            return;

        listItemText = expenseList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(",");
        split = split[0].split("ID: ");
        selectedExpenseId = split[1];
    }

    public void getSelectedIncomeItem( ) {
        String listItemText;
        if(incomeList.getSelectionModel().isEmpty())
            return;

        listItemText = incomeList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(",");
        split = split[0].split("ID: ");
        selectedIncomeId = split[1];
    }

    public void deselectItem() {
        incomeList.getSelectionModel().clearSelection();
        expenseList.getSelectionModel().clearSelection();
        selectedExpenseId = "";
        selectedIncomeId = "";
    }

    public void exit() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CATEGORIES_URL));
        Parent root = loader.load();

        CategoriesControl controller = loader.getController();
        controller.setAccountingOS(accountingOS);

        Stage stage = (Stage) exitBtn.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void displayAccessAlertPopUp(Role accessRequired) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access denied");
        alert.setHeaderText("Access required :" + accessRequired);
        alert.showAndWait();
    }
}
