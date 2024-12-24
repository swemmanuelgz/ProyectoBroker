package com.example.proyectobroker.controller;

import com.example.proyectobroker.exceptions.Exceptions;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.view.AlertView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {

    @FXML
    private Button btnAtras;
    @FXML
    private Button btnCreateAccount;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPasswordCheck;

    //Atributos
    private UserController userController = new UserController();
    private User usuario;
    private Exceptions exceptions = new Exceptions();

    @FXML
    public void initialize() {
        btnCreateAccount.setOnAction(e -> {
            System.out.println("Username: " + txtUsername.getText());
            System.out.println("Password: " + txtPassword.getText());
            System.out.println("Password Check: " + txtPasswordCheck.getText());
            usuario = new User(txtUsername.getText(), txtPassword.getText());
            //Chequeamos que las contraseñas coincidan
            exceptions.checkPasswordMatch(txtPassword.getText(), txtPasswordCheck.getText());

            if (userController.checkUserExists(usuario)) {
                System.out.println("El usuario ya existe");
                return;
            } else {
                userController.createUser(usuario);
                System.out.println("Usuario creado");
                AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito");
            }

        });
        Atras();
    }
    @FXML
    public void CreateAccount(TextField user, TextField pass, TextField passCheck) {
        btnCreateAccount.setOnAction(e -> {

             usuario = new User(txtUsername.getText(),txtPassword.getText());


            if (userController.checkUserExists(usuario)) {
                System.out.println("El usuario ya existe");
                return;
            } else {
                userController.createUser(usuario);
                System.out.println("Usuario creado");
                AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito");
                alertView.mostrarAlerta();
            }

        });

    }
    @FXML
    public void Atras() {
        btnAtras.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectobroker/login.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 522, 335);
                Stage stage = new Stage();
                stage.setTitle("Login");
                Stage stageActual = (Stage) btnAtras.getScene().getWindow();
                stageActual.close();

                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Atras");
        });
    }

}
