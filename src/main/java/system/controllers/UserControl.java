package system.controllers;

import system.model.AccountingOS;
import system.model.users.User;
import system.exceptions.UserException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static system.constants.URLConstants.*;
import static system.constants.UserConstants.*;

public class UserControl implements Initializable {
    @FXML
    public ListView<String> userList;
    @FXML
    public Button backToMenuBtn;
    @FXML
    public Button deleteUserBtn;
    @FXML
    public Button addUserBtn;
    @FXML
    public Button viewBtn;

    private AccountingOS accountingSystem;
    String selectedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void setAccountingOS(AccountingOS accountingSystem){
        this.accountingSystem = accountingSystem;
        this.accountingSystem.setUsersFromDB();
        fillUserList();
    }

    public void fillUserList(){
        userList.getItems().clear();
        List<User> users = new ArrayList<>();
        users.addAll(accountingSystem.getUsers());
        for(User user: users){
            userList.getItems().add( user.getLoginName() + " (" + user.getUserString() + ")");
        }
    }

    public void addUser() throws IOException {

        if(!accountingSystem.checkForAccess(3)){
            displayAccessAlertPopUp(ACCESS_REQUIRED);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ADD_USER_URL));
        Parent root = loader.load();

        AddUserControl control = loader.getController();
        control.setAccountingOS(accountingSystem);

        Stage stage = new Stage();
        stage.setTitle("Add user");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        fillUserList();
    }

    public void deleteUser() throws UserException {

        if(!accountingSystem.checkForAccess(3)){
            displayAccessAlertPopUp(ACCESS_REQUIRED);
            return;
        }

        if(selectedUser.isEmpty())
            showPleaseSelectedUserAlert();
        else{
            String loggedInUserName = accountingSystem.getLoggedInUser().getLoginName();
            if(!loggedInUserName.equals(selectedUser)) {
                try {
                    accountingSystem.deleteUser(selectedUser);
                } catch (Exception e) {
                    throw new UserException();
                }
            }
            else{
                showDeleteUserAlert();
            }
        }
        fillUserList();
        deselectUser();
    }
    private void showDeleteUserAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ACTION_INVALID_MSG);
        alert.setHeaderText(LOGGED_IN_USER);
        alert.showAndWait();
    }

    public void viewUserInfo() {
        if(!accountingSystem.checkForAccess(3)){
            displayAccessAlertPopUp(ACCESS_REQUIRED);
            return;
        }
        if(selectedUser.isEmpty()) {
            showPleaseSelectedUserAlert();
            return;
        }

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("User");
        window.setMinWidth(250);

        User user = accountingSystem.getUser(selectedUser);

        Label name = new Label();
        name.setText("Name: " + user.getName());

        Label type = new Label();
        type.setText("Type: " + user.getType());

        Label loginName = new Label();
        loginName.setText("Login name: " + user.getLoginName());

        Label password = new Label();
        password.setText("Password: " + user.getPsw());

        Label accessLvl = new Label();
        accessLvl.setText("Access level: " + user.getAccessLevelString());

        Label date = new Label();
        date.setText("Date created: " + user.getDateCreated());

        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(50);
        layout.getChildren().addAll(name,type,loginName,password,accessLvl,date,closeButton);
        layout.setSpacing(10);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        selectedUser = "";
    }

    public void goBackToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU_PAGE_URL));
        Parent root = loader.load();

        MenuPageControl controller = loader.getController();
        controller.setUp(accountingSystem);

        Stage stage = (Stage) backToMenuBtn.getScene().getWindow();

        stage.setTitle(TITLE);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void clickedItem() {
        String listItemText;
        if(userList.getSelectionModel().isEmpty()) // if no items selected return
            return;

        listItemText = userList.getSelectionModel().getSelectedItem();
        String[] split = listItemText.split(" ");
        selectedUser = split[0];
    }

    public void deselectItem() {
        userList.getSelectionModel().clearSelection();
        selectedUser = "";
    }

    public void deselectUser() {
        selectedUser = "";
    }

    private void showPleaseSelectedUserAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ACTION_INVALID_MSG);
        alert.setHeaderText(ACTION_INVALID_SELECT_RESPONSE);
        alert.showAndWait();
    }
    private void displayAccessAlertPopUp(String accessRequired) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ACCESS_DENIED_MSG);
        alert.setHeaderText(ACCESS_DENIED_MSG_RESPONSE + accessRequired);
        alert.showAndWait();
    }
}
