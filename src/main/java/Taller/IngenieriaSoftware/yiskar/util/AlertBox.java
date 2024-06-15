package Taller.IngenieriaSoftware.yiskar.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.Optional;

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

        Label label = (Label) alert.getDialogPane().lookup(".content.label");
        label.setTextFill(Color.web("#F56558"));

        alert.showAndWait();
    }

    /**
     * Método que inicia un cuadro de diálogo donde solicita la confirmación de una acción por medio de un botón "SÍ" y otro "NO".
     * @return True si se selecciona sí, False si se selecciona no o se cierra el cuadro.
     */
    public static boolean pedirConfirmacion()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro?");

        ButtonType buttonSi = new ButtonType("Sí");
        ButtonType buttonNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonSi, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonSi;
    }
}
