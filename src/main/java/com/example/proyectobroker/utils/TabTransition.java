package com.example.proyectobroker.utils;
import javafx.animation.TranslateTransition;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class TabTransition {

    public static void switchTabsWithTransition(TabPane tabPane, Pane currentPane, Pane nextPane) {
        // Duración de la animación
        Duration duration = Duration.millis(500);

        // Crear una transición de deslizamiento para el pane actual (hacia la izquierda)
        TranslateTransition outTransition = new TranslateTransition(duration, currentPane);
        outTransition.setToX(-currentPane.getWidth()); // Se desliza hacia la izquierda

        // Crear una transición de deslizamiento para el siguiente pane (entrando desde la derecha)
        TranslateTransition inTransition = new TranslateTransition(duration, nextPane);
        nextPane.setTranslateX(nextPane.getWidth()); // Colocarlo fuera de pantalla inicialmente
        inTransition.setToX(0); // Moverlo a su posición original

        // Ejecutar las transiciones secuencialmente
        outTransition.setOnFinished(event -> {
            // Cambiar el tab cuando termine la transición de salida
            tabPane.getSelectionModel().select(tabPane.getTabs().stream()
                    .filter(tab -> tab.getContent() == nextPane)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Tab no encontrado!")));
            outTransition.stop();
        });

        outTransition.play();
        inTransition.play();
    }
}

