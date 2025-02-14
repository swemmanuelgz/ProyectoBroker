package com.example.proyectobroker.controller;

import com.example.proyectobroker.Main;
import com.example.proyectobroker.exceptions.Exceptions;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.utils.Constantes;
import com.example.proyectobroker.utils.GradientAnimation;
import com.example.proyectobroker.utils.PantallaUtils;
import com.example.proyectobroker.view.AlertView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
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
    //javadoc
    /**
     * Metodo para inicializar la pantalla de crear cuenta
     */
    @FXML
    public void initialize() {

        btnCreateAccount.setOnAction(e -> {
            System.out.println("Username: " + txtUsername.getText());
            System.out.println("Password: " + txtPassword.getText());
            System.out.println("Password Check: " + txtPasswordCheck.getText());
            usuario = new User(txtUsername.getText(), txtPassword.getText());
            //Chequeamos que las contraseñas coincidan
            exceptions.checkPasswordMatch(txtPassword.getText(), txtPasswordCheck.getText());

            //ahora lo creamos con la base de datos
            if (userController.checkUserExistsBd(usuario)) {
                System.out.println("El usuario ya existe en la BD");
                AlertView alertView = new AlertView("Error", "El usuario ya existe", "El usuario ya existe");
                alertView.mostrarAlerta();
                return;
            } else {
                userController.createUserBD(usuario);
                System.out.println("Usuario creado");
                //creamos la configuracion del usuario , cogemos el usuario de la bd para tener su id
                User userBd = userController.getUserBD(usuario);
                System.out.println(Main.ANSI_GREEN +"Usuario de la bd: "+userBd.getUsername()+Main.ANSI_RESET);
                //creamos la configuracion del usuario por defecto
                userController.createUserConfigBd(userBd);

                AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito");
                alertView.mostrarAlerta();
            }

        });
        //para animar el fondo
        GradientAnimation.animateBackground(createPane);

        btnAtras.setOnAction(event -> {
            cerrarVentana();
        });

    }
    //javadoc
    /**
     * Metodo para abrir la pantalla de crear cuenta
     */

    //Metodo para mostrar la panatalla de creacion de cuenta
    public CreateAccountController showEstaPantalla(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new PantallaUtils().showEstaPantalla(stage, Constantes.PAGINA_CREAR_CUENTA.getDescripcion(), Constantes.TITULO_CREAR_CUENTA.getDescripcion(), 522, 335);
        //Obtenemos el controlador de la ventana
        CreateAccountController createAccountController = fxmlLoader.getController();
        return createAccountController;
    }

    //javadoc
    /**
     * Metodo para abrir la pantalla de crear cuenta
     */

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
    //javadoc
    /**
     * Metodo para crear una cuenta
     * @param user
     * @param pass
     * @param passCheck
     */
    @FXML
    public void CreateAccount(TextField user, TextField pass, TextField passCheck) {
        btnCreateAccount.setOnAction(e -> {

             usuario = new User(txtUsername.getText(),txtPassword.getText());
//            //ahora lo creamos con la base de datos
//            if (userController.checkUserExistsBd(usuario)) {
//                System.out.println("El usuario ya existe en la BD");
//                AlertView alertView = new AlertView("Error", "El usuario ya existe", "El usuario ya existe");
//                alertView.mostrarAlerta();
//                return;
//            } else {
//                userController.createUserBD(usuario);
//                System.out.println("Usuario creado");
//                AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito");
//                alertView.mostrarAlerta();
//                //creamos la configuracion del usuario , cogemos el usuario de la bd para tener su id
//                User userBd = userController.getUserBD(usuario);
//                System.out.println(Main.ANSI_GREEN +"Usuario de la bd: "+userBd.getUsername()+Main.ANSI_RESET);
//                //creamos la configuracion del usuario por defecto
//                userController.createUserConfigBd(userBd);
//            }
            //Confirmacion de la creacion de la cuenta
            int rerspuesta = new JOptionPane().showConfirmDialog(null, "¿Desea crear la cuenta?", "Confirmación", JOptionPane.YES_NO_OPTION);
            //Early return
            if (rerspuesta == JOptionPane.NO_OPTION) {
                return;
            }

            if (userController.checkUserExists(usuario)) {
                System.out.println("El usuario ya existe en el JSON");
                AlertView alertView = new AlertView("Error", "El usuario ya existe", "El usuario ya existe");
                return;
            } else {
                userController.createUser(usuario);
                userController.createUserBD(usuario);
                System.out.println("Usuario creado");
                AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito");
                alertView.mostrarAlerta();
            }


        });

    }



}
