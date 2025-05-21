package util;

import javafx.scene.control.Alert;

public class Alertas {

    /**
     * Método estático para mostrar una alerta con título y contenido personalizados.
     * 
     * @param tipo     Tipo de alerta (INFORMATION, ERROR, WARNING, CONFIRMATION)
     * @param titulo   Título de la ventana de alerta
     * @param mensaje  Texto que se mostrará en la alerta
     */
    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);       // Establecer título de la ventana
        alerta.setHeaderText(null);    // Sin encabezado para mayor limpieza visual
        alerta.setContentText(mensaje); // Mensaje que aparece en la alerta
        alerta.showAndWait();          // Mostrar alerta y esperar que el usuario la cierre
    }
}