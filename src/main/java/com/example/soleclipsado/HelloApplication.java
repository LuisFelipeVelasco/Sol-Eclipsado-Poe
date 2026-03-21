package com.example.soleclipsado;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación JavaFX "Sol Eclipsado".
 * <p>
 * Extiende {@link Application} y sirve como punto de entrada de la
 * interfaz gráfica. Carga la vista inicial ({@code VistaPalabra.fxml})
 * y configura la ventana principal del juego.
 * </p>
 *
 * @author Jeronimo Rojas 2021517760-3743
 * @author Luis Felipe Velasco Chilito 2517245-3743
 * @version 1.0.4
 * @see Launcher
 */
public class HelloApplication extends Application {

    /**
     * Método de inicio del ciclo de vida de JavaFX.
     * <p>
     * Carga el archivo FXML {@code VistaPalabra.fxml}, crea una escena de
     * 400×400 píxeles, establece el título de la ventana a "Sol eclipsado",
     * deshabilita el redimensionamiento y muestra la ventana al usuario.
     * </p>
     *
     * @param stage el escenario principal proporcionado por el entorno de JavaFX.
     * @throws IOException si el archivo FXML no puede ser cargado correctamente.
     */
    @Override
    public void start(Stage stage) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VistaPalabra.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle("Sol eclipsado");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
