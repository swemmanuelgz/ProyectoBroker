package com.example.proyectobroker.controller;

import com.example.proyectobroker.exceptions.Exceptions;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.utils.Constantes;
import com.example.proyectobroker.utils.GradientAnimation;
import com.example.proyectobroker.utils.PantallaUtils;
import com.example.proyectobroker.view.AlertView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
    @FXML
    private Pane createPane;
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
        //para animar el fondo
        //GradientAnimation.animateBackground(createPane);

        btnAtras.setOnAction(event -> {
            cerrarVentana();
        });

    }


    //Metodo para mostrar la panatalla de creacion de cuenta
    public CreateAccountController showEstaPantalla(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new PantallaUtils().showEstaPantalla(stage, Constantes.PAGINA_CREAR_CUENTA.getDescripcion(), Constantes.TITULO_CREAR_CUENTA.getDescripcion(), 522, 335);
        //Obtenemos el controlador de la ventana
        CreateAccountController createAccountController = fxmlLoader.getController();
        return createAccountController;
    }

    //METODO PARA CERRAR VENTANA
    private void cerrarVentana() {
        try {
            System.out.println("Cerrando crear cuenta");
            Stage stage = new PantallaUtils().cerrarEstaPantalla(btnAtras);

            //mostrar el main
            LoginController loginController = new LoginController().showEstaPantalla(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void CreateAccount(TextField user, TextField pass, TextField passCheck) {
        btnCreateAccount.setOnAction(e -> {

             usuario = new User(txtUsername.getText(),txtPassword.getText());


            if (userController.checkUserExists(usuario)) {
                System.out.println("El usuario ya existe");
                AlertView alertView = new AlertView("Error", "El usuario ya existe", "El usuario ya existe");
                return;
            } else {
                userController.createUser(usuario);
                System.out.println("Usuario creado");
                AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito");
                alertView.mostrarAlerta();
            }

        });

    }



}
