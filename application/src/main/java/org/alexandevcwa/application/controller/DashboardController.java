package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import org.alexandevcwa.application.BookersApplication;
import org.alexandevcwa.application.model.Usuario;

import java.io.IOException;

public class DashboardController {

    @Setter
    private Usuario usuario;

    @Setter
    private Stage dashboardStage;

    @FXML
    private SplitMenuButton splitMenuButton_Opciones;

    public void initialize() {
        configurationOptions();
    }

    private void configurationOptions() {
        MenuItem controlPanel = new MenuItem("Panel de Control");
        MenuItem logout = new MenuItem("Cerrar Sesión");

        splitMenuButton_Opciones.getItems().addAll(controlPanel, logout);

        controlPanel.setOnAction(actionEvent -> {
            try {
                openControlPanel();
            } catch (IOException e) {
                throw new RuntimeException(e);
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setHeaderText(null);
//                alert.setTitle("Error");
//                alert.setContentText(e.getMessage());
//                alert.showAndWait();
//                System.out.println(e.getMessage());
            }
        });
    }

    private void openControlPanel() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookersApplication.class.getResource("bookers-control.fxml"));
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMaximized(false);
        stage.setResizable(false);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initOwner(dashboardStage);
        ControlPanelController controller = fxmlLoader.getController();
        controller.setUsuario(usuario);
        controller.setControlPanelStage(stage);
        stage.show();
    }
}
