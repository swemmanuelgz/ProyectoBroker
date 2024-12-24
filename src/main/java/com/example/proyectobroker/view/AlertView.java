package com.example.proyectobroker.view;

import javafx.scene.control.Alert;



public class AlertView {

    private String title;
    private String headerText;
    private String message;

    public AlertView() {
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

    public void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
