package com.example.proyectobroker.utils;

/**
 * Esta clase es un enumerado para albergar las constantes de la aplicación.
 */
public enum Constantes {
    PAGINA_INICIAL("login.fxml"),
    PAGINA_SEGUNDA_PANTALLA("mainMenu.fxml"),
    TITULO_PAGINA_INICIAL("Login"),
    TITULO_SEGUNDA_PANTALLA("Main Menu");

    private final String descripcion;

    // Constructor para asociar una descripción a cada constante
    Constantes(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
