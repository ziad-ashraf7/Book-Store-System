package ziad.bookstoresystem.Data;
import org.json.JSONArray;
import ziad.bookstoresystem.Book;
import ziad.bookstoresystem.User;
import ziad.bookstoresystem.UserSingelton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
public class DB {
    // Why didn't you say this to me when i was ALIVE ?!
    public static Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                logger.info("Connected to database");
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }
    public void createTable() {
        getConnection();

        String query =  "create table if not exists users (id integer not null primary key autoincrement, name string , " +
                "password Text)";
        try
                (
                        // create a database connection
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                        Statement statement = connection.createStatement();
                ) {
            statement.executeUpdate(query);
            logger.info("Table created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    // Searching for a username and password.
    public User searchFor(String usrname, String pswrd) {
        getConnection();
        ResultSet rs = null;
        String query = "select * from users where username=? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1 , usrname);
            statement.setString(2 , pswrd);
            rs = statement.executeQuery();
            if(rs.next()){
                User user = new User(rs.getString("username") , rs.getString("password") , rs.getInt("id"));
//                System.out.println(rs.getString("username") + " " + rs.getString("password") + " " + rs.getString("id"));
                UserSingelton.getInstance().setCurr_user(user);

                closeConnection();
                return user;

            }else {
                closeConnection();
                return null;
            }

        } catch (SQLException e) {
            logger.info(e.toString());
            return null;
        }


    }
    public void closeConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
            logger.info("Connection closed");
        }
    }
    public void addUser(String name , String psw , String eml) throws SQLException {
        getConnection();
        String query = "insert into users ( username , password ,email) values (? , ? , ?);";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, psw);
            preparedStatement.setString(3, eml);
            preparedStatement.executeUpdate();
            closeConnection();

        }catch (SQLException e){
            logger.info(e.toString());
        }



    }
    public boolean searchForEmail(String eml){
        getConnection();
        ResultSet rs = null;
        String query = "select * from users where email=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1 , eml);
            rs = statement.executeQuery();
            if(rs.next()){
                closeConnection();
                return true;

            }else {
                closeConnection();
                return false;
            }

        } catch (SQLException e) {
            logger.info(e.toString());
            return false;
        }
    }


    public void creatScheme() throws SQLException {
        getConnection();
        String query = "create table if not exists users (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ," +
                "       username TEXT NOT NULL ," +
                "       password TEXT NOT NULL, email TEXT NOT NULL UNIQUE );";
        excute(query);
        closeConnection();
    }
    private void excute(String q){
        try
                (
                        // create a database connection
                        Statement statement = connection.createStatement();
                ) {
            statement.executeUpdate(q);
            logger.info("query excuted");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }
    public void addToFavList(String id) throws SQLException {
        getConnection();
        int user_id = UserSingelton.getInstance().getCurr_user().getId();
        String q = "insert into fav_books (user_id, book_id) values (? , ?)";
        try (PreparedStatement statement = connection.prepareStatement(q)) {
            statement.setInt(1, user_id);
            statement.setString(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // For better debugging
        } finally {
            closeConnection();
        }
    }
    public void delFromFavLis(String id) throws SQLException {
        getConnection();
        int user_id = UserSingelton.getInstance().getCurr_user().getId();
        String q = "DELETE from fav_books where user_id = (select id from users where id = ?) AND book_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(q)) {
            statement.setInt(1, user_id);
            statement.setString(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    public ArrayList<String> getFavList() throws SQLException {
        getConnection();
        int user_id = UserSingelton.getInstance().getCurr_user().getId();
        ResultSet rs = null;
        String q = "select book_id from fav_books where user_id = ?;";
        ArrayList<String> list = null;
        try (PreparedStatement statement = connection.prepareStatement(q)) {
            statement.setInt(1, user_id);
            rs = statement.executeQuery();
            list = new ArrayList<>();
            while(rs.next()){
                list.add(rs.getString("book_id"));
//                System.out.println(rs.getString("book_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;

    }

    public void insertFacBooks() throws SQLException {
        UserSingelton.getInstance().getCurr_user().getFavorites().clear();
        // getting the userID
        int id = UserSingelton.getInstance().getCurr_user().getId();
        // Getting the users Fav books from the DB
        getConnection();
        String q = "select book_id from fav_books where user_id =?";
        try (PreparedStatement statement = connection.prepareStatement(q)) {
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
            closeConnection();
        }
        // Adding the books in the Array
    }
}
