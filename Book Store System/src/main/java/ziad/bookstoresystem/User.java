package ziad.bookstoresystem;

import java.util.ArrayList;

public class User {
    String name;
    String password;
    int id ;
    ArrayList<String> favorites;

    public User(String name, String password, int id) {
        this.name = name;
        this.password = password;
        this.id = id;
        favorites = new ArrayList<>();
    }
    public User() {}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }
}
