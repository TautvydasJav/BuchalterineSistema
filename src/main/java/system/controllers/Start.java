package system.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static system.constants.URLConstants.*;

public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_URL));
        Parent root = loader.load();

        LoginControl controller = loader.getController();
        controller.checkIfAccountingTableExists();

        primaryStage.setTitle("AccountingOS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
