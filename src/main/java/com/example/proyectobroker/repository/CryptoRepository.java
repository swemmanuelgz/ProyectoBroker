package com.example.proyectobroker.repository;


import com.example.proyectobroker.model.Crypto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.finazon.GetTimeSeriesRequest;
import io.finazon.TimeSeries;
import io.finazon.TimeSeriesService;
import io.grpc.StatusRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class CryptoRepository {

    ArrayList<Crypto> cryptoList = new ArrayList<>();


    public void CryptoRepository() {

    }
    //Metodo para conseguir las criptomonedas de la API
    /**
     * Metodo para conseguir las criptomonedas de la API
     * @return
     */
    public ArrayList<Crypto> getCoins(){
         final String urlRapidApi ="https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&orderBy=marketCap&orderDirection=desc&limit=50&offset=0";
         final String apiKeyRapidApi ="925195b3dfmsh3ee477449ad3425p17feffjsn72acfbb0657f";
         final String hostRapidApi ="coinranking1.p.rapidapi.com";

            try {
                URL url = new URL(urlRapidApi);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-rapidapi-key", apiKeyRapidApi  );
                connection.setRequestProperty("x-rapidapi-host", hostRapidApi);
                connection.setRequestProperty("Content-Type", "application/json");
                //Cogemos la respuesta
                System.out.println("Response Code: " + connection.getResponseCode());
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();
                    //Comvertir la respuesta en JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonResponse = objectMapper.readTree(response.toString());
                    //System.out.println("JSON: " + jsonResponse.toPrettyString());
                    // Iteramos una sola vez porque el primer nodo contiene los datos actuales
                    if ( jsonResponse.size() > 0) {
                        // Primer nodo
                        JsonNode dataArray = jsonResponse.get("data").get("coins");
                       // System.out.println("Data Array: " + dataArray.toPrettyString());
                        for (JsonNode node : dataArray) {
                            Crypto crypto = new Crypto();
                            crypto.setUuid(node.get("uuid").asText()); //campo uuid
                            crypto.setSymbol(node.get("symbol").asText()); //campo symbol
                            crypto.setName(node.get("name").asText()); //campo name
                            crypto.setColor(node.get("color").asText()); //campo color
                            crypto.setIconUrl(node.get("iconUrl").asText()); //campo iconUrl
                            crypto.setMarketcap(node.get("marketCap").asText()); //campo marketCap
                            crypto.setPrice(node.get("price").asText()); //campo price
                            crypto.setListedAt(node.get("listedAt").asText()); //campo listedAt
                            crypto.setTier(node.get("tier").asText()); //campo tier
                            crypto.setChange(node.get("change").asDouble()); //campo change
                            crypto.setRank(node.get("rank").asInt()); //campo rank

                            JsonNode sparkline = node.get("sparkline");
                            Double[] sparklineArray = new Double[sparkline.size()];
                            //Hacemos un bucle para recorrer el array de sparkline
                            for (int i = 0; i < sparkline.size(); i++) {
                                sparklineArray[i] = sparkline.get(i).asDouble();
                            }
                            crypto.setSparkline(sparklineArray); //campo sparkline
                            //System.out.println("Crypto: " + crypto.getName()+ " "+" Rank:" +crypto.getRank());
                            cryptoList.add(crypto);
                        }
                        return cryptoList;
                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        return cryptoList;
    }
    //Para conseguir la moneda por su uuid
    /**
     * Para conseguir la moneda por su uuid
     * @param uuiid
     * @return
     */
    public Crypto getCoinByuuid(String uuiid){
        for (Crypto crypto : cryptoList) {
            if (crypto.getUuid().equals(uuiid)){
                return crypto;
            }
        }
        return null;
    }
    //metodo para inicializar la lista de criptomonedas
    /**
     * Metodo para inicializar la lista de criptomonedas
     */
    public void initCriptoList(){
        cryptoList = getCoins();
    }

    //Para conseguir la moneda por su nombre
    /**
     * Para conseguir la moneda por su nombre
     * @param name
     * @return
     */
    public Crypto getCoinByName(String name){

        for (Crypto crypto : cryptoList) {
            if (crypto.getName().equalsIgnoreCase(name)){
                return crypto;
            }
        }
        return null;
    }
    //Metodo para pasar el precio de una cryto de Dolares a Euros

    /**
     * Método para pasar el precio de una cryto de Dolares a Euros
     * @param crypto
     * @return
     */
    public Crypto convertToEuros(Crypto crypto){
        System.out.println("Precio en dolares: " + crypto.getPrice());
        Double price = Double.parseDouble(crypto.getPrice().replace(",","."));
        System.out.println("Precio en dolares: " + price);
        Double priceEur = price * 0.9710;

        String precio = String.valueOf(priceEur).replace(".",",");
        System.out.println("Precio en euros con coma: " + precio);
        crypto.setPrice(String.valueOf(priceEur));
        crypto.roundPrice();

        return crypto;
    }
}
