module com.example.oh2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oh2 to javafx.fxml;
    exports com.example.oh2;
}