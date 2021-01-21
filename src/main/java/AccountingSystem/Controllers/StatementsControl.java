package AccountingSystem.Controllers;

import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Statements.Expense;
import AccountingSystem.Model.Statements.Income;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    private List<Expense> expenses;
    private List<Income> income;
    private String id;
    private String selectedExpenseId = "";
    private String selectedIncomeId = "";

    public void setUp(AccountingOS OS, String id){
        this.accountingOS = OS;
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
        expenses = accountingOS.getExpenseListById(id);
        for(Expense exp: expenses){
            expenseList.getItems().add(" ID: " + exp.getId() + ", " + exp.getName() + " (" + exp.getAmount() + ")");
        }
    }
    public void fillIncomeList(){
        incomeList.getItems().clear();
        income = accountingOS.getIncomeListById(id);
        for(Income inc: income){
            incomeList.getItems().add(" ID: " + inc.getId() + ", " + inc.getName() + " (" + inc.getAmount() + ")");
        }
    }

    public void editExpense(ActionEvent actionEvent) throws IOException {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(selectedExpenseId.isEmpty()){
            showAlertFailed("Select statement first");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/EditStatement.fxml"));
        Parent root = loader.load();

        EditStatementControl control = loader.getController();
        control.setUp(accountingOS,id, selectedExpenseId, "Expense");

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        fillExpenseList();
    }

    public void editIncome(ActionEvent actionEvent) throws IOException {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(selectedIncomeId.isEmpty()){
            showAlertFailed("Select statement first");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/EditStatement.fxml"));
        Parent root = loader.load();

        EditStatementControl control = loader.getController();
        control.setUp(accountingOS,id, selectedIncomeId, "Income");

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        fillIncomeList();
    }

    public void deleteExpense(ActionEvent actionEvent) throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(selectedExpenseId.isEmpty()){
            showAlertFailed("Select statement first");
            return;
        }
        accountingOS.deleteExpense(id, selectedExpenseId);
        fillExpenseList();
    }

    public void deleteIncome(ActionEvent actionEvent) throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(selectedIncomeId.isEmpty()){
            showAlertFailed("Select statement first");
            return;
        }
        accountingOS.deleteIncome(id, selectedIncomeId);
        fillIncomeList();
    }

    public void addStatement(ActionEvent actionEvent) throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(checkIfAddFieldsAreEmpty()){
            showAlertFailed("Please fill all fields");
        }else{
            if(areAllNumbers()) {
                if (type.getValue().toString().equals("Expense"))
                    accountingOS.addExpense(id, nameField.getText(), amountField.getText());
                else if (type.getValue().toString().equals("Income")) {
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

    public void getSelectedExpenseItem(MouseEvent mouseEvent) {
        String listItemText;
        if(expenseList.getSelectionModel().isEmpty()) // if no items selected return
            return;

        listItemText = expenseList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(",");
        split = split[0].split("ID: ");
        selectedExpenseId = split[1];
        System.out.println(selectedExpenseId);
    }

    public void getSelectedIncomeItem(MouseEvent mouseEvent) {
        String listItemText;
        if(incomeList.getSelectionModel().isEmpty())
            return;

        listItemText = incomeList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(",");
        split = split[0].split("ID: ");
        selectedIncomeId = split[1];
        System.out.println(selectedIncomeId);
    }

    public void deselectItem(MouseEvent mouseEvent) {
        incomeList.getSelectionModel().clearSelection();
        expenseList.getSelectionModel().clearSelection();
        selectedExpenseId = "";
        selectedIncomeId = "";
    }

    public void exit(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/CategoriesPage.fxml"));
        Parent root = loader.load();

        CategoriesControl controller = loader.getController();
        controller.setAccountingOS(accountingOS);

        Stage stage = (Stage) exitBtn.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void displayAccessAlertPopUp(String accessRequired) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access denied");
        alert.setHeaderText("Access required :" + accessRequired);
        alert.showAndWait();
    }
}
