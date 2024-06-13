package Taller.IngenieriaSoftware.yiskar.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
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

    @FXML
    private void botonIniciarSesion(ActionEvent event) {

        if (cuentaExistente(emailIniciarSesion.getText(),contraseñaIniciarSesion.getText())){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Taller/IngenieriaSoftware/yiskar/views/administrarServicios.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No es posible iniciar sesion");
            alert.setHeaderText(null);
            alert.setContentText("Usuario no se encuentra registrado");
            alert.showAndWait();

            System.out.println("Usuario no se encuentra registrado");
        }

    }

    private boolean cuentaExistente(String correo, String contrasena) {
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\main\\resources\\Taller\\IngenieriaSoftware\\yiskar\\Data\\Cliente.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(",");
                if (datos.length > 3 && datos[2].equals(correo) && datos[3].equals(contrasena)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}