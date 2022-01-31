package edu.neu.madcourse.MyMusicPlayer;


import com.google.firebase.database.Exclude;

/**
 * User class
 * include fields user info in Firebase Database: userName, email, password, key
 * include getters, setters
 */
public class User {

    @Exclude
    private String key;
    private String userName;
    private String email;
    private String password;

    /**
     * default constructor used to read the whole User object from Firebase Database
     */
    public User() {
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
