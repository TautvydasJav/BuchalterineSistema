package system.controllers;

import system.enums.Role;
import system.model.AccountingOS;
import system.model.categories.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import static system.constants.URLConstants.*;

public class CategoriesControl{

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

    public static final String POP_UP_ALERT = "Select category first";

    public void setAccountingOS(AccountingOS accountingOS){
        this.accountingOS = accountingOS;
        accountingOS.setCategoriesFromDB();
        populateCategories();
    }

    private void populateCategories() {
        sectionTree.setRoot(new TreeItem<String>("Categories"));
        accountingOS.getTopCategories().forEach(cat -> addTreeItems(cat, sectionTree.getRoot()));
        sectionTree.setShowRoot(false);
    }

    private void addTreeItems(Category category, TreeItem parentItem) {
        TreeItem<String> sectionTreeItem = new TreeItem<>("ID: " + category.getId() + ", " + category.getName());
        parentItem.getChildren().add(sectionTreeItem);
        category.getSubCategories().forEach(cat -> addTreeItems(cat, sectionTreeItem));
    }

    public void showSelectedCatLabel() {

        selectedCatLabel.setText(selectedCat.takeThisCategoryInfo());
    }

    public void addCategory() throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
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

    public void editCategory() throws IOException {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(sectionTree.getSelectionModel().isEmpty()){
            displayAlertPopUp(POP_UP_ALERT);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_CAT_URL));
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

    public void deleteCategory() throws Exception {
        if(!accountingOS.checkForAccess(2)){
            displayAccessAlertPopUp(Role.MEDIUM);
            return;
        }
        if(sectionTree.getSelectionModel().isEmpty()){
            displayAlertPopUp(POP_UP_ALERT);
            return;
        }
        accountingOS.deleteCategory(selectedCatId);
        populateCategories();
        deselectItem();
    }

    public void openStatements() throws IOException {
        if(sectionTree.getSelectionModel().isEmpty()){
            displayAlertPopUp(POP_UP_ALERT);
            return;
        } // if no items selected return

        FXMLLoader loader = new FXMLLoader(getClass().getResource(STATEMENTS_URL));
        Parent root = loader.load();

        StatementsControl control = loader.getController();
        control.setUp(accountingOS,selectedCatId);

        Stage stage = (Stage) statementsBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        populateCategories();
    }

    public void goBackToMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU_PAGE_URL));
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

    public void getClickedTreeItem() {
        String treeItemText;
        if(sectionTree.getSelectionModel().isEmpty()) // if no items selected return
            return;

        treeItemText = sectionTree.getSelectionModel().getSelectedItem().toString();
        String[] split = treeItemText.split("ID: ");
        split = split[1].split(",");
        selectedCatId = split[0].trim();
        selectedCat = accountingOS.getCategory(selectedCatId);
        showSelectedCatLabel();
    }

    public void deselectItem() {
        selectedCat = null;
        sectionTree.getSelectionModel().clearSelection();
        selectedCatLabel.setText("");
    }

    private void displayAlertPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action invalid");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void displayAccessAlertPopUp(Role role) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access denied");
        alert.setHeaderText("Access required :" + role);
        alert.showAndWait();
    }
}
