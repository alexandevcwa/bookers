module org.alexandevcwa.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;


    opens org.alexandevcwa.application to javafx.fxml;
    exports org.alexandevcwa.application;
    exports org.alexandevcwa.application.model;
    opens org.alexandevcwa.application.model to javafx.fxml;
}