package com.example.proyectobroker.repository;


import com.example.proyectobroker.view.AlertView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class CryptoRepository {
    private final String API_KEY="7c7MNwrs9pxhGdggNZsJm5XiRHyLSHevRPfWegRuu6ATPdnHze7JcZ6s33z8auFzLk";
    private final String API_SECRET ="IrBCgRMu55U5iIysWyAPEwVncTvBSJ1VbX5OFSwMzuC5KEp55YDnphlxAbEQKCRh";
    private final String BASE_URL =  "https://api.pionex.com";

    private String crearFirma(String data)throws Exception{
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(API_SECRET.getBytes(), "HmacSHA256");
        hmacSha256.init(secretKeySpec);
        byte[] hash = hmacSha256.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hash);

    }

    public void getCryptoList()throws Exception{
        long timestamp = System.currentTimeMillis();
        String query = "timestamp="+timestamp;
        String firma = crearFirma(query);

        System.out.println("timestamp: "+timestamp);
        System.out.println("firma: "+firma);
        String endpoint = "/api/v1/common/symbols";
        String tipo = "SPOT";

        //Url de la peticion
        String finalURL =BASE_URL+ endpoint+"?type="+tipo;
        System.out.printf("Final URL "+finalURL);

        HttpClient client = HttpClient.newHttpClient();

        //Creamos la peticion
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalURL))
                //.header("Authorization", API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        //Ejecutamos la peticion
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){
           // AlertView alertView = new AlertView("Respuesta", "Respuesta de la API", response.body());
           // alertView.mostrarAlerta();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonRespuesta = objectMapper.readTree(response.body());
            System.out.println("Respuesta: "+jsonRespuesta.toPrettyString());
        }else {
            AlertView alertView = new AlertView("Error", "Error en la API", "Error en la API");
            alertView.mostrarAlerta();
            System.out.println("Error: "+response.body());
        }


    }



}
