module com.example.yiskar {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.yiskar to javafx.fxml;
    exports com.example.yiskar;
}