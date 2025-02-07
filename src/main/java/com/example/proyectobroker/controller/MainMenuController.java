package com.example.proyectobroker.controller;

import com.example.proyectobroker.exceptions.Exceptions;
import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.repository.UserRepository;
import com.example.proyectobroker.utils.Constantes;
import com.example.proyectobroker.utils.GradientAnimation;
import com.example.proyectobroker.utils.PantallaUtils;
import com.example.proyectobroker.utils.TabTransition;
import com.example.proyectobroker.view.AlertView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private ImageView imgCryptoActivo;
    @FXML
    private Button btnVender;
    @FXML
    private Label lblCryptoActivo;
    //Invertir
    @FXML
    private ListView listCryptos ;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private LineChart<String,Number> stonksChart;
    @FXML
    private Label txtCryptoName;
    @FXML
    private Label txtCryptoPrice;

    @FXML
    private ImageView imgCryptoImage;

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
    private TextField txtTipoTransaccion;
    @FXML
    private PieChart chartCryptoWallet;
    //Pane
    @FXML
    private TabPane mainPane;
    //los tabs
    @FXML
    private AnchorPane paneInvertir;
    @FXML
    private AnchorPane paneActivos;
    @FXML
    private AnchorPane paneConfiguracion;
    @FXML
    private AnchorPane paneHistorial;

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

        //animamos el fondo
        GradientAnimation.animateBackground(mainPane);
        //eventos de transicion
        initTransitionTab();

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
    //Listener para las ventas
    btnVender.setOnAction(event -> {
        Inversion inversion = (Inversion) listInversionesActivos.getSelectionModel().getSelectedItem();
        venderCrypto(inversion);
    });
    listHistorial.setOnMouseClicked(event -> {
        Inversion inversion = (Inversion) listHistorial.getSelectionModel().getSelectedItem();
        txtAccionHistorial.setText(inversion.getCrypto().getName() );
        txtidTransaccion.setText(inversion.getTransaccion());
        txtImporteHistorial.setText(inversion.getImporteInversion().toString());
        txtCantidadHistorial.setText(inversion.getCantidadCrypto().toString());
        txtTipoTransaccion.setText(inversion.getTipo());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtFechaHistorial.setText(formatter.format(inversion.getFechaInversion()));
        System.out.println("Cantidad de cripto "+inversion.getCantidadCrypto());
    });
    listInversionesActivos.setOnMouseClicked(event -> {
        Inversion inversion = (Inversion) listInversionesActivos.getSelectionModel().getSelectedItem();
        txtActivos.setText(getInversionStat(inversion).toString());
        lblCryptoActivo.setText(inversion.getCrypto().getName());
        initStackedAreaChart(inversion.getCrypto());
    });

    }


        //metodo para coger la crypto
    /**
     * Metodo para obtener las criptomonedas
     * @return
     */
    private ArrayList<Crypto> getCrypto(){

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
    /**
     * Metodo para cerrar la sesión
     */
    private void logout(){

        try {
            //Obtenemos la escena actual
            Stage stage = new PantallaUtils().cerrarEstaPantalla(btnLogout);
            //Cerramos la sesión
            userLogged = null;
            //Mostramos la pantalla de login
            LoginController loginController = new LoginController().showEstaPantalla(stage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Metodo para inicializar el chart
     * @param crypto
     */
    private void initChart(Crypto crypto){
        System.out.println("Generando chart para invertir");
        XYChart.Series<String, Number> series = new XYChart.Series();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        LocalTime horaEmpieza = LocalTime.of(0,0);

        // Variables para el precio máximo y mínimo
        double maxPrice = Double.MIN_VALUE;
        double minPrice = Double.MAX_VALUE;

        // Iterar sobre los datos del sparkline
        Double[] sparkline = crypto.getSparkline();
        for (int i = 0; i < sparkline.length; i++) {
            double price = crypto.getSparkline()[i];

            if (price <= 0.01){
                //verificamos que no es el primer index
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
            //ponemos la hora en formato HH
            String hora = horaEmpieza.plusHours(i).format(formatter);

            // Añadir el dato a la serie
            series.getData().add(new XYChart.Data<>(hora, price));
        }
        // Configurar el eje Y
        y.setAutoRanging(false);
        double margin = (maxPrice - minPrice) * 0.1;
        y.setLowerBound(minPrice - margin);
        y.setUpperBound(maxPrice + margin);
        y.setTickUnit((maxPrice - minPrice) / 10);

        x.setAutoRanging(true);
        x.setLabel("Hora");

        stonksChart.getData().clear();
        stonksChart.getData().add(series);
    }
    /**
     * Metodo para inicializar la lista de cryptos
     */
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
    /**
     * Metodo para inicializar la lista de inversiones activas
     */
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
    /**
     * Metodo para inicializar la configuración del usuario
     */
    private void initConfig(){
        //Cargamos la configuración del usuario
        System.out.println("Usuario que recibimos "+userLogged.getUsername());
        userLogged.setUserConfig(userController.getUserConfigBD(userLogged));
        System.out.println("Usuario logeado "+userLogged.getUsername());
        System.out.println("Configuración del usuario "+userLogged.getUserConfig().toString());
        cmbDivisa.getItems().addAll("USD","EUR");
        txtUser.setText(userLogged.getUsername());
        txtMoney.setText(String.valueOf(userLogged.getUserConfig().getSaldo()));
        cmbDivisa.setValue(userLogged.getUserConfig().getDivisa());
        imgProfile.setImage(userLogged.getUserConfig().getProfileImage());


    }
    /**
     * Metodo para inicializar el chart de la wallet
     */
    private void initChartWallet(){

        if (inversiones.isEmpty() || inversiones == null){
            System.out.println("No hay inversiones");
            return;
        }
        //Hacemos un hashmap para guardar las inversiones y su importe total
        Map<String, Double> inversionesMap = new HashMap<>();
        for (Inversion inversion : inversiones){
            if (!inversion.getTipo().equals("venta") && !inversion.getVendida()){
                String nombreCrypto = inversion.getCrypto().getName();
                Double importe = inversion.getImporteInversion();
                inversionesMap.put(nombreCrypto,inversionesMap.getOrDefault(nombreCrypto,0.0)+importe);
            }
        }

        ArrayList<PieChart.Data> pieChartData = new ArrayList<>();
        //Recorremos el hashmap y lo añadimos al piechart
        for (Map.Entry<String, Double> entry : inversionesMap.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        chartCryptoWallet.setData(FXCollections.observableArrayList(pieChartData));

    }
    //para el stacked area chart
    /**
     * Metodo para inicializar el stacked area chart
     * @param crypto
     */
    private void initStackedAreaChart(Crypto crypto){
        System.out.println("\nGenerando stacked area chart");
        crypto.downloadIcon();
        imgCryptoActivo.setImage(crypto.getIcon());
        //inicializamos un Grafico
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
    /**
     * Metodo para inicializar el historial
     */
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
    //metodo para settear el usuario como logueado
    /**
     * Metodo para settear el usuario logueado
     * @param userLogged
     */
    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
        initConfig();
        initHistorial();
        initChartWallet();
        initList();
        initListActivos();
        //para inicializar los graficos una vez que ya tenemos los datos
        txtActivos.setText("Seleccione activo para ver detalles");
        initChart((Crypto) listCryptos.getItems().get(0));
        initStackedAreaChart(listHistorial.getItems().isEmpty() ? null : ((Inversion) listHistorial.getItems().get(0)).getCrypto());

    }
    //metodo para guardar los cambios
    /**
     * Metodo para guardar los cambios
     */
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

                //le ponemos el último nombre de usuario para que pueda encontrarlo en el json
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
    //metodo para cambiar la imagen
    /**
     * Metodo para cambiar la imagen
     */
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
    //metodo para obtener la inversion
    /**
     * Metodo para obtener la inversion
     * @param inversion
     * @return
     */
    public StringBuilder getInversionStat(Inversion inversion){
        StringBuilder stringBuilder = new StringBuilder();
        String ganancia = String.valueOf(inversion.getGanancia());
        if (!ganancia.contains("-")){
            ganancia = "+"+ganancia;
        }
        stringBuilder.append("Transacción: "+inversion.getTransaccion()+"\n");
        stringBuilder.append("Fecha: "+inversion.getFechaInversion()+"\n");
        stringBuilder.append("Importe: "+inversion.getImporteInversion()+"\n");
        stringBuilder.append(userLogged.getUserConfig().getDivisa()+" "+inversion.getPrecioCompraCrypto()+"\n");
        stringBuilder.append("Ganancia:  "+ganancia+"\n");
        return stringBuilder;
    }

    /**
     * Metodo para comprar criptomonedas
     */
    public void compraCrypto(){


        Date fechaActual = new Date( );

        Inversion inversion = new Inversion(userLogged.getUserConfig().getDivisa(),
                "compra",
                Double.valueOf(txtImporte.getText()),
                Double.valueOf(cryptoSelected.getPrice()),
                fechaActual,
                cryptoSelected,
                userLogged);
        //vendida false como que no se ha vendido
        inversion.setVendida(false);
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
        initListActivos();
        initChartWallet();
    }
    //metodo para vender criptomonedas
    /**
     * Metodo para vender criptomonedas
     * @param inversionVenta
     */
    public void venderCrypto (Inversion inversionVenta){
        Inversion inversionInsertar = new Inversion();
        //Creamos una inversion parecida a la que nos viene pero cambiando sus parámetros
        inversionInsertar.setUser(userLogged);
        inversionInsertar.setFechaInversion(new Date());
        inversionInsertar.setCrypto(inversionVenta.getCrypto());
        inversionInsertar.setPrecioCompraCrypto(inversionVenta.getPrecioCompraCrypto());
        inversionInsertar.setImporteInversion(inversionVenta.getGanancia());
        inversionInsertar.setTipo("venta"); //venta
        inversionInsertar.setDivisa(userLogged.getUserConfig().getDivisa());
        inversionInsertar.setVendida(true);
        //Guardamos la inversion
        inversionController.saveInversion(inversionInsertar);
        //actualizamos la inversion a vendida para que no aparezca en el menu de activos
        inversionController.updateInversion(inversionVenta);
        if (inversionVenta.getGanancia() > 0){
            //Si la ganancia es positiva la sumamos al saldo
            userLogged.getUserConfig().setSaldo(userLogged.getUserConfig().getSaldo()+inversionVenta.getGanancia());
            alertView = new AlertView("Información","Venta realizada con ganancias ","Venta realizada con ganancia de "+inversionVenta.getGanancia());
            alertView.mostrarAlerta();
        }else{
            //Si la ganancia es negativa la restamos al saldo
            userLogged.getUserConfig().setSaldo(userLogged.getUserConfig().getSaldo()-inversionVenta.getGanancia());
            alertView = new AlertView("Información","Venta realizada con pérdidas ","Venta realizada con pérdida de "+inversionVenta.getGanancia());
            alertView.mostrarAlerta();
        }
        //actualizamos los activos del usuario
        initChartWallet();
        initChart((Crypto) listCryptos.getItems().get(0));
        listInversionesActivos.getItems().remove(inversionVenta);
        //actualizamos el saldo del usuario
        userController.updateUserConfig(userLogged);
        txtMoney.setText(String.valueOf(userLogged.getUserConfig().getSaldo()));


    }
    //inizializar listeners para las transiciones de cambio de tab
    /**
     * Metodo para inicializar las transiciones de cambio de tab
     */
    public void initTransitionTab(){
        // Configura un listener para cambiar de tab
        mainPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (oldTab != null && newTab != null) {
                // Obtén los panes asociados a los tabs
                AnchorPane oldPane = (AnchorPane) oldTab.getContent();
                AnchorPane newPane = (AnchorPane) newTab.getContent();

                // Asegurarse de que el nuevo pane es visible
                newPane.setVisible(true);

                // Ejecutar la transición
                TabTransition.switchTabsWithTransition(mainPane, oldPane, newPane);
            }
        });
        // Configuración inicial para mostrar el primer tab
        initializeTabVisibility();
    }
    // Metodo para inicializar la visibilidad de los panes
    /**
     * Metodo para inicializar la visibilidad de los panes
     */
    private void initializeTabVisibility() {
        paneInvertir.setVisible(false);
        paneActivos.setVisible(false);
        paneConfiguracion.setVisible(false);
        paneHistorial.setVisible(false);

        // Mostrar solo el pane correspondiente al primer tab seleccionado
        Tab selectedTab = mainPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            AnchorPane firstPane = (AnchorPane) selectedTab.getContent();
            firstPane.setVisible(true);
        }
    }

    public MainMenuController showEstaPantalla(Stage stage)throws Exception{
        FXMLLoader fxmlLoader = new PantallaUtils().showEstaPantalla(stage, Constantes.PAGINA_SEGUNDA_PANTALLA.getDescripcion(),Constantes.TITULO_SEGUNDA_PANTALLA.getDescripcion(),963,622);
        MainMenuController controller = fxmlLoader.getController();
        return controller;
    }
}
