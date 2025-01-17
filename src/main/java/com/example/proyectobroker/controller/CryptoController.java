package com.example.proyectobroker.controller;

import com.example.proyectobroker.model.Crypto;
import com.example.proyectobroker.repository.CryptoRepository;

import java.util.ArrayList;

public class CryptoController {
    private CryptoRepository cryptoRepository = new CryptoRepository();
    private ArrayList<Crypto> cryptos = new ArrayList<>();
    public CryptoController() {
    }
    public ArrayList<Crypto> getMemoryCrypto(){
        return cryptos;
    }
    public ArrayList<Crypto> getAllCrypto(){
            cryptos = cryptoRepository.getCoins();
            return cryptoRepository.getCoins();

    }

    public Crypto convertToEuros(Crypto crypto){
        return cryptoRepository.convertToEuros(crypto);
    }
}
