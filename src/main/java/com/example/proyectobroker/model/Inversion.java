package com.example.proyectobroker.model;

import java.util.Date;

public class Inversion {
    private User user;
    private Crypto crypto;
    private Date fechaInversion;
    private String transaccion;
    private Double precioCompraCrypto;
    private Double importeInversion;
    private Double cantidadCrypto;
    private String tipo; //compra o venta
    private String divisa;
    private Boolean vendida;


    public Inversion() {
    }

    public Inversion(String divisa, String tipo, Double importeInversion, Double precioCompraCrypto, Date fechaInversion, Crypto crypto, User user) {
        this.divisa = divisa;
        this.tipo = tipo;
        this.importeInversion = importeInversion;
        this.precioCompraCrypto = precioCompraCrypto;
        this.fechaInversion = fechaInversion;
        this.crypto = crypto;
        this.user = user;
        calcularCantidadCrypto();
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public Date getFechaInversion() {
        return fechaInversion;
    }

    public void setFechaInversion(Date fechaInversion) {
        this.fechaInversion = fechaInversion;
    }

    public Double getPrecioCompraCrypto() {
        return precioCompraCrypto;
    }

    public void setPrecioCompraCrypto(Double precioCompraCrypto) {
        this.precioCompraCrypto = precioCompraCrypto;
    }

    public Double getImporteInversion() {
        return importeInversion;
    }

    public void setImporteInversion(Double importeInversion) {
        this.importeInversion = importeInversion;
    }

    public Double getCantidadCrypto() {
        return cantidadCrypto;
    }


    public void setCantidadCrypto(Double cantidadCrypto) {
        this.cantidadCrypto = cantidadCrypto;
    }

    public Boolean getVendida() {
        return vendida;
    }

    public void setVendida(Boolean vendida) {
        this.vendida = vendida;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }
    //Redondeo a dos decimales
    public Double roundPrice(){
        Double valor;
        String precio = String.format("%.2f", precioCompraCrypto);
        precio = precio.replace(",", ".");
        valor = Double.parseDouble(precio);
        return valor;
    }
    //Metodo que consigue la cantidad de criptomonedas multiplicando el importe de la inversion por el precio de compra
    public void calcularCantidadCrypto(){
        Double cantidad;
        cantidad = (importeInversion / precioCompraCrypto);
        this.cantidadCrypto = cantidad;
    }
    public Double getGanancia(){
        Crypto crypto = this.crypto;
        Double precioActual = Double.parseDouble(crypto.getPrice());
        Double ganancia = (precioActual - precioCompraCrypto);

        return ganancia;

    }
    @Override
    public String toString() {
        return "Inversion{" +
                "user=" + user.getUsername() +
                ", crypto=" + crypto.getName() +
                ", fechaInversion=" + fechaInversion +
                ", precioCompraCrypto=" + precioCompraCrypto +
                ", importeInversion=" + importeInversion +
                ", cantidadCrypto=" + cantidadCrypto +
                ", tipo='" + tipo + '\'' +
                ", divisa='" + divisa + '\'' +
                '}';
    }
}
