package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

public class UserController {
    private UserRepository userRepository = new UserRepository();
    private User usuario;

    public UserController() {
    }
    public boolean checkUserExists(User user) {
        return userRepository.checkUserExists(user);
    }
    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public User login(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
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
                if (usuarioExistente.equalsIgnoreCase(user.getUsername()) && bCryptPasswordEncoder.matches(user.getPassword(), password)) {
                    usuario = new User(usuarioExistente, password);
                    return usuario;
                }else {
                    usuario = null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }
}
