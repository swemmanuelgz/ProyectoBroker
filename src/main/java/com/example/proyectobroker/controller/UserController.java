package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.ArrayList;

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
        ArrayList<User> users = userRepository.getUsersList();
        for (User usuario : users) {
            if (usuario.getUsername().equals(user.getUsername())) {
                if (bCryptPasswordEncoder.matches(user.getPassword(), usuario.getPassword())) {
                    this.usuario = usuario;
                    return usuario;
                }
            }
        }
        return usuario;
    }

}
