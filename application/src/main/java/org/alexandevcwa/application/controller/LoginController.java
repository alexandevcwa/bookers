package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import lombok.Setter;
import org.alexandevcwa.application.BookersApplication;
import org.alexandevcwa.application.controller.util.TextFieldConfiguration;
import org.alexandevcwa.application.model.Usuario;
import org.alexandevcwa.application.model.repository.UsuarioRepository;
import org.alexandevcwa.application.model.repository.UsuarioRepositoryImp;

import java.io.IOException;

public class LoginController {

    private final UsuarioRepositoryImp<Usuario> usuarioRepository;

    @FXML
    private TextField textField_cui;

    @FXML
    private PasswordField passwordField_Contrasenia;

    @FXML
    private Hyperlink hyperlink_registrar;

    @FXML
    private Button button_login;

    @FXML
    private Button button_salir;

    @FXML
    private Label label_mensaje;

    @Setter
    private Stage loginStage;

    public LoginController() {
        this.usuarioRepository = new UsuarioRepository();
    }

    @FXML
    public void initialize() {

        TextFieldConfiguration.textFieldSetRegex(this.textField_cui, "\\d*");
        //TextFieldConfiguration.textFieldFocusLost(this.textField_cui, 13, 13, "El número de CUI debe de contener mínomo 13 dígitos", this.label_mensaje);

        hyperlink_registrar.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(BookersApplication.class.getResource("bookers-registrar.fxml"));
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.initOwner(loginStage);
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.show();
        });

        button_login.setOnAction(event -> {
            if (this.textField_cui.getLength() < 13 && this.passwordField_Contrasenia.getText().length() > 0) {
                this.label_mensaje.setText("Los campos no cumple con el mínimo requerido");
            } else {
                Usuario usuario = usuarioRepository.findByCuiAndPassword(textField_cui.getText(), passwordField_Contrasenia.getText());
                if (usuario != null) {
                    try {
                        openDashboard(usuario);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    label_mensaje.setText("Credenciales Incorrectas");
                }
            }
        });

        button_salir.setOnAction(actionEvent -> {
            System.exit(1);
        });
    }

    private void openDashboard(Usuario usuario) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookersApplication.class.getResource("bookers-dashboard.fxml"));
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setResizable(false);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        DashboardController controller = fxmlLoader.getController();
        controller.setDashboardStage(stage);
        controller.setUsuario(usuario);
        stage.show();
    }
}
