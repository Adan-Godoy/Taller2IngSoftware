module com.example.yiskar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;


    opens Taller.IngenieriaSoftware.yiskar to javafx.fxml;
    exports Taller.IngenieriaSoftware.yiskar;
    exports Taller.IngenieriaSoftware.yiskar.controllers;
    opens Taller.IngenieriaSoftware.yiskar.controllers to javafx.fxml;
}