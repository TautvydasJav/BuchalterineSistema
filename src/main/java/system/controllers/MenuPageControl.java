package system.controllers;

import system.model.AccountingOS;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MenuPageControl {
    @FXML
    public Button users;
    @FXML
    public Button categories;
    @FXML
    public Label loggedInLabel;
    @FXML
    public Button exit;

    private AccountingOS accountingOS;

    public void setUp(AccountingOS accountingOS){
        this.accountingOS = accountingOS;
        showLoggedInInfo();
    }

    public void openUsers() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/UserPage.fxml"));
        Parent root = loader.load();

        UserControl controller = loader.getController();
        controller.setAccountingOS(accountingOS);

        Stage stage = (Stage) users.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void openCategories() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/CategoriesPage.fxml"));
        Parent root = loader.load();

        CategoriesControl controller = loader.getController();
        controller.setAccountingOS(accountingOS);

        Stage stage = (Stage) users.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showLoggedInInfo(){
        String text = "Logged in as: " + "\n" +
                        accountingOS.getLoggedInUser().getName() + "\n" +
                       "Access level: " + accountingOS.getLoggedInUser().getAccessLevelString() + "\n";
        loggedInLabel.setText(text);
    }

    public void exitAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Alert");
        alert.setHeaderText("Do you really want to exit the program?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
                System.exit(0);
    }

}
