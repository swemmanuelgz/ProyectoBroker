package com.example.proyectobroker.controller;

import com.example.proyectobroker.Main;
import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.repository.UserRepository;
import database.ConnectMysql;

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
    //MNETODO GUARDAR INVERSIONES BD
    /**
     * Metodo para guardar inversiones en la base de datos
     * @param inversion
     */
    public void saveInversionesBd(Inversion inversion) {
        userRepository.saveInversionesBD(inversion);
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
    //metodo para coger inversiones que sean de compra de la base de datos
    /**
     * Metodo para coger inversiones que sean de compra
     * @param user
     * @return
     */
    public ArrayList<Inversion> getComprasBd(User user) {
        ArrayList<Inversion> compras = userRepository.getUserInversionesBd(user);
        ArrayList<Inversion> comprasFiltradas = new ArrayList<>();
        System.out.println(Main.ANSI_PURPLE + "Filtrando Compras" + Main.ANSI_RESET);
        for (Inversion inversion : compras) {
            if ( !inversion.getVendida()) {
                comprasFiltradas.add(inversion);
            }
        }
        return comprasFiltradas;
    }
    //metodo para coger inversiones que sean de compra de la memoria
    /**
     * Metodo para coger inversiones que sean de compra de la memoria
     * @param user
     * @return
     */
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
    //TODO: metodo para coger las inversiones que esten vendidas de la base de datos
    //metodo para coger las inversiones que esten vendidas de la base de datos
    /**
     * Metodo para coger las inversiones que esten vendidas de la base de datos
     * @param user
     * @return
     */
    public ArrayList<Inversion> getVendidas(User user) {
        ArrayList<Inversion> vendidas = new ArrayList<>();
        for (Inversion inversion : getUserInversions(user)) {
            if (inversion.getVendida()) {
                vendidas.add(inversion);
            }
        }
        return vendidas;
    }
}
