package Taller.IngenieriaSoftware.yiskar.util;

import javafx.scene.control.Alert;

public class AlertBox {

    /**
     * Método que muestra un cuadro de mensaje al usuario.
     * @param mensaje Mensaje del cuadro.
     * @param titulo Título del cuadro.
     * @param tipoAlerta Tipo de mensaje.
     */
    public static void mostrarError(String mensaje, String titulo, Alert.AlertType tipoAlerta)
    {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
