module com.example.soleclipsado {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.soleclipsado to javafx.fxml;
    exports com.example.soleclipsado;
}