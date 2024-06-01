package org.alexandevcwa.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import org.alexandevcwa.application.BookersApplication;
import org.alexandevcwa.application.controller.component.LibroCard;
import org.alexandevcwa.application.model.Libro;
import org.alexandevcwa.application.model.Usuario;
import org.alexandevcwa.application.model.repository.InventarioRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @Setter
    private Usuario usuario;

    @Setter
    private Stage dashboardStage;

    @FXML
    private SplitMenuButton splitMenuButton_Opciones;

    @FXML
    private FlowPane flowPane_LibrosCards;

    @FXML
    private Button button_Buscar;

    @FXML
    private TextField textField_Texto;

    @FXML
    private Button button_Carrito;

    /**
     * Lista de libros agregados al carrito
     * */
    private List<Libro> carrito = new ArrayList<>();

    private InventarioRepository inventarioRepository;

    public DashboardController() {
        this.inventarioRepository = new InventarioRepository();
    }

    public void initialize() {
        configurationOptions();
        var librosInventario = inventarioRepository.findAllWithCover();
        for (var libroInventario : librosInventario) {
            LibroCard libroCard = new LibroCard(libroInventario);
            libroCard.getAgregar().setOnAction(actionEvent -> {
                Libro libro = libroCard.getInventarioLibro().getLibro();
                carrito.add(libro);
                button_Carrito.setText(String.format("CARRITO (%s)", carrito.size()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Carrito");
                alert.setHeaderText(null);
                alert.setContentText("Nuevo libro se ha agregado a tu carrito");
                alert.showAndWait();
            });
            flowPane_LibrosCards.getChildren().add(libroCard);

        }
        button_Buscar.setOnAction(event -> {
            String textoToFind = textField_Texto.getText();
            var inventarios  = inventarioRepository.findAllByTittleRegex(textoToFind);
            if (inventarios != null){
                flowPane_LibrosCards.getChildren().clear();
                for (var libroInventario : inventarios) {
                    LibroCard libroCard = new LibroCard(libroInventario);
                    flowPane_LibrosCards.getChildren().add(libroCard);
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("No se encontraron libros relacionados con ese nombre");
                alert.showAndWait();
            }

        });

        button_Carrito.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(BookersApplication.class.getResource("bookers-detalle-carrito.fxml"));
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setMaximized(false);
            stage.setResizable(false);
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            stage.initOwner(dashboardStage);
            DetallePedidoController controller = fxmlLoader.getController();
            controller.customInitialize(carrito,usuario);
            //controller.setControlPanelStage(stage);
            stage.showAndWait();
        });
    }

    private void configurationOptions() {
        MenuItem maestroLibros = new MenuItem("Maestro de Libros");
        MenuItem maestroUsuarios = new MenuItem("Maestro de Usuarios");
        MenuItem logout = new MenuItem("Cerrar Sesión");

        splitMenuButton_Opciones.getItems().clear();
        splitMenuButton_Opciones.getItems().addAll(maestroLibros, maestroUsuarios, logout);

        maestroLibros.setOnAction(actionEvent -> {
            try {
                openControlPanel();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
