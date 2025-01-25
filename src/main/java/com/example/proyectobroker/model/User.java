package com.example.proyectobroker.model;


public class User {
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userConfig=" + userConfig +
                '}';
    }
}
