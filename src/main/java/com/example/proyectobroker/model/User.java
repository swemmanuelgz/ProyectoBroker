package com.example.proyectobroker.model;


public class User {
    private int id;
    private String username;
    private String password;
    private UserConfig userConfig;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public User(String username, UserConfig userConfig, String password) {
        this.username = username;
        this.userConfig = userConfig;
        this.password = password;
    }

    public User() {
    }

    public User(int id, String username, String password, UserConfig userConfig) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userConfig = userConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }
    public void loadProfileImage(){
        String pathProfileImage = "/com/example/proyectobroker/img/profile/";
        String userProfileImage = pathProfileImage + this.username + ".png";
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userConfig=" + userConfig +
                '}';
    }
}
