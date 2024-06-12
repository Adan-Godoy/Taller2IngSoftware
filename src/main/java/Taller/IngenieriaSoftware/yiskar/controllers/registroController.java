package Taller.IngenieriaSoftware.yiskar.controllers;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import static java.time.Duration.*;

public class registroController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField edadTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private PasswordField RepeatPasswordField;

    @FXML
    private Button crearButton;

    @FXML
    private Button iniciarSesionButton;

    @FXML
    private Label mensajeLabel;

    @FXML
    private Label errorTexto;

    public registroController( ) {
    }

    @FXML
    private void cambiarIniciarSesion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Taller/IngenieriaSoftware/yiskar/views/iniciarSesion.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void registrarUsuario(ActionEvent event)
    {

        if(nombreTextField.getText().isEmpty())
        {
            mostrarError("Debe completar el campo nombre.");
            return;
        }
        if(edadTextField.getText().isEmpty())
        {
            mostrarError("Debe completar el campo edad.");
            return;
        }else
        {
            try
            {
                int edad = Integer.parseInt(edadTextField.getText());
                if(edad < 18 || edad > 65)
                {
                    mostrarError("La edad no puede ser inferior a 18 y mayor a 65.");
                    return;
                }
            }
            catch(Exception e)
            {
                mostrarError("La edad debe ser numérica.");
                return;
            }
        }
        if(emailTextField.getText().isEmpty())
        {
            mostrarError("Debe completar el campo correo electrónico.");
            return;
        }
        else
        {
            validarCorreo(emailTextField.getText());
        }
        if(PasswordField.getText().isEmpty())
        {
            mostrarError("Debe completar el campo contraseña.");
        }
        else
        {

        }
    }

    /***
     * Método que mustra un mensaje de error en pantalla.
     * @param msg Mensaje a mostrar.
     */
    private void mostrarError(String msg)
    {
        errorTexto.setText(msg);
        errorTexto.setVisible(true);

    }

    /***
     * Método que valida que el correo electrónico sea ingresado correctamente.
     * @param email Campo a validar.
     */
    private void validarCorreo(String email)
    {
        String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
        {
            mostrarError("El correo tiene que tener un formato válido.");
        }
    }

    @FXML
    private void verificarContraseniasCoiniciden(KeyEvent event)
    {
        Object evt = event.getSource();
        if(evt.equals(RepeatPasswordField))
        {
            if(!RepeatPasswordField.getText().equals(PasswordField.getText()))
            {
                mostrarError("Las contraseñas deben coincidir.");
            }
        }

    }
}