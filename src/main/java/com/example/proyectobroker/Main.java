package com.example.proyectobroker;

import com.example.proyectobroker.controller.LoginController;
import database.ConnectMysql;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    //colores ansi para la consola
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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