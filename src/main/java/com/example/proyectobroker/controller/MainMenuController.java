package com.example.proyectobroker.controller;

import com.example.proyectobroker.exceptions.Exceptions;
import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.view.AlertView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {
    private AlertView alertView = new AlertView();
    private final Logger logger = Logger.getLogger(MainMenuController.class.getName());
    //Activos
    @FXML
    private Button btnLogout;
    @FXML
    private ListView listCryptos ;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private LineChart<?,?> stonksChart;
    @FXML
    private Label txtCryptoName;
    @FXML
    private Label txtCryptoPrice;
    //Invertir
    @FXML
    private ImageView imgCryptoImage;
   //Configuracion
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPasswordConfirm;
    @FXML
    private TextField txtMoney;
    @FXML
    private Button btnSaveChanges;
    @FXML
    private Button btnChangeImage;
    @FXML
    private ImageView imgProfile;
    @FXML
    private ComboBox cmbDivisa;
    private User userLogged = new User();


    private CryptoController cryptoController = new CryptoController();
    private LoginController loginController = new LoginController();
    private UserController userController = new UserController();
    @FXML
    public void initialize() {

        initList();
        //initChart((Crypto) listCryptos.getItems().get(0));
        listCryptos.setOnMouseClicked(event -> {
            Crypto crypto = (Crypto) listCryptos.getSelectionModel().getSelectedItem();
            crypto.downloadIcon();
            imgCryptoImage.setImage(crypto.getIcon());
            txtCryptoName.setText(crypto.getName());
            txtCryptoPrice.setText("Precio por acción: "+crypto.getPrice());
            initChart(crypto);
        });
    btnLogout.setOnAction(event -> {
        logout();
    });
    btnSaveChanges.setOnAction(event -> {
        saveChanges();
    });
    btnChangeImage.setOnAction(event -> {
        changeImage();
    });
    }



    private ArrayList getCrypto(){

        try {
           return cryptoController.getAllCrypto();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void logout(){

        try {
            //Logout
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectobroker/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 522, 335);
            Stage stage = new Stage();
            stage.setTitle("Login");
            Stage stageActual = (Stage) btnLogout.getScene().getWindow();
            stageActual.close();
            stage.setScene(scene);
            stage.show();
            System.out.println("Logout del usuario "+loginController.getUserLogged().getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void initChart(Crypto crypto){
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < crypto.getSparkline().length; i++) {
            int contador = i;
            String cont = String.valueOf(contador);
            series.getData().add(new XYChart.Data(cont, crypto.getSparkline()[i]));
        }
        stonksChart.getData().clear();
        stonksChart.getData().add(series);
    }
    private void initList(){
        ArrayList<Crypto> cryptos = getCrypto();
        listCryptos.setCellFactory(param -> new ListCell<Crypto>() {
            @Override
            protected void updateItem(Crypto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {;
                    setText(item.getName());
                }
            }
        });
        listCryptos.getItems().addAll(cryptos);
    }
    //Configuracion
    private void initConfig(){
        //Cargamos la configuración del usuario
        System.out.println("Usuario que recibimos "+userLogged.getUsername());
        userLogged.setUserConfig(userController.getUserConfig(userLogged));
        System.out.println("Usuario logeado "+userLogged.getUsername());
        System.out.println("Configuración del usuario "+userLogged.getUserConfig().toString());
        cmbDivisa.getItems().addAll("USD","EUR");
        txtUser.setText(userLogged.getUsername());
        txtMoney.setText(String.valueOf(userLogged.getUserConfig().getSaldo()));
        cmbDivisa.setValue(userLogged.getUserConfig().getDivisa());
        imgProfile.setImage(userLogged.getUserConfig().getProfileImage());


    }

    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
        initConfig();

    }
    public void saveChanges(){
        Exceptions exceptions = new Exceptions();
        UserRepository userRepository = new UserRepository();
        exceptions.validateUserField(txtUser.getText());
        exceptions.checkPasswordMatch(txtPassword.getText(),txtPasswordConfirm.getText());
        exceptions.validateMoney(txtMoney.getText());
        exceptions.validateImage(imgProfile.getImage());

        logger.log(Level.INFO,"Validaciones pasadas");
        logger.log(Level.INFO,"Usuario logeado "+userLogged.getUsername());
        //CComprobar si existre el usuario y si el usuario es diferente al actual
        if (userLogged.getUsername().equals(txtUser.getText()) || !userController.checkUserExists(new User(txtUser.getText(),txtPassword.getText()))){
            //Crear usuario

            User user = new User(txtUser.getText(),txtPassword.getText());
            //Si el usuario deja la casilla de la contraseña vacia se deja la contraseña anterior
            if (txtPassword.getText().isEmpty()){
                user.setPassword(userLogged.getPassword());
            }
            //Si no se ha seleccionado imagen y la imagen es la default se lo dejamos como null
            /*if (imgProfile.getImage().getUrl().equals("file:/C:/Users/aleja/IdeaProjects/proyecto-broker/src/main/resources/com/example/proyectobroker/default.png")){
                imgProfile.setImage(null);

            }*/
            //Crear configuración
            user.setUserConfig(new UserConfig(user,cmbDivisa.getValue().toString(),Double.parseDouble(txtMoney.getText()),userLogged.getUserConfig().getProfileImage()));

                //le ponemos el ultimo nombre de usuario para que pueda encontrarlo en el json
                //por si el usuario cambia el nombre
                logger.log(Level.INFO,"Usuario diferente en nombres: "+userLogged.getUsername()+" "+txtUser.getText());
                user.getUserConfig().setLastname(userLogged.getUsername());

            //guardamos la configuracion del usuario
            userController.updateUserConfig(user);
            logger.log(Level.INFO,"Usuario actualizado");
            alertView = new AlertView("Información","Cambios guardados","Cambios guardados");
            alertView.mostrarAlerta();
        }

    }
    private void changeImage() {
        //Creamos un filechooser donde solo se puedan coger imagenes
        FileChooser fileChooser = new FileChooser();
        //Filtro de imagenes
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        //Abrimos el filechooser
        File file = fileChooser.showOpenDialog(null);
        //Comprobamos si se ha seleccionado una imagen
        if (file != null) {

            System.out.println(file.getAbsolutePath());
            //Cargamos la imagen
            imgProfile.setImage(new javafx.scene.image.Image(file.toURI().toString()));
            userLogged.getUserConfig().setProfileImage(imgProfile.getImage());

        }
    }
}
