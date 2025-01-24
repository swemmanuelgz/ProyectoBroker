package com.example.proyectobroker.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class AlertView {

    private String title;
    private String headerText;
    private String message;
    private Alert.AlertType alertType;

    public AlertView() {
    }

    public AlertView(String title, String headerText, String message, Alert.AlertType alertType) {
        this.title = title;
        this.headerText = headerText;
        this.message = message;
        this.alertType = alertType;
    }
    public AlertView(String title, String headerText, String message) {
        this.title = title;
        this.headerText = headerText;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Alert.AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
    }

    public void mostrarAlerta() {
        Alert alert = new Alert(alertType != null ? alertType : Alert.AlertType.INFORMATION);
        alert.setTitle(title != null ? title : "Información");
        alert.setHeaderText(headerText != null ? headerText : "Mensaje");
        alert.setContentText(message != null ? message : "Contenido de la alerta");

        // Personalizar ícono de la alerta (requiere un ícono en tu proyecto)
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/proyectobroker/img/ico/icon_alert.png")));

        // Añadir un estilo CSS (opcional)
        //alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/alert-style.css").toExternalForm());

        // Mostrar la alerta y manejar la interacción
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("El usuario hizo clic en OK");
        }
    }
}
