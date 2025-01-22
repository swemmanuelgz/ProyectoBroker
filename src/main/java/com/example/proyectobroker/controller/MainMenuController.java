package com.example.proyectobroker.controller;

import com.example.proyectobroker.exceptions.Exceptions;
import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.view.AlertView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {
    private AlertView alertView = new AlertView();
    private final Logger logger = Logger.getLogger(MainMenuController.class.getName());
    //Activos
    @FXML
    private Button btnLogout;
    @FXML
    private StackedAreaChart<String,Number> stonkActivos;
    @FXML
    private CategoryAxis timeAxis;
    @FXML
    private NumberAxis moneyAxis;
    @FXML
    private ListView listInversionesActivos;
    @FXML
    private TextArea txtActivos;
    @FXML
    private ComboBox cmbVender;
    @FXML
    private Button btnVender;
    //Invertir
    @FXML
    private ListView listCryptos ;
    @FXML
    private NumberAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private LineChart<Number,Number> stonksChart;
    @FXML
    private Label txtCryptoName;
    @FXML
    private Label txtCryptoPrice;

    @FXML
    private ImageView imgCryptoImage;
    @FXML
    private ComboBox cmbImporte;
    @FXML
    private TextField txtImporte;
    @FXML
    private Button btnPagar;
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
    //Historial
    @FXML
    private TextField txtAccionHistorial;
    @FXML
    private TextField txtidTransaccion;
    @FXML
    private TextField txtImporteHistorial;
    @FXML
    private TextField txtCantidadHistorial;
    @FXML
    private TextField txtFechaHistorial;
    @FXML
    private ListView listHistorial;
    @FXML
    private PieChart chartCryptoWallet;
    private User userLogged = new User();


    private CryptoController cryptoController = new CryptoController();
    private LoginController loginController = new LoginController();
    private UserController userController = new UserController();
    private InversionController inversionController = new InversionController();
    private Crypto cryptoSelected = new Crypto();
    private ArrayList<Inversion> inversiones = new ArrayList<>();
    @FXML
    public void initialize() {

        //initList();
        //initChart((Crypto) listCryptos.getItems().get(0));

        listCryptos.setOnMouseClicked(event -> {
            Crypto crypto = (Crypto) listCryptos.getSelectionModel().getSelectedItem();
            this.cryptoSelected = crypto;
            crypto.downloadIcon();
            imgCryptoImage.setImage(crypto.getIcon());
            txtCryptoName.setText(crypto.getName());

            if (userLogged.getUserConfig().getDivisa().equals("EUR")){
                crypto = cryptoController.convertToEuros(crypto);
            }
            //Ponemos el precio redondeado y la divisa
            txtCryptoPrice.setText("Precio por acción: "+crypto.roundPrice()+" "+userLogged.getUserConfig().getDivisa());
            System.out.println("uuid de la criptomoneda "+crypto.getUuid());
            initChart(crypto);
        });

        listHistorial.setOnMouseClicked(event -> {
            Inversion inversion = (Inversion) listHistorial.getSelectionModel().getSelectedItem();
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
    btnPagar.setOnAction(event -> {
        if (txtImporte.getText().isEmpty()){
            alertView = new AlertView("Error","Introduce un importe","Introduce un importe");
            alertView.mostrarAlerta();
        }
        compraCrypto();
    });
    listHistorial.setOnMouseClicked(event -> {
        Inversion inversion = (Inversion) listHistorial.getSelectionModel().getSelectedItem();
        txtAccionHistorial.setText(inversion.getCrypto().getName() );
        txtidTransaccion.setText(inversion.getTransaccion());
        txtImporteHistorial.setText(inversion.getImporteInversion().toString());
        txtCantidadHistorial.setText(inversion.getCantidadCrypto().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtFechaHistorial.setText(formatter.format(inversion.getFechaInversion()));
        System.out.println("Cantidad de cripto "+inversion.getCantidadCrypto());
    });
    listInversionesActivos.setOnMouseClicked(event -> {
        Inversion inversion = (Inversion) listInversionesActivos.getSelectionModel().getSelectedItem();
        txtActivos.setText(inversion.getCrypto().getName());
        cmbVender.getItems().add(inversion.getCrypto().getName());
        initStackedAreaChart(inversion.getCrypto());
    });

    }



    private ArrayList getCrypto(){

        try {
           return cryptoController.getAllCrypto();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private ArrayList<Inversion> getHistorial(){

            try {
                //Historial
                return inversionController.getUserInversions(userLogged);
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
        System.out.println("Generando chart para invertir");
        XYChart.Series<Number, Number> series = new XYChart.Series();

        // Variables para el precio máximo y mínimo
        double maxPrice = Double.MIN_VALUE;
        double minPrice = Double.MAX_VALUE;

        // Iterar sobre los datos del sparkline
        Double[] sparkline = crypto.getSparkline();
        for (int i = 0; i < sparkline.length; i++) {
            double price = crypto.getSparkline()[i];

            if (price <= 0.01){
                //verificamos que no es el primer indice
                if (i != 0){
                    price = crypto.getSparkline()[i-1];
                }else{
                    //si es el primer indice ponemos el precio a 0.01
                    price = 0.01;
                }

            }

            // Actualizar el precio máximo y mínimo
            maxPrice = Math.max(maxPrice, price);
            minPrice = Math.min(minPrice, price);


            // Añadir el dato a la serie
            series.getData().add(new XYChart.Data<>(i, price));
        }
        // Configurar el eje Y
        y.setAutoRanging(false);
        double margin = (maxPrice - minPrice) * 0.1;
        y.setLowerBound(minPrice - margin);
        y.setUpperBound(maxPrice + margin);
        y.setTickUnit((maxPrice - minPrice) / 10);

        x.setAutoRanging(true);

        stonksChart.getData().clear();
        stonksChart.getData().add(series);
    }
    private void initList(){
        ArrayList<Crypto> cryptos = getCrypto();
        System.out.println("Iniciamos la lista de cryptos");
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
    //este metodo inicializa la lista de inversiones del usuario que tiene abiertas
    private void initListActivos(){
        ArrayList<Inversion> comprasCrypto = inversionController.getCompras(userLogged);
        //Añadimos las compras a la lista
        listInversionesActivos.setCellFactory(param -> new ListCell<Inversion>() {
            @Override
            protected void updateItem(Inversion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getCrypto().getName() == null) {
                    setText(null);
                } else {
                    setText(item.getCrypto().getName());
                }
            }
        });
        listInversionesActivos.getItems().addAll(comprasCrypto);
        System.out.println("Iniciamos la lista de inversiones activas");
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
    private void initChartWallet(){

        if (inversiones.isEmpty() || inversiones == null){
            System.out.println("No hay inversiones");
            return;
        }
        //Hacemos un hashmap para guardar las inversiones y su importe total
        Map<String, Double> inversionesMap = new HashMap<>();
        for (Inversion inversion : inversiones){
            String nombreCrypto = inversion.getCrypto().getName();
            Double importe = inversion.getImporteInversion();
            inversionesMap.put(nombreCrypto,inversionesMap.getOrDefault(nombreCrypto,0.0)+importe);
        }

        ArrayList<PieChart.Data> pieChartData = new ArrayList<>();
        //Recorremos el hashmap y lo añadimos al piechart
        for (Map.Entry<String, Double> entry : inversionesMap.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        chartCryptoWallet.setData(FXCollections.observableArrayList(pieChartData));
    }
    //para el stacked area chart
    private void initStackedAreaChart(Crypto crypto){
        System.out.println("\nGenerando stacked area chart");
          //TODO: arreglar sparkline por si sale 0
        XYChart.Series <String, Number> series = new XYChart.Series<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        LocalTime horaEmpieza = LocalTime.of(0,0);

        //variables para saber cual es el precio maximo y el minimo
        double maxPrice = Double.MIN_VALUE;
        double minPrice = Double.MAX_VALUE;
        for (int i = 0; i < crypto.getSparkline().length; i++) {

            double price = crypto.getSparkline()[i];

            if (price <= 0.01){
                //verificamos que no es el primer indice
                if (i != 0){
                    price = crypto.getSparkline()[i-1];
                }else{
                    //si es el primer indice ponemos el precio a 0.01
                    price = 0.01;
                }

            }
            if (price < minPrice){
                minPrice= price;
            }if (price > maxPrice){
                maxPrice = price;
            }
            //ponemos la hora en formato HH:mm
            String hora = horaEmpieza.plusHours(i).format(formatter);

            series.getData().add(new XYChart.Data<>(hora, price));
        }
        //ponemos el precio maximo y minimo en el eje Y
        moneyAxis.setAutoRanging(false); //para que no se autoajuste
        double margen = (maxPrice-minPrice)*0.1;
        moneyAxis.setLowerBound(minPrice-margen);
        moneyAxis.setUpperBound(maxPrice+margen);
        moneyAxis.setTickUnit((maxPrice-minPrice)/10);
        //ponemos el tiempo en el eje X
        timeAxis.setAutoRanging(true); //para que no se autoajuste
        timeAxis.setLabel("Hora");
        timeAxis.setTickLabelRotation(45);
        stonkActivos.getData().clear();
        stonkActivos.getData().add(series);

    }
    private void initHistorial(){
        System.out.println("Generando historial");
         inversiones = getHistorial();
        listHistorial.setCellFactory(param -> new ListCell<Inversion>() {
            @Override
            protected void updateItem(Inversion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getCrypto().getName() == null) {
                    setText(null);
                } else {
                    setText(item.getCrypto().getName()+" "+item.getFechaInversion());
                }
            }
        });
        listHistorial.getItems().addAll(inversiones);

    }

    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
        initConfig();
        initHistorial();
        initChartWallet();
        initList();
        initListActivos();

    }
    public void saveChanges(){
        Exceptions exceptions = new Exceptions();
        UserRepository userRepository = new UserRepository();
        exceptions.validateUserField(txtUser.getText());
        exceptions.checkPasswordMatch(txtPassword.getText(),txtPasswordConfirm.getText());
        exceptions.validateMoney(txtMoney.getText());
        //exceptions.validateImage(imgProfile.getImage());

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

    public void compraCrypto(){


        Date fechaActual = new Date( );

        Inversion inversion = new Inversion(userLogged.getUserConfig().getDivisa(),
                "compra",
                Double.valueOf(txtImporte.getText()),
                Double.valueOf(cryptoSelected.getPrice()),
                fechaActual,
                cryptoSelected,
                userLogged);
        System.out.println("Saldo antes de la compra : "+userLogged.getUserConfig().getSaldo());
        Double saldoRestante = userLogged.getUserConfig().getSaldo() - Double.valueOf(txtImporte.getText());
        userLogged.getUserConfig().setSaldo(saldoRestante);

        System.out.println("Saldo restante : "+saldoRestante);
        //Subimos la inversion a la base de datos
        inversionController.saveInversion(inversion);
        //Actualizamos el saldo del usuario
        userController.updateUserConfig(userLogged);
        //Actualizamos el historial
        initHistorial();

        AlertView alertView = new AlertView("Información","Compra realizada","Compra realizada");
        alertView.mostrarAlerta();
        initChartWallet();
    }


}
