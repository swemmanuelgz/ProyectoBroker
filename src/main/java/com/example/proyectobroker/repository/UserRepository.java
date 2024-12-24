package com.example.proyectobroker.repository;

import com.example.proyectobroker.controller.CreateAccountController;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.view.AlertView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> users = new ArrayList<>();



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
}
