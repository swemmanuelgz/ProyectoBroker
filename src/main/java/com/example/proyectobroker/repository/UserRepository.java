package com.example.proyectobroker.repository;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.view.AlertView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.scene.image.Image;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> usersList = new ArrayList<>();
    private final String pathProfileImg = "/com/example/proyectobroker/img/profile/";
    private final String pathProfileImgSave = "src/main/resources/com/example/proyectobroker/img/profile/";
    private static final System.Logger logger = System.getLogger(UserRepository.class.getName());
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public UserRepository() {

    }
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return usersList;
    }

    public ArrayList<User> getUsersList() {
        getAllUsers();
        return usersList;
    }
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
    //Este metodo es el segundo que se emplea para guardar la configuracion del usuario
    //Despues llamamos al otro para soobresicbir los datos del usuario
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
                ;
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
                if (node.get("username").asText().equals(userConfig.getLastname())){
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
    //Para actualizar al usuario y la configuracion usamos este metodo que a su vez emplea el otro
    //Creamos el metodo para sobreescribir los datos del usuario del archivo data.json
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

}
