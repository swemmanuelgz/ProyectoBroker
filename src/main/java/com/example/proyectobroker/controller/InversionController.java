package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;

import java.util.ArrayList;

public class InversionController {
    private UserRepository userRepository = new UserRepository();

    public InversionController() {

    }
    //metodo para coger las inversiones de un usuario
    /**
     * Metodo para coger las inversiones de un usuario
     * @param user
     * @return
     */
    public ArrayList<Inversion> getUserInversions(User user) {
        System.out.println("Getting user inversions");
        return userRepository.getUserInversiones(user);
    }

    //Metodo para guardar una inversion
    /**
     * Metodo para guardar una inversion
     * @param inversion
     */
    public void saveInversion(Inversion inversion) {
        userRepository.saveInversion(inversion);
    }
    //metodo para actualizar la inversion y ponerla como vendida en la base de datos
    /**
     * Metodo para actualizar la inversion y ponerla como vendida en la base de datos
     * @param inversion
     */
    public void updateInversion(Inversion inversion) {
        userRepository.updateInvsersion(inversion.getTransaccion());
    }
    //metodo para coger inversiones que sean de compra
    /**
     * Metodo para coger inversiones que sean de compra
     * @param user
     * @return
     */
    public ArrayList<Inversion> getCompras(User user) {
        ArrayList<Inversion> compras = new ArrayList<>();

        for (Inversion inversion : getUserInversions(user)) {
            //si la inversion es de compra y no esta vendida la añadimos a la lista
            if (inversion.getTipo().equals("compra") && !inversion.getVendida()) {
                compras.add(inversion);
            }
        }
        return compras;
    }
    //metodo para coger inversiones que sean de venta
    /**
     * Metodo para coger inversiones que sean de venta
     * @param user
     * @return
     */
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
