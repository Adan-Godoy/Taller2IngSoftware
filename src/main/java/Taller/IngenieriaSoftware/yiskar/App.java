package Taller.IngenieriaSoftware.yiskar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Taller.IngenieriaSoftware.yiskar.controllers.administrarServiciosController;

import javax.imageio.IIOParam;
import java.io.IOException;

public class App extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        try {
            Image icon = new Image(getClass().getResourceAsStream("images/Yiskar_logo-solo-preview.png"));
            Parent root = FXMLLoader.load(getClass().getResource("views/iniciarSesion.fxml"));
            Scene scene = new Scene(root);
            stage.setMinWidth(1000);
            stage.setMaxWidth(1000);
            stage.setMinHeight(600);
            stage.setMaxHeight(600);
            stage.setTitle("Yiskar");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e){

            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch();
    }
}