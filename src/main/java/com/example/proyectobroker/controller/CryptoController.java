package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.repository.CryptoRepository;

import java.util.ArrayList;

public class CryptoController {
    private CryptoRepository cryptoRepository = new CryptoRepository();
    private ArrayList<Crypto> cryptos = new ArrayList<>();
    public CryptoController() {
    }
    //metodo para coger las criptomonedas de la memoria Y NO DE LA BASE DE DATOS
    /**
     * Metodo para coger las criptomonedas de la memoria Y NO DE LA BASE DE DATOS
     * @return
     */
    public ArrayList<Crypto> getMemoryCrypto(){
        return cryptos;
    }
    //metodo para coger todas las criptomonedas de la API
    /**
     * Metodo para coger todas las criptomonedas de la API
     * @return
     */
    public ArrayList<Crypto> getAllCrypto(){
            cryptos = cryptoRepository.getCoins();
            return cryptoRepository.getCoins();

    }
    //metodo para converit una criptomoneda a euros
    /**
     * Metodo para convertir una criptomoneda a euros
     * @param crypto
     * @return
     */
    public Crypto convertToEuros(Crypto crypto){
        return cryptoRepository.convertToEuros(crypto);
    }
}
