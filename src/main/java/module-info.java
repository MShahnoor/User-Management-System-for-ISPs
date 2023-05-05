module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires easytable.master;
    requires java.desktop;


    opens com.example.ISP to javafx.fxml;
    exports com.example.ISP;
}