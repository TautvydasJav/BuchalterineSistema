package AccountingSystem.Controllers;

import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserControl implements Initializable {
    @FXML
    public ListView userList;
    @FXML
    public Button backToMenuBtn;
    @FXML
    public Button deleteUserBtn;
    @FXML
    public Button addUserBtn;
    @FXML
    public Button viewBtn;

    private AccountingOS accountingOS;
    private List<User> users = new ArrayList<>();
    String selectedUser = new String();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void setAccountingOS(AccountingOS OS){
        this.accountingOS = OS;
        this.accountingOS.setUsersFromDB();
        fillUserList();
    }

    public void fillUserList(){
        userList.getItems().clear();
        users = new ArrayList<>();
        users.addAll(accountingOS.getUsers());
        for(User user: users){
            userList.getItems().add( user.getLoginName() + " (" + user.getUserString() + ")");
        }
    }

    public void addUser(ActionEvent actionEvent) throws IOException {

        if(!accountingOS.checkForAccess(3)){
            displayAccessAlertPopUp("Admin");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/AddUser.fxml"));
        Parent root = loader.load();

        AddUserControl control = loader.getController();
        control.setAccountingOS(accountingOS);

        Stage stage = new Stage();
        stage.setTitle("Add user");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        fillUserList();
    }

    public void deleteUser(ActionEvent actionEvent) throws Exception {

        if(!accountingOS.checkForAccess(3)){
            displayAccessAlertPopUp("Admin");
            return;
        }

       if(selectedUser.isEmpty())
           showPleaseSelectedUserAlert();
       else{
           String loggedInUserName = accountingOS.getLoggedInUser().getLoginName();
           if(!loggedInUserName.equals(selectedUser))
                accountingOS.deleteUser(selectedUser);
           else{
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Action invalid");
               alert.setHeaderText("Cannot delete currently logged in user");
               alert.showAndWait();
           }
       }
        fillUserList();
        selectedUser = "";
    }

    public void viewUserInfo(ActionEvent actionEvent) {
        if(!accountingOS.checkForAccess(3)){
            displayAccessAlertPopUp("Admin");
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

        User user = accountingOS.getUser(selectedUser);

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

    public void goBackToMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/MenuPage.fxml"));
        Parent root = loader.load();

        MenuPageControl controller = loader.getController();
        controller.setUp(accountingOS);

        Stage stage = (Stage) backToMenuBtn.getScene().getWindow();

        stage.setTitle("AccountingOS");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void clickedItem(MouseEvent mouseEvent) {
        String listItemText;
        if(userList.getSelectionModel().isEmpty()) // if no items selected return
            return;

        listItemText = userList.getSelectionModel().getSelectedItem().toString();
        String[] split = listItemText.split(" ");
        selectedUser = split[0];
        System.out.println(userList.getSelectionModel().getSelectedItem());
    }

    public void deselectItem(MouseEvent mouseEvent) {
        userList.getSelectionModel().clearSelection();
        selectedUser = "";
    }

    private void showPleaseSelectedUserAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action invalid");
        alert.setHeaderText("Please select user first");
        alert.showAndWait();
    }
    private void displayAccessAlertPopUp(String accessRequired) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access denied");
        alert.setHeaderText("Access required :" + accessRequired);
        alert.showAndWait();
    }
}
