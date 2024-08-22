package ziad.bookstoresystem.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
public class DB {
    private static DB Instance = null;
    Connection connection;



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

    public boolean searchFor(String usrname, String pswrd) {
        getConnection();
        ResultSet rs = null;
        String query = "select * from users where username=? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1 , usrname);
            statement.setString(2 , pswrd);
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
    private void closeConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
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


    public void creatScheme() {
        String query = "create table if not exists users (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ," +
                "       username TEXT NOT NULL ," +
                "       password TEXT NOT NULL, email TEXT NOT NULL UNIQUE );";
        excute(query);













    }
    private void excute(String q){
        getConnection();
        try
                (
                        // create a database connection
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                        Statement statement = connection.createStatement();
                ) {
            statement.executeUpdate(q);
            logger.info("Table created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

}
