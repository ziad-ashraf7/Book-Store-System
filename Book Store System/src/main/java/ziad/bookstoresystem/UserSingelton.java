package ziad.bookstoresystem;

public class UserSingelton {
    private static UserSingelton INSTANCE;
    User curr_user;

    private UserSingelton() {}
    public static UserSingelton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserSingelton();
        }
        return INSTANCE;
    }

    public User getCurr_user() {
        return curr_user;
    }
    public void setCurr_user(User curr_user) {
        this.curr_user = curr_user;
    }
}
