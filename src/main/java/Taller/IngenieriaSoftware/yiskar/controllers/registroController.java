package Taller.IngenieriaSoftware.yiskar.controllers;

import java.io.*;

import Taller.IngenieriaSoftware.yiskar.repository.PersonaRepository;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.animation.KeyValue;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class registroController {

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

    /**
     * Método que inicia la interfaz de registro de usuario.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
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

    /**
     * Método que permite al usuario poder registrarse en el sistema.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
    @FXML
    private void registrarUsuario(ActionEvent event) {
        if (nombreTextField.getText().isEmpty()) {
            mostrarError("Debe completar el campo nombre.");

            return;
        }
        if (edadTextField.getText().isEmpty()) {
            mostrarError("Debe completar el campo edad.");
            return;
        } else {
            try {
                int edad = Integer.parseInt(edadTextField.getText());
                if (edad < 18 || edad > 65) {
                    mostrarError("La edad no puede ser inferior a 18 y mayor a 65.");
                    edadTextField.clear();
                    return;
                }
            } catch (Exception e) {
                mostrarError("La edad debe ser numérica.");
                edadTextField.clear();
                return;
            }
        }
        if (emailTextField.getText().isEmpty()) {
            mostrarError("Debe completar el campo correo electrónico.");
            return;
        } else {
            String correo = emailTextField.getText();
            if (correoExistente(correo)) {
                mostrarError("El correo electrónico ingresado ya existe en el sistema.");
                return;
            }
            validarCorreo(correo);
            if (errorTexto.isVisible()) {
                return;
            }
        }
        if (PasswordField.getText().isEmpty()) {
            mostrarError("Debe completar el campo contraseña.");
            return;
        }
        if (!PasswordField.getText().equals(RepeatPasswordField.getText())) {
            mostrarError("Las contraseñas ingresadas no coinciden.");
            return;
        }
        if(!AlertBox.pedirConfirmacion())
        {
            nombreTextField.clear();
            edadTextField.clear();
            emailTextField.clear();
            PasswordField.clear();
            RepeatPasswordField.clear();
            errorTexto.setText("");
            return;
        }
        PersonaRepository personaRepository = PersonaRepository.getInstance();
        if(personaRepository.registrarCliente(nombreTextField.getText(),edadTextField.getText(),emailTextField.getText(),PasswordField.getText()))
        {
            AlertBox.mostrarError("usuario registrado con éxito.", "Registro existoso", Alert.AlertType.CONFIRMATION);

            nombreTextField.clear();
            edadTextField.clear();
            emailTextField.clear();
            PasswordField.clear();
            RepeatPasswordField.clear();
            errorTexto.setText("");
        }
        else
        {
            AlertBox.mostrarError("Intente nuevamente con otro correo.", "Registro", Alert.AlertType.WARNING);
            emailTextField.clear();
        }
    }

    /***
     * Método que activa un mensaje de error en pantalla que desaparece luego de 4 segundos.
     * @param msg Texto del mensaje a mostrar.
     */
    private void mostrarError(String msg)
    {
        errorTexto.setText(msg);
        errorTexto.setVisible(true);
        Timeline contador = new Timeline(new KeyFrame(
                Duration.seconds(4),
                acción -> errorTexto.setVisible(false)));
        contador.play();
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


    /**
     * Método que accede al repositorio de personas para verificar que un correo no exista en el sistema.
     * @param correo correo a buscar en el sistema.
     * @return True si existe, false de lo contrario.
     */
    private boolean correoExistente(String correo) {
        PersonaRepository personaRepository = PersonaRepository.getInstance();
        return personaRepository.buscarEmail(correo);
    }
}