package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class MainMenuController {
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
    private ImageView imgProfile;
    @FXML
    private ComboBox cmbDivisa;

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
        cmbDivisa.getItems().addAll("USD","EUR");


    }

}
