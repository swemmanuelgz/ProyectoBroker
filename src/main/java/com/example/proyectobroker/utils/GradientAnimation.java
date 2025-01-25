package com.example.proyectobroker.utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GradientAnimation {
    // Método para animar el fondo de un pane con un gradiente de colores
    /**
     * Método para animar el fondo de un pane con un gradiente de colores
     * @param pane Pane
     */
    public static void animateBackground(Pane pane) {
        Timeline timeline = new Timeline();
        final int steps = 200; // Más pasos para fluidez
        final Duration duration = Duration.millis(20); // Menor duración para mayor fluidez (~50 FPS)

        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps; // Progreso de la animación (0 a 1)

            // Calcula colores intermedios en un ciclo continuo
            String color1 = cycleColor("#000000", "#a56605", progress);
            String color2 = cycleColor("#a56605", "#000000", (progress + 0.5) % 1); // Desfase para suavidad

            // Configura el estilo del pane para este paso
            timeline.getKeyFrames().add(
                    new KeyFrame(
                            duration.multiply(i),
                            e -> pane.setStyle("-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + ");")
                    )
            );
        }

        timeline.setCycleCount(Timeline.INDEFINITE); // Animación infinita
        timeline.play(); // Inicia la animación
    }
    //metodo para generar un ciclo continuo entre dos colores
    /**
     * Método para generar un ciclo continuo entre dos colores
     * @param pane Pane
     */
    public static void animateBackground(TabPane pane) {
        Timeline timeline = new Timeline();
        final int steps = 200; // Más pasos para fluidez
        final Duration duration = Duration.millis(20); // Menor duración para mayor fluidez (~50 FPS)

        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps; // Progreso de la animación (0 a 1)

            // Calcula colores intermedios en un ciclo continuo
            String color1 = cycleColor("#000000", "#a56605", progress);
            String color2 = cycleColor("#a56605", "#000000", (progress + 0.5) % 1); // Desfase para suavidad

            // Configura el estilo del pane para este paso
            timeline.getKeyFrames().add(
                    new KeyFrame(
                            duration.multiply(i),
                            e -> pane.setStyle("-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + ");")
                    )
            );
        }

        timeline.setCycleCount(Timeline.INDEFINITE); // Animación infinita
        timeline.play(); // Inicia la animación
    }

    // Método para generar un ciclo continuo entre dos colores
    /**
     * Método para generar un ciclo continuo entre dos colores
     * @param startColor String
     * @param endColor String
     * @param fraction double
     * @return String
     */
    private static String cycleColor(String startColor, String endColor, double fraction) {
        int r1 = Integer.parseInt(startColor.substring(1, 3), 16);
        int g1 = Integer.parseInt(startColor.substring(3, 5), 16);
        int b1 = Integer.parseInt(startColor.substring(5, 7), 16);

        int r2 = Integer.parseInt(endColor.substring(1, 3), 16);
        int g2 = Integer.parseInt(endColor.substring(3, 5), 16);
        int b2 = Integer.parseInt(endColor.substring(5, 7), 16);

        // Interpolación de colores con un ciclo infinito (usa seno para suavidad)
        double sine = 0.5 * (1 - Math.cos(2 * Math.PI * fraction));
        int r = (int) (r1 + (r2 - r1) * sine);
        int g = (int) (g1 + (g2 - g1) * sine);
        int b = (int) (b1 + (b2 - b1) * sine);

        return String.format("#%02x%02x%02x", r, g, b); // Retorna el color interpolado
    }
}


