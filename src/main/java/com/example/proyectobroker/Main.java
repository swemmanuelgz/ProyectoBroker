package com.example.proyectobroker;

import com.example.proyectobroker.controller.LoginController;
import com.example.proyectobroker.repository.CryptoRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Login");
        new LoginController().showEstaPantalla(stage);


    }

    public static void main(String[] args) {
        launch();
    }
}