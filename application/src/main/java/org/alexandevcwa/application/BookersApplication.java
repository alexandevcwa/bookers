package org.alexandevcwa.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.alexandevcwa.application.controller.LoginController;

import java.io.IOException;

public class BookersApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookersApplication.class.getResource("bookers-login.fxml"));
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        LoginController loginController = fxmlLoader.getController();
        loginController.setLoginStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}