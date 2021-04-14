package system.controllers;

import javafx.fxml.FXML;
import system.hiberante.AccountingHibernateCtrl;
import system.model.AccountingOS;
import system.model.users.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import static system.constants.URLConstants.*;

public class LoginControl{

    @FXML
    public Button signInBtn;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField pswField;
    @FXML
    public TextField version;

    private AccountingOS accountingOS;

    EntityManagerFactory factory = Persistence.createEntityManagerFactory(DATABASE_NAME);
    AccountingHibernateCtrl hibernateControl = new AccountingHibernateCtrl(factory);

    public void checkIfAccountingTableExists() throws Exception {
        if(hibernateControl.getAccountingOSCount() == 0){
            AccountingOS newAccounting = new AccountingOS("Prif18/03", "1.0.0");
            hibernateControl.create(newAccounting);
            newAccounting.addUserEmp(new Employee("tautvydas", "jav", "123", "admin", 3, accountingOS));
        }
        accountingOS = hibernateControl.findFirstAccountingTable();
        accountingOS.setUsersFromDB();
    }

    public void validateUser(){
        boolean isValidLogin;
        isValidLogin = accountingOS.logIn(loginField.getText(), pswField.getText());
        if(isValidLogin) {
            displayLoginPopup("Valid login", "Entered username and password is correct",  Alert.AlertType.CONFIRMATION);
            try {
                loadMainWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            displayLoginPopup("Invalid login","Entered username or password is incorrect", Alert.AlertType.ERROR);
    }

    public void loadMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU_PAGE_URL));
        Parent root = loader.load();

        MenuPageControl controller = loader.getController();
        controller.setUp(accountingOS);

        Stage stage = (Stage) signInBtn.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void displayLoginPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}