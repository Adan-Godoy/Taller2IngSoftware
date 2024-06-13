package Taller.IngenieriaSoftware.yiskar.util;

import javafx.scene.control.Alert;

public class AlertBox {

    public static void mostrarError(String mensaje, String titulo, Alert.AlertType tipoAlerta)
    {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
