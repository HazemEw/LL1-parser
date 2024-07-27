module com.example.ll1_parsing {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ll1_parsing to javafx.fxml;
    exports com.example.ll1_parsing;
}