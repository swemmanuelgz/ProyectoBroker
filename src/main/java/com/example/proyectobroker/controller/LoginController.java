package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.utils.Constantes;
import com.example.proyectobroker.utils.GradientAnimation;
import com.example.proyectobroker.utils.PantallaUtils;
import com.example.proyectobroker.view.AlertView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnLogin;
    @FXML
    private Label txtCreateAccount;
    @FXML
    private Pane loginPane;
    private Stage loadingStage;


    private User userLogged = new User();
    private UserController userController = new UserController();

    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> {
            login();
        });
        createAccount();
        // GradientAnimation.animateBackground(loginPane);

    }

    public LoginController() {
    }

    public LoginController showEstaPantalla(Stage stage) throws IOException {
        FXMLLoader fxmlloader = new PantallaUtils().showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), Constantes.PAGINA_INICIAL.getDescripcion(), 522, 335);
        //Obtenemos el controlador de la ventana
        LoginController loginController = fxmlloader.getController();
        return loginController;
    }

    public LoginController(PasswordField txtPassword, TextField txtUsername, Button btnLogin, Label txtCreateAccount, User userLogged, UserController userController) {
        this.txtPassword = txtPassword;
        this.txtUsername = txtUsername;
        this.btnLogin = btnLogin;
        this.txtCreateAccount = txtCreateAccount;
        this.userLogged = userLogged;
        this.userController = userController;
    }

    @FXML
    public void createAccount() {
        txtCreateAccount.setOnMouseClicked(e -> {
            System.out.println("Create account");

            abrirCrearCuenta();
        });
    }


    @FXML
    public void login() {
        AlertView alertView;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User(txtUsername.getText(), txtPassword.getText());
        user.setUsername(txtUsername.getText());
        user.setPassword(txtPassword.getText());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        //Hacemos el intento de login
        user= userController.login(user);
        if (user == null) {
            alertView = new AlertView("Error", "Error al logearse", "Error al logearse");
            alertView.mostrarAlerta();
            return;
        }
        if (user != null) {

            this.userLogged = user;
            setUserLogged(user);
            System.out.println("Usuario logeado "+userLogged.getUsername());



            cerrarVentana();


        }



    }


    public User getUserLogged() {
        return userLogged;
    }

    private void initUserLogged(User userLogged) {
        this.userLogged = userLogged;
    }

    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
    }

    @FXML
    private void cerrarVentana() {
        try {
            Stage stage = new PantallaUtils().cerrarEstaPantalla(btnLogin);

            //Mostramos el main
            MainMenuController mainMenuController = new MainMenuController().showEstaPantalla(stage);

            mainMenuController.setUserLogged(userLogged);

        } catch (Exception ex) {
            AlertView alertView = new AlertView("Error", "Error al cargar la ventana de main", "Error al cargar la ventana de creación de cuenta " + ex.getMessage());
            throw new RuntimeException("Error al cargar la ventana de creación de main" + ex.getMessage());
        }
    }

    public void abrirCrearCuenta() {
        try {
            Stage stage = new PantallaUtils().cerrarEstaPantalla(txtCreateAccount);
            //Mostramos la ventana de creación de cuenta
            CreateAccountController createAccountController = new CreateAccountController().showEstaPantalla(stage);

        } catch (Exception e) {
            AlertView alertView = new AlertView("Error", "Error al cargar la ventana de creación de cuenta", "Error al cargar la ventana de creación de cuenta " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void mostrarPantallaDeCarga(Runnable tarea) {
        try {
            // Cargar la pantalla de carga
            FXMLLoader loader = new PantallaUtils().showEstaPantalla(new Stage(), Constantes.PANTALLA_CARGANDO.getDescripcion(), Constantes.PANTALLA_CARGANDO.getDescripcion(), 300, 200);
            Pane loadingPane = loader.load();

            Scene loadingScene = new Scene(loadingPane);
            //le ponemos los css al loading
            String css = getClass().getResource("/styles/loading.css").toExternalForm();
            loadingScene.getStylesheets().add(css);

            // Crear una nueva ventana para la pantalla de carga
            loadingStage = new Stage();
            loadingStage.setScene(loadingScene);
            loadingStage.initStyle(StageStyle.UNDECORATED); // Sin bordes
            loadingStage.initModality(Modality.APPLICATION_MODAL); // Bloquea otras ventanas
            loadingStage.show();

            // Ejecutar la tarea en segundo plano
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    tarea.run(); // Ejecutar la tarea proporcionada
                    return null;
                }
            };

            // Manejar errores en la tarea
            task.setOnFailed(event -> {
                ocultarPantallaDeCarga();
                event.getSource().getException().printStackTrace();
            });

            // Inicia la tarea
            new Thread(task).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ocultarPantallaDeCarga() {
        if (loadingStage != null) {
            loadingStage.close();
        }
    }
}