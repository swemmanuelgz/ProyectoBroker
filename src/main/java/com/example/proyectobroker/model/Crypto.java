package com.example.proyectobroker.model;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;

public class Crypto {
    private String uuid;
    private String symbol;
    private String name;
    private String color;
    private String iconUrl;
    private Image icon;
    private String marketcap;
    private String price;
    private String listedAt;
    private String tier;
    private Double change;
    private Integer rank;
    private Double[] sparkline;
    private String coinRankingUrl;

    public Crypto(String uuid, String symbol, String name, String color, String iconUrl, String marketcap, String price, String listedAt, String tier, Double change, Integer rank, Double[] sparkline, String coinRankingUrl) {
        this.uuid = uuid;
        this.symbol = symbol;
        this.name = name;
        this.color = color;
        this.iconUrl = iconUrl;
        this.marketcap = marketcap;
        this.price = price;
        this.listedAt = listedAt;
        this.tier = tier;
        this.change = change;
        this.rank = rank;
        this.sparkline = sparkline;
        this.coinRankingUrl = coinRankingUrl;
    }

    public Crypto() {
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(String marketcap) {
        this.marketcap = marketcap;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getListedAt() {
        return listedAt;
    }

    public void setListedAt(String listedAt) {
        this.listedAt = listedAt;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Double[] getSparkline() {
        return sparkline;
    }

    public void setSparkline(Double[] sparkline) {
        this.sparkline = sparkline;
    }

    public String getCoinRankingUrl() {
        return coinRankingUrl;
    }

    public void setCoinRankingUrl(String coinRankingUrl) {
        this.coinRankingUrl = coinRankingUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Metodo que redondea el precio de la criptomoneda a dos decimales
    /**
     * Metodo que redondea el precio de la criptomoneda a dos decimales
     * @return Double
     */
    public Double roundPrice() {
        Double precioRedondeado;
        String price = String.format("%.2f", Double.parseDouble(this.price));
        price = price.replace(",", ".");
        System.out.println("Precio redondeado: " + price);
        precioRedondeado = Double.parseDouble(price);
        return precioRedondeado;
    }
    //Metodo para descargar el icono
    /**
     * Metodo para descargar el icono
     */
    public void downloadIcon() {
        //Soporte para imagenes SVG
        try (InputStream inputStream = new java.net.URL(iconUrl).openStream()) {
            if (iconUrl.endsWith(".svg")) {
                SVGImage svgImage = SVGLoader.load(new URL(iconUrl));
                this.icon = svgImage.toImage();
            } else {
                icon = new Image(inputStream);
            }
            System.out.println("Icono descargado" + iconUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "uuid='" + uuid + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", marketcap='" + marketcap + '\'' +
                ", price='" + price + '\'' +
                ", listedAt='" + listedAt + '\'' +
                ", tier='" + tier + '\'' +
                ", change=" + change +
                ", rank=" + rank +
                ", sparkline=" + Arrays.toString(sparkline) +
                ", coinRankingUrl='" + coinRankingUrl + '\'' +
                '}';
    }
}
