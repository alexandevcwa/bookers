package org.alexandevcwa.application.controller.component;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.alexandevcwa.application.Inventario;

import java.io.ByteArrayInputStream;


public class LibroCard extends VBox {

    @Getter
    private final Inventario inventarioLibro;

    private ImageView cover;
    private Label titulo;
    private Label inventario;

    @Getter
    private Button agregar;

    public LibroCard(Inventario inventario) {
        this.inventarioLibro = inventario;
        this.cover = new ImageView();
        this.titulo = new Label();
        this.inventario = new Label();
        this.agregar = new Button();
        configurarEstilos();
        configurarComponentes();
    }

    private void configurarComponentes() {

        this.setPadding(new Insets(20, 20, 20, 20));
        this.setStyle("-fx-border-color: gray; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 8;");

        // Portada de Libro
        this.cover.setImage(new Image(new ByteArrayInputStream(inventarioLibro.getLibro().getLbrCover())));
        // Titulo de Libro
        this.titulo.setText(inventarioLibro.getLibro().getLbrTitulo());
        this.titulo.setStyle("-fx-font-size: 14px;");
        this.titulo.setMaxWidth(120);
        this.titulo.setMinHeight(45);
        this.titulo.setWrapText(true);
        // Inventario de Libro
        this.inventario.setText("DISPONIBLES: " + inventarioLibro.getInvDisponible());
        // Botón Agregar
        this.agregar.setText("AGREGAR");
        this.agregar.getStyleClass().add("button-general");
        this.agregar.getStyleClass().add("button-alert");
        this.agregar.setMaxWidth(120);
        this.agregar.setMinWidth(120);
        // Agregar Componentes a VBox
        this.getChildren().addAll(cover, titulo, inventario, agregar);
    }

    private void configurarEstilos() {
        this.cover.setFitHeight(200);
        this.cover.setFitWidth(120);
        this.cover.setPreserveRatio(false);
        this.cover.setSmooth(true);
        this.cover.setCache(true);
        //Rectangle clip = new Rectangle(300,120);
        //this.cover.setClip(clip);
    }
}
