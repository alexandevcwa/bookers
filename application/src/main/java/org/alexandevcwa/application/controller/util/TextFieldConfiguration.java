package org.alexandevcwa.application.controller.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TextFieldConfiguration {

    /**
     * Configuración para verificación de TextField cuando pierda el Focus
     *
     * @param textField TextField a configurar
     * @param minLength Mínimo de dígitos que debe de contener el TextField
     * @param maxLength Máximo de dígitos que debe de contener el TextField
     * @param message   Mensaje que asigna al Label en caso de que las condiciones mínimas y máximas de dígitos no se cumplan
     * @param label     Componente Label al que se le asignara el mensaje
     */
    public static void textFieldFocusLost(TextField textField, Integer minLength, Integer maxLength, String message, Label label) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (textField.getText().length() < minLength || textField.getText().length() > maxLength) {
                    textField.requestFocus();
                    label.setText(null);
                    label.setText(message);
                } else {
                    label.setText(null);
                }
            }
        });
    }

    /**
     * Configuración para verificación de TextField cuando pierda el Focus
     *
     * @param textField  TextField a configurar
     * @param minLength  Mínimo de dígitos que debe de contener el TextField
     * @param maxLength  Máximo de dígitos que debe de contener el TextField
     * @param message    Mensaje que asigna al Label en caso de que las condiciones mínimas y máximas de dígitos no se cumplan
     * @param label      Componente Label al que se le asignara el mensaje
     * @param methodName Nombre del método del objeto al que se le asignara el valor contenido en el TextField
     * @param object     Objeto al que se le asignaran los valores
     * @param <T>        Genérico del tipo de Objeto de que manejara
     */
    public static <T> void textFieldFocusLost(TextField textField, Integer minLength, Integer maxLength, String message, Label label, String methodName, T object) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (textField.getText().length() < minLength || textField.getText().length() > maxLength) {
                    textField.requestFocus();
                    label.setText(null);
                    label.setText(message);
                } else {
                    label.setText(null);
                    setValue(object, methodName, textField.getText());
                }
            }
        });
    }

    /**
     * Configuración para comparación del contenido de 2 TextFields
     *
     * @param textField_main     TextField principal
     * @param textField_compared TextField con contenido a comprar con el TextField principal
     * @param message            Mensaje en caso de que la comprobación no sea correcto
     * @param label              Label al que se le asignara el mensaje en caso de que la comprobación no sea correcta
     */
    public static void textFieldCompareFields(TextField textField_main, TextField textField_compared, String message, Label label) {
        textField_compared.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!Objects.equals(textField_main.getText(), textField_compared.getText())) {
                    label.setText(null);
                    label.setText(message);
                } else {
                    label.setText(null);
                }
            }
        });
    }

    /**
     * Configuración para comparación del contenido de 2 TextFields
     *
     * @param textField_main     TextField principal
     * @param textField_compared TextField con contenido a comprar con el TextField principal
     * @param message            Mensaje en caso de que la comprobación no sea correcto
     * @param label              Label al que se le asignara el mensaje en caso de que la comprobación no sea correcta
     * @param methodName         Nombre del método del objeto al que se le asignara el valor del TextField
     * @param object             Objeto al que se le asignaran los valores
     * @param <T>                Genérico del tipo de Objeto de que manejara
     */
    public static <T> void textFieldCompareFields(TextField textField_main, TextField textField_compared, String message, Label label, String methodName, T object) {
        textField_compared.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!Objects.equals(textField_main.getText(), textField_compared.getText())) {
                    label.setText(null);
                    label.setText(message);
                } else {
                    label.setText(null);
                    setValue(object, methodName, textField_main.getText());
                }
            }
        });

    }

    /**
     * Convertir todas las letras de un TextField en mayúsculas
     *
     * @param textField TextField a modificar su contenido
     */
    public static void textFieldUpperCase(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(newValue.toUpperCase());
        });
    }

    /**
     * Configurar un REGEX para restringir los tipos de caracteres a ingresar en un TextField
     *
     * @param textField TextField al que se asignara un patrón para los caracteres permitidos
     * @param regex     Patron de caracteres permitidos
     */
    public static void textFieldSetRegex(TextField textField, String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final UnaryOperator<TextFormatter.Change> filter;
        filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void textFieldVerifyContentWithRegex(TextField textField, String regex, Label label, String message) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Si el campo pierde el foco
                String content = textField.getText();
                if (!content.matches(regex)) {
                    label.setText(null);
                    label.setText(message);
                    textField.requestFocus();
                } else {
                    label.setText(null);
                }

            }
        });
    }

    /**
     * @param object     Objeto al que se le van a asignar valores
     * @param methodName Nombre del método que se usará el valor
     * @param value      Valor a asignar
     * @param <T>        Genérico del tipo de objeto al que se le asignaran los valores
     * @param <R>        Genérico del tipo de objeto que se va a asignar
     */
    private static <T, R> void setValue(T object, String methodName, R value) {
        try {
            Method method = object.getClass().getMethod(methodName, value.getClass());
            method.invoke(object, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
