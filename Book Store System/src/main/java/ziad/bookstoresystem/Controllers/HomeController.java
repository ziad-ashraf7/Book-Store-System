package ziad.bookstoresystem.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import ziad.bookstoresystem.App;
import ziad.bookstoresystem.Book;
import ziad.bookstoresystem.Data.DB;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeController implements Initializable {
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

    public static ArrayList<String> fav_books = new ArrayList<>();



    DB db = new DB();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        db.getConnection();
        bGrid.setHgap(50); // Horizontal gap between columns
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
            // Gettign the VolumInfo JSON Object
            JSONObject vInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
            JSONObject imgSec;

            // Getting the Image Section ( JSON Object )
            try{
                imgSec = vInfo.getJSONObject("imageLinks");
            }catch (Exception e){
                System.out.println("#########################################################################################################################");
                System.out.println("Faild to load book " + vInfo);
                System.out.println("#########################################################################################################################");
                continue;
            }

            String thum = imgSec.getString("thumbnail");
            System.out.println(thum);
            String title = vInfo.getString("title");
            String author = vInfo.getJSONArray("authors").getString(0);
            String publisher = vInfo.getString("publisher");
            String ISBN = items.getJSONObject(i).getString("id");
            String image = imgSec.getString("thumbnail");
            Book book = new Book(title, author, publisher, ISBN, image);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("FXMLs/BookCard.fxml"));
            AnchorPane anchorPane = loader.load();

            anchorPane.getStyleClass().add("container");
            BookCardController controller = loader.getController();
            controller.setData(book);
            GridPane.setMargin(anchorPane , new Insets(20));
            bGrid.add(anchorPane, col , row++);

        }


    }

    private Request sendReq() throws IOException {
        String url = "https://www.googleapis.com/books/v1/volumes?q=java&key=AIzaSyBCS5bBrh1bxYF73R7kWIeGpgj0xxZtxj8";
        return new Request.Builder()
                .url(url)
                .build();
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







}
