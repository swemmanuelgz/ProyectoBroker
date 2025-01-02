package com.example.proyectobroker.model;

import javafx.scene.image.Image;

public class UserConfig {
    private User user;
    private String divisa;
    private Double saldo;
    private Image profileImage;

    public UserConfig(User user, String divisa, Double saldo, Image profileImage) {
        this.user = user;
        this.divisa = divisa;
        this.saldo = saldo;
        this.profileImage = profileImage;
    }

    public UserConfig() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "user= " + user.getUsername() +
                ", divisa='" + divisa + '\'' +
                ", saldo=" + saldo +
                ", profileImage=" + profileImage +
                '}';
    }
}
