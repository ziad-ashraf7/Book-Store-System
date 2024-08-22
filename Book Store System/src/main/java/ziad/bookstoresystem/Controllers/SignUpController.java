package ziad.bookstoresystem.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class SignUpController implements Initializable {
    DB db = new DB();

    @FXML
    private TextField emltf;

    @FXML
    private Text errT;

    @FXML
    private Text errmsg;

    @FXML
    private PasswordField pstf;

    @FXML
    private Button sgnbtn;

    @FXML
    private TextField usrtf;

    @FXML
    private Button bck;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sgnbtn.setOnMouseClicked(e->{
            if(emltf.getText().isEmpty()){
                errmsg.setText("");
                errmsg.setText("Please enter the Email");
            }else if(pstf.getText().isEmpty()){
                errmsg.setText("");
                errmsg.setText("Please enter the Password");

            }else if(usrtf.getText().isEmpty()){
                errmsg.setText("");
                errmsg.setText("Please enter the username");
            }
            else{
                if(isUserExist()){
                    errmsg.setText("");
                    System.out.println("This Email is already have account , Try to login");
                }else {
                    try {
                        db.addUser(usrtf.getText() , pstf.getText() , emltf.getText());
                        System.out.println("Added the user");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }


        });
        bck.setOnMouseClicked(e->{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLs/Login.fxml"));
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





    }

    private boolean isUserExist() {
        boolean here = db.searchForEmail(emltf.getText());
        return here;
    }


}
