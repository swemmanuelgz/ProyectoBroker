package com.example.proyectobroker;

import com.example.proyectobroker.repository.CryptoRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CryptoRepository cryptoRepository = new CryptoRepository();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 522, 335);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        cryptoRepository.getCoins();


    }

    public static void main(String[] args) {
        launch();
    }
}