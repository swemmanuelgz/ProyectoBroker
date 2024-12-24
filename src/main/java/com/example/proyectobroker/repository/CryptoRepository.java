package com.example.proyectobroker.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CryptoRepository {
    private final String USER_TOKEN = "fe9ce1e75d624164ab0e4f6e108ee400";
    private final String BASE_URL = "https://api.profit.com";

    public JsonNode getHistoricoData(String symbol,String startDate ,String endDate) throws IOException {
        //Construimos la url para la solicitud
        String endpoint = String.format("/crypto/%s/history?start=%s&end=%s", symbol, startDate, endDate);
        URL url = new URL(BASE_URL + endpoint);
        //Abrimos la conexión
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + USER_TOKEN);

        //Leemos la respuesta
        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream);
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        //parseamos la respuesta
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("RERSPUESTA "+responseBody);
        return objectMapper.readTree(responseBody);
    }

}
