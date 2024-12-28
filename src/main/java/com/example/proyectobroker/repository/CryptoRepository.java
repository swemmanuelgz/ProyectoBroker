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
    private final String API_KEY="d46adc9f3bc442659d0987d576c4a744hw";
    private final String BASE_URL =  "https://api.finazon.io";


    public void CryptoRepository() {

    }

   /* public ArrayList<Crypto> getCryptoList() {
        ArrayList<Crypto> cryptoList = new ArrayList<>();
        String[] usdtPairs = {
                "BTC/USDT",  // Bitcoin
                "ETH/USDT",  // Ethereum
                "BNB/USDT",  // Binance Coin
                "XRP/USDT",  // Ripple
                "ADA/USDT",  // Cardano
                "DOGE/USDT", // Dogecoin
                "MATIC/USDT",// Polygon
                "SOL/USDT",  // Solana
                "DOT/USDT",  // Polkadot
                "LTC/USDT",  // Litecoin
                "SHIB/USDT", // Shiba Inu
                "TRX/USDT",  // TRON
                "AVAX/USDT", // Avalanche
                "ATOM/USDT", // Cosmos
                "ALGO/USDT", // Algorand
                "FTM/USDT",  // Fantom
                "NEAR/USDT", // NEAR Protocol
                "VET/USDT",  // VeChain
                "XTZ/USDT",  // Tezos
                "EOS/USDT"   // EOS
        };

        for (int i = 0; i < usdtPairs.length; i++) {
            String finalURL = BASE_URL + "/latest/finazon/crypto/time_series?ticker=" + usdtPairs[2] + "&interval=1d&page=0&page_size=30&apikey=" + API_KEY;
           // System.out.println(finalURL);
            //peticion get
            try {
                URL url = new URL(finalURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

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
                        JsonNode dataArray = jsonResponse.get("data");
                        JsonNode firstNode =dataArray.get(0);
                        Crypto crypto = new Crypto();

                        // Asignar valores del primer nodo
                        crypto.setTicker(usdtPairs[2]); // "BTC-USDT" es el ticker
                        crypto.setTimestamp(Instant.ofEpochSecond(firstNode.get("t").asLong())); // "t" es el timestamp
                        crypto.setOpen(firstNode.get("o").asDouble()); // "o" es el open
                        crypto.setHigh(firstNode.get("h").asDouble()); // "h" es el high
                        crypto.setLow(firstNode.get("l").asDouble()); // "l" es el low
                        crypto.setVolume(firstNode.get("v").asDouble()); // "v" es el volume

                        // Crear el histórico para la criptomoneda
                        HashMap<Instant, Crypto> cryptoHistorical = new HashMap<>();
                        for (JsonNode historicalNode : dataArray) {
                            String historicalTicker = usdtPairs[2];
                            Instant historicalTimestamp = Instant.ofEpochSecond(historicalNode.get("t").asLong());
                            Double historicalOpen = historicalNode.get("o").asDouble();
                            Double historicalHigh = historicalNode.get("h").asDouble();
                            Double historicalLow = historicalNode.get("l").asDouble();
                            Double historicalVolume = historicalNode.get("v").asDouble();

                            // Crear un objeto Crypto para cada nodo histórico
                            Crypto historicalCrypto = new Crypto(historicalTicker,historicalTimestamp, historicalOpen, historicalHigh, historicalLow, historicalVolume);

                            // Añadirlo al mapa histórico
                            cryptoHistorical.put(historicalTimestamp, historicalCrypto);
                        }
                        // Asignar el mapa histórico al objeto Crypto principal
                        crypto.setCrypyoHistorical(cryptoHistorical);

                        // Añadir el objeto Crypto a la lista
                        System.out.println("Crypto: " + crypto.toString());
                        cryptoList.add(crypto);
                    }
                    //return cryptoList;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return cryptoList;
    }*/
    public ArrayList<Crypto> getCoins(){
         final String urlRapidApi ="https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&orderBy=marketCap&orderDirection=desc&limit=50&offset=0";
         final String apiKeyRapidApi ="925195b3dfmsh3ee477449ad3425p17feffjsn72acfbb0657f";
         final String hostRapidApi ="coinranking1.p.rapidapi.com";
            ArrayList<Crypto> cryptoList = new ArrayList<>();
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
                            System.out.println("Crypto: " + crypto.getName()+ " "+" Rank:" +crypto.getRank());
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
}
