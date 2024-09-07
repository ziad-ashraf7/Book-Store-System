package ziad.bookstoresystem.Controllers;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import ziad.bookstoresystem.Book;
import ziad.bookstoresystem.Data.DB;
import ziad.bookstoresystem.UserSingelton;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookCardController implements Initializable {

    DB db = new DB();
    APIManager api = new APIManager();

    @FXML
    private AnchorPane fav_cont;

    @FXML
    private ImageView fav_img;
    @FXML
    private Text aothName;

    @FXML
    private Text bName;

    @FXML
    private Text des;

    @FXML
    private AnchorPane cont;

    Book myBook;

    @FXML
    private ImageView img;

    public void setData(Book book) {
        myBook = book;
        bName.setText(book.getTitle());
        aothName.setText(book.getAuthor());
        Image image = new Image(book.getImage());
        img.setImage(image);
        if (UserSingelton.getInstance().getCurr_user().getFavorites().isEmpty()) {

            try {
                db.insertFacBooks();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (String favBook : UserSingelton.getInstance().getCurr_user().getFavorites()) {
            if (favBook.equals(myBook.getISBN())) {
                Image image1 = new Image(getClass().getResource("/ziad/Images/fav_black.png").toExternalForm());

                fav_img.setImage(image1);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bName.setFont(new Font(30));
        aothName.setFont(new Font(30));
        bName.setFill(Color.WHITE);
        aothName.setFill(Color.WHITE);
        System.out.println(UserSingelton.getInstance().getCurr_user().getName());

    }

    @FXML
    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        System.out.println(bName.getText());
    }


    public void add_fav(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        String sel_book = myBook.getISBN();
        // is The book is one of the books fav List
        System.out.println(sel_book);
        if (isFav(sel_book)) {
            System.out.println("This is a favaurite book");
            Image image1 = new Image(getClass().getResource("/ziad/Images/fav_white.png").toExternalForm());
            fav_img.setImage(image1);
            db.delFromFavLis(sel_book);
            UserSingelton.getInstance().getCurr_user().getFavorites().remove(sel_book);
            System.out.println("DELETED from FAV LIST");
        } else {
            System.out.println("This is NOT a favaurite book");
            db.addToFavList(sel_book);
            UserSingelton.getInstance().getCurr_user().getFavorites().add(sel_book);
            System.out.println("ADDED to FAV LIST");
            Image image1 = new Image(getClass().getResource("/ziad/Images/fav_black.png").toExternalForm());
            fav_img.setImage(image1);
        }
    }

    private boolean isFav(String id) throws SQLException {
        db.insertFacBooks();
        ArrayList<String> books = UserSingelton.getInstance().getCurr_user().getFavorites();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(id)) {
                return true;
            }
        }
        return false;
    }
    @FXML
    void prevBook(MouseEvent event) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        Stage stage = new Stage();
        String t = myBook.getPrevLink().replace("http" , "https");
        webEngine.load(myBook.getPrevLink().replace("http" , "https"));
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Loaded URL: " + webEngine.getLocation());
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(webView);
        Scene scene = new Scene(borderPane , 1200 , 800);
        stage.setTitle("JavaFX Web Browser");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        System.out.println(myBook.getPrevLink());
    }
}
