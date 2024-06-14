package Taller.IngenieriaSoftware.yiskar.controllers;

import Taller.IngenieriaSoftware.yiskar.repository.PersonaRepository;
import Taller.IngenieriaSoftware.yiskar.services.ApiService;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class iniciarSesionController
{
    @FXML
    private TextField emailIniciarSesion;

    @FXML
    private PasswordField contraseñaIniciarSesion;

    @FXML
    private Button botonIniciarSesion;

    @FXML
    private Button botonCrearCuenta;

    /**
     * Método que inicia la ventana de iniciar sesión.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
    @FXML
    private void cambiarRegistro(ActionEvent event)  {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Taller/IngenieriaSoftware/yiskar/views/registro.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Método que permite al usuario iniciar sesión en el sistema y dependiendo de su rol, abre la ventana correspondiente al usuario.
     * @param event Detecta una interacción del usuario con la interfaz.
     */
    @FXML
    private void IniciarSesion(ActionEvent event) {

        if(emailIniciarSesion.getText().isEmpty())
        {
            AlertBox.mostrarError("Debe ingresar su email para iniciar sesión.", "Login fallido", Alert.AlertType.ERROR);
            return;
        }
        if(contraseñaIniciarSesion.getText().isEmpty())
        {
            AlertBox.mostrarError("Debe ingresar su contraseña para iniciar sesión.", "Login fallido", Alert.AlertType.ERROR);
            return;
        }
        PersonaRepository personaRepository = PersonaRepository.getInstance();
        String respRepository = personaRepository.loguearPersona(emailIniciarSesion.getText(),contraseñaIniciarSesion.getText());
        if (respRepository!=null){
            ApiService apiService = ApiService.getInstance();
            try {
                apiService.login("fllanos21", "678234");

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(respRepository.equals("Cliente"))
            {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Taller/IngenieriaSoftware/yiskar/views/comprarGiftCard.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            else
            {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Taller/IngenieriaSoftware/yiskar/views/administrarServicios.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            AlertBox.mostrarError("Usuario no registrado o contraseña incorrecta.", "Login fallido", Alert.AlertType.ERROR);
        }
    }

}