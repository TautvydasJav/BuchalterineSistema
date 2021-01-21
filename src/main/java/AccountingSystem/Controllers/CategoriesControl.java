package AccountingSystem.Controllers;

import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Categories.Category;
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
import java.util.ResourceBundle;

public class CategoriesControl implements Initializable {

    @FXML
    public Button editCategoryBtn;
    @FXML
    public Button addCatBtn;
    @FXML
    public Button deleteCat;
    @FXML
    public Button backToMenuBtn;
    @FXML
    public TreeView sectionTree;
    @FXML
    public TextField categoryName;
    @FXML
    public TextField categoryDescription;
    @FXML
    public Label selectedCatLabel;
    @FXML
    public Button statementsBtn;


    private AccountingOS accountingOS;
    private Category selectedCat;
    private String selectedCatId;

    public void setAccountingOS(AccountingOS OS){
        this.accountingOS = OS;
        accountingOS.setCategoriesFromDB();
        populateCategories();
    }

    private void populateCategories() {
        sectionTree.setRoot(new TreeItem<String>("Categories"));
        accountingOS.getTopCategories().forEach(cat -> addTreeItems(cat, sectionTree.getRoot()));
        sectionTree.setShowRoot(false);
    }

    private void addTreeItems(Category category, TreeItem parentItem) {
        TreeItem<String> sectionTreeItem = new TreeItem<String>("ID: " + category.getId() + ", " + category.getName());
        parentItem.getChildren().add(sectionTreeItem);
        category.getSubCategories().forEach(cat -> addTreeItems(cat, sectionTreeItem));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showSelectedCatLabel() {

        selectedCatLabel.setText(selectedCat.takeThisCategoryInfo());
    }

    public void addCategory(ActionEvent actionEvent) throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(sectionTree.getSelectionModel().isEmpty()){
            if(!checkIfFieldsEmpty()){
                accountingOS.addTopCategory(categoryName.getText(), categoryDescription.getText());
            }
            else{
                displayAlertPopUp("Enter text fields first");
                return;
            }
        }
        else{
            if(!checkIfFieldsEmpty()){
                accountingOS.addSubCategory(selectedCatId, categoryName.getText(), categoryDescription.getText());
            }
            else
                return;
        }

        categoryName.setText("");
        categoryDescription.setText("");
        populateCategories();
        deselectItem();
    }

    public void editCategory(ActionEvent actionEvent) throws IOException {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(sectionTree.getSelectionModel().isEmpty()){
            displayAlertPopUp("Select category first");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/EditCategory.fxml"));
        Parent root = loader.load();

        EditCategoryControl control = loader.getController();
        control.setAccountingOS(accountingOS);
        control.setId(selectedCatId);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        populateCategories();
        deselectItem();
    }

    public void deleteCategory(ActionEvent actionEvent) throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp("Medium");
            return;
        }
        if(sectionTree.getSelectionModel().isEmpty()){
            displayAlertPopUp("Select category first");
            return;
        }
        accountingOS.deleteCategory(selectedCatId);
        populateCategories();
        deselectItem();
    }

    public void openStatements(ActionEvent actionEvent) throws IOException {
        if(sectionTree.getSelectionModel().isEmpty()){
            displayAlertPopUp("Select category first");
            return;
        } // if no items selected return

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/StatementsPage.fxml"));
        Parent root = loader.load();

        StatementsControl control = loader.getController();
        control.setUp(accountingOS,selectedCatId);

        Stage stage = (Stage) statementsBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        populateCategories();
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

    public boolean checkIfFieldsEmpty() {
        if(categoryName.getText().isEmpty())
            return true;
        if(categoryDescription.getText().isEmpty())
            return true;
        return false;
    }

    public void getClickedTreeItem(MouseEvent mouseEvent) {
        String TreeItemText;
        if(sectionTree.getSelectionModel().isEmpty()) // if no items selected return
            return;

        TreeItemText = sectionTree.getSelectionModel().getSelectedItem().toString();
        String[] split = TreeItemText.split("ID: ");
        split = split[1].split(",");
        selectedCatId = split[0].trim();
        selectedCat = accountingOS.getCategory(selectedCatId);
        showSelectedCatLabel();
        System.out.println("Selected id: " + selectedCatId);
    }

    public void deselectItem() {
        selectedCat = null;
        sectionTree.getSelectionModel().clearSelection();
        selectedCatLabel.setText("");
    }

    private void displayAlertPopUp(String Message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action invalid");
        alert.setHeaderText(Message);
        alert.showAndWait();
    }

    private void displayAccessAlertPopUp(String accessRequired) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access denied");
        alert.setHeaderText("Access required :" + accessRequired);
        alert.showAndWait();
    }
}
