package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.view.AlertView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private User userLogged = new User();
    private UserController userController = new UserController();
    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> {
            login();
        });
        createAccount();

    }
    public LoginController() {
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

            try {
                System.out.println(getClass().getResource("/com/example/proyectobroker/createAccount.fxml"));

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectobroker/createAccount.fxml"));
                fxmlLoader.setController(new CreateAccountController());
                Scene scene = new Scene(fxmlLoader.load(), 522, 335);
                Stage stage = new Stage();
                stage.setTitle("Create Account");


                //Cerramos el stage actual
                Stage stageActual = (Stage) txtCreateAccount.getScene().getWindow();
                stageActual.close();

                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                throw new RuntimeException("Error al cargar la ventana de creación de cuenta "+ ex.getMessage());
            }
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


           try {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectobroker/mainMenu.fxml"));
               Scene scene = new Scene(fxmlLoader.load(), 963, 622);
               Stage stage = new Stage();
               stage.setTitle("Main");
               //Cogemos el controlador
               System.out.println("Mandamos el usuario al main "+userLogged.getUsername());
               MainMenuController mainMenuController = fxmlLoader.getController();
               mainMenuController.setUserLogged(userLogged);


               //Cerramos el stage actual
               Stage stageActual = (Stage) btnLogin.getScene().getWindow();
               stageActual.close();
               stage.setScene(scene);
               stage.show();

           } catch (IOException ex) {
               alertView = new AlertView("Error", "Error al cargar la ventana de main", "Error al cargar la ventana de creación de cuenta "+ ex.getMessage());
               throw new RuntimeException("Error al cargar la ventana de creación de main"+ ex.getMessage());
           }
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
}