package ziad.bookstoresystem.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import ziad.bookstoresystem.User;
import ziad.bookstoresystem.UserSingelton;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private Button picButton;

    @FXML
    private ImageView profPic;
    @FXML
    void choosePic(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        int userId=UserSingelton.getInstance().getCurr_user().getId();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            profPic.setImage(image);
        }
        String nFileName = userId +"."+ getFileExtension(file);
        String desFolder = "D:\\CV_Projects\\Book Store System\\Testing the system\\Book-Store-System\\Book Store System\\src\\main\\resources\\ziad\\Images\\UsersPics";
        File destinationFile = new File(desFolder, nFileName);

        try{

            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("file coppied successfully");
        }catch(IOException e){
            System.out.println("file did NOT coppied");
        }








    }
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int user_id = UserSingelton.getInstance().getCurr_user().getId();
        try{
            Image image = new Image(getClass().getResource("/ziad/Images/UsersPics/"+user_id+".png").toExternalForm() , false);
            profPic.setImage(image);
        }catch (Exception e){
            Image image = new Image(getClass().getResource("/ziad/Images/profile.png").toExternalForm(), false);
            profPic.setImage(image);
        }
    }
}
