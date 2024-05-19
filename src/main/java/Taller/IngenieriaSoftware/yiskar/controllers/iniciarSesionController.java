package Taller.IngenieriaSoftware.yiskar.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class iniciarSesionController  {

    public iniciarSesionController() {
    }

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField emailIniciarSesion;

    @FXML
    private PasswordField contraseñaIniciarSesion;

    @FXML
    private Button botonIniciarSesion;

    @FXML
    private Button botonCrearCuenta;


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


}