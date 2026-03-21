package com.example.soleclipsado;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.text.Normalizer;
import java.util.*;

/**
 * Controlador de la vista final {@code VistaFinal.fxml}.
 * <p>
 * Se encarga de mostrar el resultado de la partida (victoria o derrota),
 * actualizar la imagen del sol al estado en que quedó al terminar el juego
 * y permitir al jugador iniciar una nueva partida regresando a la vista inicial.
 * </p>
 *
 * @author Jeronimo Rojas 2021517760-3743
 * @author Luis Felipe Velasco Chilito 2517245-3743
 * @version 1.0.4
 * @see PlayController
 * @see HelloController
 */
public class FinalController {

    /** Panel raíz de la vista final; usado para obtener el {@link Stage} activo. */
    @FXML
    private AnchorPane RootPane;

    /** Etiqueta que muestra el mensaje de resultado (victoria o derrota). */
    @FXML
    private Label TextoFinal;

    /** Vista de imagen que muestra el estado final del sol eclipsado. */
    @FXML
    private ImageView imageSol;

    /**
     * Maneja el evento del botón "Jugar de nuevo".
     * <p>
     * Carga la vista inicial {@code VistaPalabra.fxml} y reemplaza la escena
     * actual con una nueva partida, reiniciando el flujo del juego desde el
     * ingreso de la palabra secreta.
     * </p>
     *
     * @throws IOException si el archivo {@code VistaPalabra.fxml} no puede cargarse.
     */
    @FXML
    protected void onHelloButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaPalabra.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) RootPane.getScene().getWindow();
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Actualiza el texto de la etiqueta para indicar que el jugador perdió.
     * <p>
     * Muestra el mensaje "Intentos terminados\nPerdiste" en {@link #TextoFinal}.
     * Este método es invocado por {@link PlayController#CambiarVistaFinal()} cuando
     * el sol queda completamente eclipsado (6 errores).
     * </p>
     */
    public void cambiarLabelPerdedor() {
        TextoFinal.setText("Intentos terminados\n" + "Perdiste");
    }

    /**
     * Actualiza el texto de la etiqueta para indicar que el jugador ganó.
     * <p>
     * Muestra el mensaje "Felicitaciones\nAdivinaste la palabra" en {@link #TextoFinal}.
     * Este método es invocado por {@link PlayController#CambiarVistaFinal()} cuando
     * el jugador completa la palabra antes de agotar los intentos.
     * </p>
     */
    public void cambiarLabelGanador() {
        TextoFinal.setText("Felicitaciones\n" + "Adivinaste la palabra");
    }

    /**
     * Establece la imagen del sol en el estado final de la partida.
     * <p>
     * Carga el recurso de imagen {@code Sol_N.png} correspondiente al estado
     * en que quedó el sol al terminar la partida, donde N va de 1 (sol completo)
     * a 6 (sol completamente eclipsado).
     * </p>
     *
     * @param EstadoSolEclipaso el índice de la imagen del sol (entre 1 y 6 inclusive).
     */
    public void setImageSol(int EstadoSolEclipaso) {
        imageSol.setImage(new Image(getClass().getResource(
                "/com/example/soleclipsado/IMAGENES/Sol_" + EstadoSolEclipaso + ".png").toExternalForm()));
    }
}
