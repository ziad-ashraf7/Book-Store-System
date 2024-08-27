package ziad.bookstoresystem.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ziad.bookstoresystem.App;
import ziad.bookstoresystem.Book;
import ziad.bookstoresystem.Data.DB;
import ziad.bookstoresystem.UserSingelton;

import java.awt.event.MouseEvent;
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
        bName.setText( book.getTitle());
        aothName.setText( book.getAuthor());
        Image image = new Image(book.getImage());
        img.setImage(image);
        if(UserSingelton.getInstance().getCurr_user().getFavorites().isEmpty()){
            try {
                insertFacBooks();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (String favBook : UserSingelton.getInstance().getCurr_user().getFavorites()) {
            if(favBook.equals(myBook.getISBN())){
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
//        cont.setStyle("-fx-background-color: #ff0000");
        cont.setStyle(
                "-fx-background-color: #7e0101;" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 10;"+  // Optional: Adds some padding
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 60, 0.5, 3, 10);"
        );
        System.out.println("This is from the init of the hoem");
        System.out.println(UserSingelton.getInstance().getCurr_user().getName());
    }


    @FXML
    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        System.out.println(bName.getText());
    }


    void insertFacBooks() throws SQLException {

        // getting the userID
        int id = UserSingelton.getInstance().getCurr_user().getId();
        // Getting the users Fav books from the DB
        db.getConnection();
        String q = "select book_id from fav_books where user_id =?";
        try (PreparedStatement statement = db.connection.prepareStatement(q)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String bookId = rs.getString("book_id");
                    System.out.println("Book ID: " + bookId);
                   UserSingelton.getInstance().getCurr_user().getFavorites().add(bookId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // For better debugging
        } finally {
            db.closeConnection();
        }
        // Adding the books in the Array
    }


    public void add_fav(javafx.scene.input.MouseEvent mouseEvent) {
    }
}
