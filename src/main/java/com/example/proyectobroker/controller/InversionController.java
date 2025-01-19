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
    //metodo para coger inversiones que sean de compra
    public ArrayList<Inversion> getCompras(User user) {
        ArrayList<Inversion> compras = new ArrayList<>();

        for (Inversion inversion : getUserInversions(user)) {
            if (inversion.getTipo().equals("compra")) {
                compras.add(inversion);
            }
        }
        return compras;
    }
    //metodo para coger inversiones que sean de venta
    public ArrayList<Inversion> getVentas(User user) {
        ArrayList<Inversion> ventas = new ArrayList<>();
        for (Inversion inversion : getUserInversions(user)) {
            if (inversion.getTipo().equals("venta")) {
                ventas.add(inversion);
            }
        }
        return ventas;
    }
}
