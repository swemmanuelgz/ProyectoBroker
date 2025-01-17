package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;

import java.util.ArrayList;

public class InversionController {
    private UserRepository userRepository = new UserRepository();

    public InversionController() {

    }
    public ArrayList<Inversion> getUserInversions(User user) {
        System.out.println("Getting user inversions");
        return userRepository.getUserInversiones(user);
    }

    //Metodo para guardar una inversion
    public void saveInversion(Inversion inversion) {
        userRepository.saveInversion(inversion);
    }
}
