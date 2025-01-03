package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UserController {
    private UserRepository userRepository = new UserRepository();
    private User usuario;
    private final Logger logger = Logger.getLogger(UserController.class.getName());


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
        ArrayList<User> users = userRepository.getUsersList();
        for (User usuario : users) {
            if (usuario.getUsername().equals(user.getUsername())) {
                if (bCryptPasswordEncoder.matches(user.getPassword(), usuario.getPassword())) {
                    this.usuario = usuario;
                    logger.info("Usuario encontrdado"+ usuario.getUsername()+ " "+ usuario.getPassword());
                    return usuario;
                }else {
                    logger.info("Contraseña incorrecta");
                }
            }
        }
        logger.info("Usuario no encontrado");
        return usuario;
    }
    public UserConfig getUserConfig(User user) {
        return userRepository.getUserConfig(user);
    }
    //Metodo para actualizar la configuracion del usuario
    public void updateUserConfig(User user) {
        userRepository.updateUser(user.getUserConfig());
    }

}
