package com.example.proyectobroker.repository;

import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.view.AlertView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.ConnectMysql;
import javafx.scene.image.Image;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserRepository {
    private ArrayList<User> usersList = new ArrayList<>();
    private final String pathProfileImg = "/com/example/proyectobroker/img/profile/";
    private final String pathProfileImgSave = "src/main/resources/com/example/proyectobroker/img/profile/";
    private static final System.Logger logger = System.getLogger(UserRepository.class.getName());
    private final CryptoRepository cryptoRepository = new CryptoRepository();
    public UserRepository() {

    }
    /**
     * Método para comprobar si el usuario existe en la base de datos
     * @param user
     * @return
     */
    public  boolean checkUserExists(User user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJson = new File("src/main/java/database/data.json");
            logger.log(System.Logger.Level.INFO, "Ruta del archivo: "+archivoJson);
            System.out.println("UserRepository: "+archivoJson);

            //Comprobar si existe el archivo
            if (!archivoJson.exists()) {
                System.out.printf("El archivo no existe: ", archivoJson.getName());
                return false;
            }
            if (archivoJson.length() == 0) {
                System.out.printf("El archivo está vacío: ", archivoJson.getName());
                return false;
            }

            //Leemos el contenido del archivo
            JsonNode rootNode = objectMapper.readTree(archivoJson);
            if (rootNode.isArray()&& rootNode.size() == 0) {
                System.out.printf("El archivo está vacío: ", archivoJson.getName());
                return false;
            }

            //Buscamos al usuario en el JSON
            for (JsonNode node : rootNode) {
                String usuarioExistente = node.get("username").asText();
                if (usuarioExistente.equalsIgnoreCase(user.getUsername())) {
                    System.out.println("El usuario ya existe");
                    AlertView alertView = new AlertView("Usuario existente", "El usuario ya existe", "El usuario ya existe en la base de datos");
                    alertView.mostrarAlerta();
                    return true; //usuario existe
                }
            }

        } catch (Exception e) {
            AlertView alertView = new AlertView("Error", "Error al buscar usuario", e.getMessage());
            alertView.mostrarAlerta();
            throw new RuntimeException(e);
        }
        return false; //usuario no existe
    }
    public boolean checkuserExistsBd(User user){
        ConnectMysql connectMysql = new ConnectMysql();
        return connectMysql.checkUserExists(user);
    }
    /**
     *metodo para crear un usuario
     * @param userInsert
     * @return
     */
    public void createUser(User userInsert){

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //Encriptamos la contraseña del usuario
            userInsert.setPassword(bCryptPasswordEncoder.encode(userInsert.getPassword()));
        try {
            //Inicializamos el maper
            ObjectMapper objectMapper = new ObjectMapper();
            //Guardar el archivo en la ruta resources
            File archivoJson = new File("src/main/java/database/data.json");
            ArrayNode userArray;
            if (archivoJson.exists() && archivoJson.length() > 0) {
                ;
                userArray = (ArrayNode) objectMapper.readTree(archivoJson);
            } else {
                userArray = objectMapper.createArrayNode();
            }


            //Creamos nuevo objecto de usuario
            ObjectNode newUser = objectMapper.createObjectNode();
            newUser.put("username", userInsert.getUsername());
            newUser.put("password", userInsert.getPassword());

            userArray.add(newUser);

            //Comprobar si existe el archivo



            //Imprimimos la ruta del archivo
            System.out.println(archivoJson);
            logger.log(System.Logger.Level.INFO, "Ruta del archivo: "+archivoJson);

            //Escribimos el nuevo array en el archivo
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(archivoJson, userArray);
            //imprimimos el nuevo json en consola
            System.out.println(userArray);
            //Noficamos que se creo el usuario
            AlertView alertView = new AlertView("Usuario creado", "Usuario creado con éxito", "El usuario ha sido creado con éxito "+ userInsert.getUsername());
            alertView.mostrarAlerta();
        }catch (Exception e){
            AlertView alertView = new AlertView("Error", "Error al crear usuario", e.getMessage());
            alertView.mostrarAlerta();
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para conseguir todos los usuarios
     * @return
     */
    public ArrayList<User> getAllUsers(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJson = new File("src/main/java/database/data.json");
            //Comprobamos que sigue existiendo el archivo
            if (!archivoJson.exists()) {
                System.out.printf("El archivo no existe: ", archivoJson.getName());
                return null;
            }
            //Comprobamos que no esté vacio
            if (archivoJson.length() == 0) {
                System.out.printf("El archivo está vacío: ", archivoJson.getName());
                return null;
            }
            //Leemos el contenido del archivo
            JsonNode rootNode = objectMapper.readTree(archivoJson);
            //Buscamos al usuario en el JSON
            for (JsonNode node : rootNode) {
                String usuarioExistente = node.get("username").asText();
                String password = node.get("password").asText();
                User user = new User(usuarioExistente, password);
                usersList.add(user);
            }
            return usersList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //return usersList;
    }
    public User getUserBD(User user){
        Connection conexion = new ConnectMysql().conectar();
        return new ConnectMysql().getUserBD(user);
    }

    public ArrayList<User> getUsersList() {
        getAllUsers();
        return usersList;
    }
    /**
     * Método para conseguir la configuración del usuario
     * @param user
     * @return
     */
    public UserConfig getUserConfig(User user){
        UserConfig userConfig = new UserConfig();
        userConfig.setUser(user);
        String userProfile = pathProfileImg+user.getUsername();
        String defaultProfile = pathProfileImg+"defaultProfile.png";
        try {
            //Inicializamos el maper
            ObjectMapper objectMapper = new ObjectMapper();
            //Guardar el archivo en la ruta resources
            File archivoConfig = new File("src/main/java/database/config.json");

            //Comprobar si existe el archivo
            if (!archivoConfig.exists()) {
                System.out.printf("El archivo no existe: ", archivoConfig.getName());
                return null;
            }

            //Leemos el contenido del archivo
            JsonNode rootNode = objectMapper.readTree(archivoConfig);
            //Buscamos al usuario en el JSON
            for (JsonNode node : rootNode){

               if (node.has("username") && node.get("username").asText().equals(user.getUsername())  ) {
                   String divisa = node.get("divisa").asText();
                   logger.log(System.Logger.Level.INFO, "Divisa del usuario: "+divisa);
                   Double saldo = node.get("saldo").asDouble();
                   logger.log(System.Logger.Level.INFO, "Saldo del usuario: "+saldo);
                     userConfig.setDivisa(divisa);
                        userConfig.setSaldo(saldo);

                   //Commprobamos si tiene foto de perfil
                   if (node.get("img").asText().equals("false")){
                       Image image = loadImageFromResouces(defaultProfile);
                       userConfig.setProfileImage(image);
                       return userConfig;
                   }else {
                       //En ese nodo está la extension de imagen
                          Image image = loadImageFromResouces(userProfile+"."+node.get("img").asText());
                       userConfig.setProfileImage(image);
                          return userConfig;
                   }
               }//si no tiewne configuracion le ponemos una default



            }
            logger.log(System.Logger.Level.INFO, "Usuario no tiene configuración");
            userConfig.setDivisa("USD");
            userConfig.setSaldo(1000.0);
            Image image = loadImageFromResouces(defaultProfile);
            userConfig.setProfileImage(image);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userConfig;
    }

    //Creamos el metodo para guardar la configuracion PREDETERMINADA del usuario
    public void createUserConfigBD(User user){
        Connection conexion = new ConnectMysql().conectar();
        new ConnectMysql().createUserConfig(user);
    }
    //Este metodo es el segundo que se emplea para guardar la configuracion del usuario
    //Despues llamamos al otro para soobresicbir los datos del usuario
    /**
     * Método para guardar la configuración del usuario
     * @param userConfig
     */
    private void saveUserConfig(UserConfig userConfig){
        try {
            //Inicializamos el maper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);//Formatea el json

            //Guardar el archivo en la ruta resources
            File archivoConfig = new File("src/main/java/database/config.json");
            logger.log(System.Logger.Level.INFO, "Ruta del archivo de configuraciones: "+archivoConfig);
            System.out.println("Ruta del archivo de configuraciones: "+archivoConfig);

            ArrayNode userArray;
            if (archivoConfig.exists() && archivoConfig.length() > 0) {
                userArray = (ArrayNode) objectMapper.readTree(archivoConfig);
            } else {
                userArray = objectMapper.createArrayNode();
            }
            //Buscamos si existe el usuario en el archivo de configuraciones o si no existe lo creamos
            boolean userExists = false;
            for (JsonNode node : userArray){
                logger.log(System.Logger.Level.INFO, "Usuario encontrado en el archivo de configuraciones");
                System.out.println("Usuario encontrado en el archivo de configuraciones");
                String extensionImg = userConfig.getProfileImage().getUrl().substring(userConfig.getProfileImage().getUrl().lastIndexOf(".")+1);
                if (node.get("username").asText().equals(userConfig.getUser().getUsername())){
                    ((ObjectNode) node).put("divisa", userConfig.getDivisa());
                    ((ObjectNode) node).put("saldo", userConfig.getSaldo());
                    //Validamos que la imagen no sea la default
                    if (userConfig.getProfileImage().getUrl().contains("defaultProfile.png")){
                        ((ObjectNode) node).put("img", false);
                    }else {
                        saveImageToResourcesPath(userConfig);
                         extensionImg = userConfig.getProfileImage().getUrl().substring(userConfig.getProfileImage().getUrl().lastIndexOf(".")+1);
                        ((ObjectNode) node).put("img", extensionImg);
                    }
                    ((ObjectNode) node).put("username", userConfig.getUser().getUsername());
                    ((ObjectNode) node).put("password", userConfig.getUser().getPassword());

                    userExists = true;
                    break;
                }
            }
            //Si no existe el usuario lo creamos
            if (!userExists){
                logger.log(System.Logger.Level.INFO, "Usuario no existe, se creará uno nuevo");
                System.out.println("Usuario no existe, se creará uno nuevo");
                ObjectNode newUser = objectMapper.createObjectNode();
                newUser.put("username", userConfig.getUser().getUsername());
                newUser.put("password", userConfig.getUser().getPassword());
                if (userConfig.getProfileImage().getUrl().contains("defaultProfile.png")){
                    newUser.put("img", false);
                }else {
                    saveImageToResourcesPath(userConfig);
                    String extensionImg = userConfig.getProfileImage().getUrl().substring(userConfig.getProfileImage().getUrl().lastIndexOf(".")+1);
                    newUser.put("img", extensionImg);
                }
                newUser.put("divisa", userConfig.getDivisa());
                newUser.put("saldo", userConfig.getSaldo());

                userArray.add(newUser);
            }
            //Escribimos el nuevo array en el archivo
            objectMapper.writeValue(archivoConfig, userArray);
            logger.log(System.Logger.Level.INFO, "Usuario guardado "+userConfig.getUser().getUsername()+"en el archivo de configuraciones");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void createUserconfigBD(User user){
        new ConnectMysql().createUserConfig(user);
    }
    public UserConfig getUserConfigBD(User user) {
        return new ConnectMysql().getUserConfigBD(user);
    }
    public void createUserBD(User user){
        new ConnectMysql().createUser(user);
    }
    //Para actualizar al usuario y la configuracion usamos este metodo que a su vez emplea el otro
    //Creamos el metodo para sobreescribir los datos del usuario del archivo data.json
    /**
     * Método para actualizar el usuario
     * @param userConfig
     */
    public void updateUser(UserConfig userConfig){
        try {
            //Inicializamos el maper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);//Formatea el json

            //Guardar el archivo en la ruta resources
            File archivoJson = new File("src/main/java/database/data.json");

            logger.log(System.Logger.Level.INFO, "Ruta del archivo de usuarios: "+archivoJson);
            System.out.println("Ruta del archivo de usuarios: "+archivoJson);

            ArrayNode userArray;
            if (archivoJson.exists() && archivoJson.length() > 0) {
                ;
                userArray = (ArrayNode) objectMapper.readTree(archivoJson);
            } else {
                userArray = objectMapper.createArrayNode();
            }
            //Buscamos si existe el usuario en el archivo de configuraciones o si no existe lo creamos
            for (JsonNode node : userArray){
                logger.log(System.Logger.Level.INFO, "Usuario encontrado en el archivo de usuarios");
                if (node.get("username").asText().equals(userConfig.getLastname())){
                    ((ObjectNode) node).put("username", userConfig.getUser().getUsername());
                    ((ObjectNode) node).put("password", userConfig.getUser().getPassword());
                    break;
                }
            }
            //Escribimos el nuevo array en el archivo
            objectMapper.writeValue(archivoJson, userArray);
            //logger.log(System.Logger.Level.INFO, "Usuario actualizado "+userConfig.getUser().getUsername()+"en el archivo de usuarios");
            System.out.println("Usuario actualizado "+userConfig.getUser().getUsername()+"en el archivo de usuarios");
            //Guardamos la configuración del usuario
            saveUserConfig(userConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //coger las inversiones del usuario
    /**
     * Método para conseguir las inversiones del usuario
     * @param user
     * @return
     */
    public ArrayList<Inversion> getInversionesBD(User user){
        return new ConnectMysql().getInversionesBD(user);
    }
    //METODOS PARA LAS IMAGENES
    /**
     * Método para cargar una imagen desde resources
     * @param path
     * @return
     */
   private Image loadImageFromResouces(String path){
        try {
            URL resourceUrl = getClass().getResource(path);
            logger.log(System.Logger.Level.INFO, "Imagen cargada desde "+resourceUrl.toExternalForm());
            return new Image(resourceUrl.toExternalForm());
        } catch (NullPointerException e) {
            throw new RuntimeException("Error al cargar imagen"+e);
        }catch (Exception e){
            throw new RuntimeException("Error al cargar imagen no existe"+e);
        }
   }
   /**
    * Método para guardar una imagen en resources
    * @param userConfig
    */
   private void saveImageToResourcesPath(UserConfig userConfig){
        try {
            //Cogemos la imagen del usuario
            Image image = userConfig.getProfileImage();
            String extension = image.getUrl().substring(image.getUrl().lastIndexOf(".")+1);
            String nombreArchivo = userConfig.getUser().getUsername()+"."+extension;
            File output = new File(pathProfileImgSave+nombreArchivo);
            URL url = new URL(image.getUrl());

            //Guardamos esa imagen en el directorio
            try(InputStream inputStream = url.openStream()){
                //Copiamos la imagen
                FileOutputStream fileOutputStream = new FileOutputStream(output);
                byte[] buffer = new byte[1024];
                int bytesLeidos;

                while ((bytesLeidos = inputStream.read(buffer)) != -1){
                    fileOutputStream.write(buffer,0,bytesLeidos);
                }
                System.out.println("Imagen guardada en "+output.getAbsolutePath());
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("Error al cargar imagen"+e);
        }catch (Exception e){
            throw new RuntimeException("Error al cargar imagen no existe"+e);
        }

   }
   //METODOS PARA LAS INVERSIONES DEL USUARIO
    /**
     * Método para conseguir las inversiones del usuario
     * @param user
     * @return
     */
    public ArrayList <Inversion> getUserInversiones(User user){
        ArrayList<Inversion> userInversionesList = new ArrayList<>();
        Inversion inversiones = new Inversion();

        System.out.println("Buscando inversiones del usuario: "+user.getUsername());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJson = new File("src/main/java/database/inversiones.json");
            //Comprobamos que sigue existiendo el archivo
            if (!archivoJson.exists()) {
                System.out.printf("El archivo no existe: ", archivoJson.getName());
                return null;
            }
            //Comprobamos que no esté vacio
            if (archivoJson.length() == 0) {
                System.out.printf("El archivo está vacío: ", archivoJson.getName());
                return null;
            }
            //Leemos el contenido del archivo
            JsonNode rootNode = objectMapper.readTree(archivoJson);
            cryptoRepository.initCriptoList();
            //Buscamos las inversiones del usuario en el JSON
            for (JsonNode node : rootNode){
                if (node.get("username").asText().equals(user.getUsername())){
                    //Conseguimos los datos
                    Date fechaInversion = stringToDate(node.get("fecha").asText());
                    Crypto crypto = cryptoRepository.getCoinByName(node.get("crypto").asText());
                    Double precioCompraCrypto = node.get("precioCompraCrypto").asDouble();
                    Double importeCrypto = node.get("importe").asDouble();
                    String tipo = node.get("tipo").asText();
                    String divisa = node.get("divisa").asText();
                    String transaccion = node.get("transaccion").asText();
                    Boolean vendida = node.get("vendida").asBoolean();

                    //Creamos la inversion y le ponemos la transaccion
                    Inversion inversion = new Inversion(divisa, tipo,importeCrypto,precioCompraCrypto,fechaInversion,crypto,user);
                    inversion.setTransaccion(transaccion);
                    inversion.setVendida(vendida); //si está vendida
                    //Añadimos la inversion a la lista
                    userInversionesList.add(inversion);
                   // System.out.println("Inversion encontrada: "+inversion.getTransaccion() +"----"+inversion.getFechaInversion());
                  //  logger.log(System.Logger.Level.INFO, "Inversion encontrada: "+inversion.getVendida()+"---"+inversion.getCrypto().getName());
                }
            }
        }catch (NullPointerException ex){
            System.out.println("Error al consegur inversiones :"+ ex.getMessage());
        }catch (Exception e){
            System.out.println("Error al conseguir inversiones: "+e.getMessage());
        }

        return userInversionesList;
    }
    //metodo para actualizar la inversion
    /**
     * Método para actualizar la inversion
     * @param transaccion
     */
    public void updateInvsersion (String transaccion){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);//Formatea el json

            File archivoJson = new File("src/main/java/database/inversiones.json");
            //Comprobamos que sigue existiendo el archivo
            if (!archivoJson.exists()) {
                System.out.printf("El archivo no existe: ", archivoJson.getName());
                return;
            }
            //Comprobamos que no esté vacio
            if (archivoJson.length() == 0) {
                System.out.printf("El archivo está vacío: ", archivoJson.getName());
                return;
            }
            //Leemos el contenido del archivo
            JsonNode rootNode = objectMapper.readTree(archivoJson);
            //Buscamos las inversiones del usuario en el JSON
            for (JsonNode node : rootNode) {
                if (node.get("transaccion").asText().equals(transaccion)) {
                    ((ObjectNode) node).put("vendida", true);
                    break;
                }
            }
            //Escribimos el nuevo array en el archivo
            objectMapper.writeValue(archivoJson, rootNode);
            logger.log(System.Logger.Level.INFO, "Inversion actualizada "+transaccion+"en el archivo de inversiones");

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    //Metodo para guardar las inversiones del usuario
    /**
     * Método para guardar la inversion
     * @param inversion
     */
    public void saveInversion(Inversion inversion) {
        try {
            //Inicializamos el maper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);//Formatea el json

            //Guardar el archivo en la ruta resources
            File archivoJson = new File("src/main/java/database/inversiones.json");

            logger.log(System.Logger.Level.INFO, "Ruta del archivo de inversiones: " + archivoJson);
            System.out.println("Ruta del archivo de inversiones: " + archivoJson);

            ArrayNode inversionArray;
            if (archivoJson.exists() && archivoJson.length() > 0) {
                ;
                inversionArray = (ArrayNode) objectMapper.readTree(archivoJson);
            } else {
                inversionArray = objectMapper.createArrayNode();
            }
            //Asignamos un serial a la transaccion
            inversion.setTransaccion(randomTransactionSerial());
            System.out.println("Transaccion: "+inversion.getTransaccion());

            //Creamos nuevo objecto de inversion
            ObjectNode newInversion = objectMapper.createObjectNode();
            newInversion.put("username", inversion.getUser().getUsername());
            newInversion.put("fecha", inversion.getFechaInversion().toString());
            newInversion.put("transaccion", inversion.getTransaccion());
            newInversion.put("crypto", inversion.getCrypto().getName());
            newInversion.put("precioCompraCrypto", inversion.getPrecioCompraCrypto());
            newInversion.put("importe", inversion.getImporteInversion());
            newInversion.put("tipo", inversion.getTipo());
            newInversion.put("divisa", inversion.getDivisa());
            newInversion.put("vendida", inversion.getVendida() ? "true" : "false");
            //Añadimos la inversion al array
            inversionArray.add(newInversion);
            //Escribimos el nuevo array en el archivo
            objectMapper.writeValue(archivoJson, inversionArray);
            logger.log(System.Logger.Level.INFO, "Inversion guardada " + inversion.getTransaccion() + "en el archivo de inversiones");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    //Metodo pasar un String en formato Fri Jan 17 00:37:11 CET 2025 a Date
    /**
     * Método para convertir un string en fecha
     * @param fecha
     * @return
     */
    public Date stringToDate(String fecha){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            return formatter.parse(fecha);
        }catch (Exception e){
            throw new RuntimeException("Error al convertir la fecha: "+e.getMessage());
        }
    }
    //metodo para generar un serial unico
    /**
     * Método para generar un serial único
     * @return
     */
    public String randomTransactionSerial(){
        String [] letras = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String [] numeros = {"0","1","2","3","4","5","6","7","8","9"};
        String serial = "";
        Random random = new Random();
        for (int i = 0; i <10 ; i++) {
            serial += letras[random.nextInt(letras.length)];
            serial += numeros[random.nextInt(numeros.length)];
        }
        //Comprobamos que no exista el serial
        if (checkSerialNumber(serial)){
            randomTransactionSerial();
        }
        System.out.println("Serial ÚNICO: "+serial);
        return serial;
    }
    //Metodo para comprobar si el serial ya existe
    /**
     * Método para comprobar si el serial ya existe
     * @param serial
     * @return
     */
    public Boolean checkSerialNumber(String serial){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJson = new File("src/main/java/database/inversiones.json");
            //Comprobamos que sigue existiendo el archivo
            if (!archivoJson.exists()) {
                System.out.printf("El archivo no existe: ", archivoJson.getName());
                return false;
            }
            //Comprobamos que no esté vacio
            if (archivoJson.length() == 0) {
                System.out.printf("El archivo está vacío: ", archivoJson.getName());
                return false;
            }
            //Leemos el contenido del archivo
            JsonNode rootNode = objectMapper.readTree(archivoJson);
            //Buscamos las inversiones del usuario en el JSON
            for (JsonNode node : rootNode){
                if (node.get("transaccion").asText().equals(serial)){
                    System.out.println("Serial ya existe");
                    return true;
                }
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public void saveInversionesBD(Inversion inversione) {
        new ConnectMysql().saveInversion(inversione);
    }

    public ArrayList<Inversion> getUserInversionesBd(User user) {
        return new ConnectMysql().getInversionesBD(user);
    }
}
