package ziad.bookstoresystem.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import ziad.bookstoresystem.App;
import ziad.bookstoresystem.Data.DB;
import ziad.bookstoresystem.User;
import ziad.bookstoresystem.UserSingelton;

import java.io.IOException;
import java.net.URL;
import java.rmi.UnknownHostException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static ziad.bookstoresystem.App.showPasswordField;

public class LoginController implements Initializable {
    private DB db = new DB();

//    public static User curr_user = new User();

    @FXML
    private Button lgnbtn;
    @FXML
    private AnchorPane ancRoot;


    @FXML
    private PasswordField pstf ;

    APIManager api = new APIManager();
    @FXML
    private ProgressBar progBar;
    @FXML
    private Button sgnbtn;
    @FXML
    private Text errmsg;

    @FXML
    private TextField usrtf;
    @FXML
    private Text errT;

    @FXML
    private CheckBox shwpss;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progBar.setVisible(false);

        showPasswordField.setLayoutX(pstf.getLayoutX());
        showPasswordField.setLayoutY(pstf.getLayoutY());

        showPasswordField.setMaxWidth(pstf.getMaxWidth());
        showPasswordField.setMaxHeight(pstf.getMaxHeight());

        showPasswordField.setManaged(false);
        showPasswordField.setVisible(false);

        // Sync the text between PasswordField and TextField
        showPasswordField.textProperty().bindBidirectional(pstf.textProperty());

//        showPasswordField.textProperty().bindBidirectional(psdtf.textProperty());
        shwpss.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                showPasswordField.setVisible(true);
                showPasswordField.setManaged(true);
                pstf.setVisible(false);
                pstf.setManaged(false);
            } else {
                showPasswordField.setVisible(false);
                showPasswordField.setManaged(false);
                pstf.setVisible(true);
                pstf.setManaged(true);
            }
        });

        sgnbtn.setOnMouseClicked(e->{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLs/SignUp.fxml"));
            Pane root = new Pane();
            try {
                root = fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(scene);

        });

        lgnbtn.setOnMouseClicked(e->{
            if(usrtf.getText().isEmpty()){
                errmsg.setText("");
                errmsg.setText("Please enter the username");
            }else if(pstf.getText().isEmpty()){
                errmsg.setText("");
                errmsg.setText("Please enter the Password");
            } else{
                try {
                    isUserExist();
                    if(UserSingelton.getInstance().getCurr_user() != null){
                        errmsg.setText("");
                        errT.setVisible(false);
                        System.out.println(UserSingelton.getInstance().getCurr_user().getId());
                        System.out.println(UserSingelton.getInstance().getCurr_user().getName());
                        System.out.println(UserSingelton.getInstance().getCurr_user().getPassword());
    //                    progBar.setVisible(true);
                        ProgressBar prg = new ProgressBar();
                        prg.setPrefWidth(250);
                        prg.setLayoutX(160);
                        prg.setLayoutY(270);
                        ancRoot.getChildren().add(prg);


                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLs/Home.fxml"));
                                Pane root = new Pane();
                                try {
                                    root = fxmlLoader.load();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                Pane finalRoot = root;
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("Thread is Running");
                                        Scene scene = new Scene(finalRoot);
                                        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();


                                        stage.setScene(scene);
                                        System.out.println("Wellcome "+usrtf.getText());
                                    }
                                });


                            }
                        });
                        thread.start();

                    }else {

                        errmsg.setText("");
                        errmsg.setText("No such a user , please try to sign up");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

    }
    private static JSONArray items ;
    public  static JSONArray getItems(){
        return items;
    }

    private User isUserExist() throws SQLException {
        return db.searchFor(usrtf.getText() , pstf.getText());
    }
}
