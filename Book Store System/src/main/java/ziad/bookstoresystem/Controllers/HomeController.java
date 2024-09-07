package ziad.bookstoresystem.Controllers;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import ziad.bookstoresystem.App;
import ziad.bookstoresystem.Book;
import ziad.bookstoresystem.Data.DB;
import ziad.bookstoresystem.UserSingelton;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class HomeController implements Initializable {

    String gemAPI = "AIzaSyCnyy4bi62FeNhKMvGAtPtueL5YzQ67q3w";
    @FXML
    private HBox bar;
    @FXML
    private GridPane bGrid;

    @FXML
    private ScrollPane bScroll;

    @FXML
    private GridPane catGrid;

    @FXML
    private ScrollPane catScrl;

    @FXML
    private TextField srchBar;

    @FXML
    private ImageView srchIcone;

    @FXML
    private ImageView favImg;

    @FXML
    private ImageView catImg;
    OkHttpClient client = new OkHttpClient();

    APIManager api = new APIManager();


    @FXML
    private ImageView hmImg;

    @FXML
    private ImageView srchImg;

    @FXML
    private ImageView usrImg;

    @FXML
    private BorderPane brdPane;


    DB db = new DB();

    StackPane container = new StackPane();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db.getConnection();
//        try {
//            loadFav();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        bGrid.setHgap(50);
        bGrid.setVgap(50);

        try {
            fetchData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void fetchData() throws IOException {
        String url = "https://www.googleapis.com/books/v1/volumes?q=java&key=AIzaSyBCS5bBrh1bxYF73R7kWIeGpgj0xxZtxj8";
        int col = 1;
        int row = 1;
        JSONArray items = api.getData(url);
        for (int i = 0; i < items.length(); i++) {
            System.out.println(UserSingelton.getInstance().getCurr_user().getName());

            JSONObject vInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
            JSONObject imgSec;

            String thum =      "Unknown";
            String title =     "Unknown";
            String author =    "Unknown";
            String publisher = "Unknown";
            String image =     "Unknown";
            String prevlink=   "Unknown";
            String ISBN =      items.getJSONObject(i).getString("id");
            Book book = new Book(title, author, publisher, ISBN, image , prevlink);
            volData(vInfo, thum, title, author, publisher, ISBN, image, book);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("FXMLs/BookCard.fxml"));
            AnchorPane anchorPane = loader.load();
            anchorPane.getStyleClass().add("container");
            BookCardController controller = loader.getController();
            controller.setData(book);
            GridPane.setMargin(anchorPane, new Insets(20));
            bGrid.add(anchorPane, col, row++);

        }
    }


    private Image loadImageWithUserAgent(String imageUrl) {
        Image image = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set User-Agent to mimic a common web browser
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

            connection.connect();

            // Print response headers to see what the server is returning
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (String header : headers.keySet()) {
                System.out.println(header + ": " + headers.get(header));
            }

            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                image = new Image(inputStream);
                inputStream.close();
            } else {
//                System.out.println("Failed to load image. HTTP response code: " + connection.getResponseCode());
                Exception e = new Exception("Failed to load image. HTTP response code: " + connection.getResponseCode());
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }


    public void goFav(MouseEvent mouseEvent) throws SQLException, IOException {
        loadFav();

    }
    private void loadFav() throws IOException, SQLException {
        bGrid.setVisible(false);
        ArrayList<String> book_list = db.getFavList();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50);
        gridPane.setVgap(50);
        bScroll.setContent(gridPane);
        JSONArray items = new JSONArray();
        if (book_list != null) {
            for (String s : book_list) {
                items.put(api.srchID(s));
            }
        }
        loopOnItems(items , gridPane);
    }

    public void goHome(MouseEvent mouseEvent) {
        bGrid.setVisible(true);

        bScroll.setContent(bGrid);

    }

    public void gotTo(MouseEvent mouseEvent) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        Stage stage = new Stage();
        webEngine.load("https://chatgpt.com/c/a779e3ae-2763-4f72-84e8-23eff0bd8d68");
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
    }

    public void goProfile(MouseEvent mouseEvent) throws IOException {
        bGrid.setVisible(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("FXMLs/ProfilePage.fxml"));
        AnchorPane anchorPane = loader.load();
        bScroll.setContent(anchorPane);
    }

    public void searchOnBook(MouseEvent mouseEvent) throws IOException {

        String url = "https://www.googleapis.com/books/v1/volumes?q=";
        String q = srchBar.getText();
        String serch = url + q + ":keyes&key=" + APIManager.apiKey;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray items = api.getData(serch);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GridPane gridPane = (GridPane) bScroll.getContent();
                            loopOnItems(items , gridPane);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        thread.start();


    }

    private void loopOnItems(JSONArray items , GridPane grid) throws IOException {
        grid.getChildren().clear();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                int col = 1;
                final int[] row = {1};
                for (int i = 0; i < items.length(); i++) {
                    JSONObject vInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

                    String thum =      "Unknown";
                    String title =     "Unknown";
                    String author =    "Unknown";
                    String publisher = "Unknown";
                    String image =     "Unknown";
                    String prevlink=   "Unknown";
                    String ISBN =      items.getJSONObject(i).getString("id");
                    Book book = new Book(title, author, publisher, ISBN, image , prevlink);
                    volData(vInfo, thum, title, author, publisher, ISBN, image, book);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(App.class.getResource("FXMLs/BookCard.fxml"));
                    AnchorPane anchorPane = null;
                    try {
                        anchorPane = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AnchorPane finalAnchorPane = anchorPane;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            finalAnchorPane.getStyleClass().add("container");
                            BookCardController controller = loader.getController();
                            controller.setData(book);
                            GridPane.setMargin(finalAnchorPane, new Insets(20));
                            grid.add(finalAnchorPane, col, row[0]++);
                        }
                    });


                }


            }

        });
        thread.start();

    }

    private void volData(JSONObject vInfo, String thum, String title, String author, String publisher, String ISBN, String image, Book book) {
        JSONObject imgSec;
        try {
            imgSec = vInfo.getJSONObject("imageLinks");
        } catch (Exception e) {
            System.out.println("#########################################################################################################################");
            System.out.println("Faild to load book " + vInfo);
            System.out.println("#########################################################################################################################");
            return;
        }
        thum = imgSec.getString("thumbnail");
        System.out.println(thum);
        title = vInfo.getString("title");
        try{

            String prev = vInfo.getString("previewLink");
            book.setPrevLink(prev);
        }catch (Exception e){
            System.out.println("Faild to load prev link");
        }
        author = "Unknown";
        try {
            author = vInfo.getJSONArray("authors").getString(0);

        } catch (Exception e) {
            System.out.println("Faild to load the author ");
        }
         publisher = "Unknown";
        try {
            publisher = vInfo.getString("publisher");
        } catch (Exception e) {
            System.out.println("Faild to load the publisher ");
        }
         image = imgSec.getString("thumbnail");
         book.setAuthor(author);
         book.setPublisher(publisher);
         book.setISBN(ISBN);
         book.setImage(image);
         book.setTitle(title);
    }


}
