package AccountingSystem.Controllers;

import AccountingSystem.HibernateControl.AccountingHibernateCtrl;
import AccountingSystem.HibernateControl.UsersHibernateCtrl;
import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginControl{
    public Button signInBtn;
    public TextField loginField;
    public PasswordField pswField;
    public TextField Name;
    public TextField version;

    private AccountingOS accountingOS;
    private User loggedInUser;

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    AccountingHibernateCtrl hibernateControl = new AccountingHibernateCtrl(factory);



    public void checkIfAccountingTableExists() throws Exception {
        if(hibernateControl.getAccountingOSCount() == 0){
            AccountingOS accountingOS = new AccountingOS("Prif18/03", "1.0.0");
            hibernateControl.create(accountingOS);
        }
        System.out.println("Database empty, created new accounting table");
        accountingOS = hibernateControl.findFirstAccountingTable();
        accountingOS.setUsersFromDB();
    }

    public void validateUser(ActionEvent actionEvent) throws IOException {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/MenuPage.fxml"));
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