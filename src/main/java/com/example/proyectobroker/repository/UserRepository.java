package com.example.proyectobroker.repository;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.view.AlertView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.scene.image.Image;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> usersList = new ArrayList<>();
    private final String pathProfileImg = "/com/example/proyectobroker/img/profile/";


    public UserRepository() {

    }
    public  boolean checkUserExists(User user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File archivoJson = new File("src/main/java/database/data.json");

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
        String userProfile = pathProfileImg+user.getUsername()+".png";
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

               if (node.has("username") ||rootNode.get("username").asText().equals(user.getUsername())  ) {
                   String divisa = node.get("divisa").asText();
                   Double saldo = node.get("saldo").asDouble();
                     userConfig.setDivisa(divisa);
                        userConfig.setSaldo(saldo);

                   //Commprobamos si tiene foto de perfil
                   if (!node.get("img").asBoolean()){
                       Image image = loadImageFromResouces(defaultProfile);
                       userConfig.setProfileImage(image);
                       return userConfig;
                   }else {
                          Image image = loadImageFromResouces(userProfile);
                       userConfig.setProfileImage(image);
                          return userConfig;
                   }
               }//si no tiewne configuracion le ponemos una default
               else {
                   userConfig.setDivisa("USD");
                   userConfig.setSaldo(1000.0);
                   Image image = new Image(pathProfileImg+"defaultProfile.png");
                   userConfig.setProfileImage(image);
                     return userConfig;
               }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userConfig;
    }
   private Image loadImageFromResouces(String path){
        try {
            String fullPath = getClass().getResource(path).toExternalForm();
            return new Image(fullPath);
        } catch (NullPointerException e) {
            throw new RuntimeException("Error al cargar imagen"+e);
        }catch (Exception e){
            throw new RuntimeException("Error al cargar imagen no existe"+e);
        }
   }

}
