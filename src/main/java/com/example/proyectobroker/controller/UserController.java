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
    public User loginBD(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User usuario = userRepository.getUserBD(user);
        if (usuario != null) {
            if (bCryptPasswordEncoder.matches(user.getPassword(), usuario.getPassword())) {
                this.usuario = usuario;
                logger.info("Usuario encontrado"+ usuario.getUsername()+ " "+ usuario.getPassword());
                return usuario;
            }else {
                logger.info("Contraseña incorrecta");
            }
        }
        logger.info("Usuario no encontrado");
        return usuario;
    }
    public void createUserConfigBd(User user) {
         userRepository.createUserConfigBD(user);
    }
    public UserConfig getUserConfigBD(User user) {
        return userRepository.getUserConfigBD(user);
    }
    public void createUserBD(User user) {
        userRepository.createUserBD(user);
    }
    public boolean checkUserExistsBd(User user) {
        return userRepository.checkuserExistsBd(user);
    }
    public User getUserBD(User user) {
        return userRepository.getUserBD(user);
    }

    //Metodo para actualizar la configuracion del usuario
    public void updateUserConfig(User user) {
        userRepository.updateUser(user.getUserConfig());
    }
    public void updateUserBd(User user) {
        userRepository.updateUserBD(user);
    }

}
