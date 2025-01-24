package com.example.proyectobroker;

import com.example.proyectobroker.controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Iniciando aplicación");
        stage.setTitle("Login");
        new LoginController().showEstaPantalla(stage);


    }

    public static void main(String[] args) {
        launch();
    }
}