package ziad.bookstoresystem;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;



import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ziad.bookstoresystem.Data.DB;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
    private DB db = new DB();
    public static Parent root;
    public  static TextField showPasswordField = new TextField();

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLs/Login.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Books System");
        stage.setScene(scene);
        stage.show();
        db.getConnection();
        db.creatScheme();


    }
    public static void main(String[] args) {

        launch();
    }
}