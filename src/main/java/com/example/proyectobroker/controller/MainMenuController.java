package com.example.proyectobroker.controller;

import com.example.proyectobroker.repository.CryptoRepository;
import com.example.proyectobroker.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;

public class MainMenuController {
    @FXML
    private Button btnLogout;

    private UserRepository userRepository = new UserRepository();
    private CryptoRepository cryptoRepository = new CryptoRepository();
    @FXML
    public void initialize() {
    btnLogout.setOnAction(event -> {

            getCrypto();

    });
    }
    public void getCrypto(){

        try {
            cryptoRepository.getCryptoList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
