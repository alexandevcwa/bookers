module org.alexandevcwa.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires java.desktop;


    opens org.alexandevcwa.application to javafx.fxml;
    exports org.alexandevcwa.application;
    exports org.alexandevcwa.application.model;

    opens org.alexandevcwa.application.controller to javafx.fxml;
    opens org.alexandevcwa.application.controller.util to javafx.fxml;
}