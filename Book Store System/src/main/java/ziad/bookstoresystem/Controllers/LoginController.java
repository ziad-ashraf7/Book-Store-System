package ziad.bookstoresystem.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ziad.bookstoresystem.App;
import ziad.bookstoresystem.Data.DB;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static ziad.bookstoresystem.App.showPasswordField;

public class LoginController implements Initializable {
    private DB db = new DB();

    @FXML
    private Button lgnbtn;


    @FXML
    private PasswordField pstf ;

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
//            String nm = usrtf.getText();
//            String ps = pstf.getText();
//            try {
//                db.addUser(nm , ps);
//                System.out.println("Added the user");
//            } catch (SQLException ex) {
//                System.out.println("Couldn't add the user");
//
//            }
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
                if(isUserExist()){
                    errmsg.setText("");

                    System.out.println("Wellcome "+usrtf.getText());

                }else {

                    errmsg.setText("");
                    errmsg.setText("No such a user , please try to sign up");
                }

            }
        });

    }

    private boolean isUserExist() {
        boolean here = db.searchFor(usrtf.getText() , pstf.getText());
        return here;
    }
}
