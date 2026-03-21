package com.example.soleclipsado;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador de la vista inicial {@code VistaPalabra.fxml}.
 * <p>
 * Gestiona la pantalla en la que el primer jugador ingresa la palabra secreta
 * que el segundo jugador deberá adivinar. Se encarga de validar que la palabra
 * cumpla con las restricciones de longitud y de caracteres, normalizarla
 * (eliminar tildes y convertir a minúsculas) y traspasar el control a
 * {@link PlayController} para iniciar el juego.
 * </p>
 *
 * @author Jeronimo Rojas 2021517760-3743
 * @version 1.0.4
 * @see PlayController
 */
public class HelloController {

    /** Etiqueta que muestra mensajes de advertencia o retroalimentación al usuario. */
    @FXML
    private Label welcomeText;

    /** Campo de texto donde el primer jugador escribe la palabra secreta. */
    @FXML
    private TextField palabraField;

    /** Referencia al escenario principal para cambiar de escena. */
    private Stage stage;

    /** Escena que se cargará al pasar a la vista de juego. */
    private Scene scene;

    /** Nodo raíz de la siguiente vista cargada desde FXML. */
    private Parent root;

    /**
     * Maneja el evento del botón de confirmación de la palabra secreta.
     * <p>
     * Valida que la palabra no contenga caracteres especiales ni espacios
     * ({@link #VerificarCaracteresEspeciales()}) y que su longitud esté entre
     * 6 y 12 caracteres ({@link #VerificarLongitudPalabra()}). Si ambas
     * validaciones pasan, obtiene la palabra normalizada, carga la vista
     * {@code VistaAdivinarPalabra.fxml}, configura el {@link PlayController}
     * con la palabra secreta y cambia la escena activa.
     * </p>
     *
     * @throws IOException si el archivo FXML de la siguiente vista no puede cargarse.
     */
    @FXML
    protected void onHelloButtonClick() throws IOException {
        if (VerificarCaracteresEspeciales() && VerificarLongitudPalabra()) {
            String PalabraSecreta = ObtenerPalabraVerificada();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaAdivinarPalabra.fxml"));
            root = fxmlLoader.load();
            PlayController playController = fxmlLoader.getController();

            playController.PalabraSecreta = PalabraSecreta;
            playController.CrearCamposdeTextoEnInterfaz();
            playController.AsignarSetOnKeyTypedACamposDeTexto();

            stage = (Stage) palabraField.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Verifica que la palabra ingresada tenga entre 6 y 12 caracteres.
     * <p>
     * Si la longitud no cumple el requisito, muestra un aviso en
     * {@code welcomeText} y limpia el campo de texto.
     * </p>
     *
     * @return {@code true} si la longitud está en el rango válido [6, 12];
     *         {@code false} en caso contrario.
     */
    protected boolean VerificarLongitudPalabra() {
        Integer GuardarLongitudPalabra = palabraField.getLength();
        if (GuardarLongitudPalabra < 6 || GuardarLongitudPalabra > 12) {
            welcomeText.setText("La palabra debe ser entre 6 y 12 letras");
            palabraField.setText("");
            return false;
        }
        return true;
    }

    /**
     * Verifica que la palabra ingresada no contenga espacios ni caracteres especiales.
     * <p>
     * Usa la expresión regular {@code \\p{L}+} que acepta únicamente letras Unicode
     * (incluyendo tildes y ñ), rechazando números, espacios y símbolos.
     * Si la validación falla, muestra un aviso y limpia el campo de texto.
     * </p>
     *
     * <p><b>Detalle de la regex {@code \\p{L}+}:</b></p>
     * <ul>
     *   <li>{@code .matches()} — evalúa que <em>todo</em> el texto cumpla la regla.</li>
     *   <li>{@code \\p} — propiedad de Unicode.</li>
     *   <li>{@code L} — letra (Letter), incluye tildes y ñ; rechaza espacios y números.</li>
     *   <li>{@code +} — uno o más caracteres (no acepta cadena vacía).</li>
     * </ul>
     *
     * @return {@code true} si la palabra contiene únicamente letras;
     *         {@code false} si contiene espacios, números u otros caracteres.
     */
    protected boolean VerificarCaracteresEspeciales() {
        String GuardarPalabra = palabraField.getText();

        /*
         * .matches("\\p{L}+") valida que TODA la cadena sea letras Unicode:
         * \\ -> doble barra para enviar un solo '\' al motor de Regex.
         * p  -> propiedad (Property) de Unicode.
         * L  -> Letra (Letter): incluye tildes y ñ; rechaza espacios y números.
         * +  -> uno o más; obliga a leer la palabra completa.
         */
        if (!GuardarPalabra.matches("\\p{L}+")) {
            welcomeText.setText("La palabra no puede tener espacios ni caracteres especiales");
            palabraField.setText("");
            return false;
        }
        return true;
    }

    /**
     * Normaliza y retorna la palabra ingresada por el usuario.
     * <p>
     * Aplica dos transformaciones para estandarizar la cadena:
     * </p>
     * <ol>
     *   <li>{@link Normalizer#normalize(CharSequence, Normalizer.Form)} con
     *       {@code Form.NFD}: descompone cada letra acentuada en su letra base
     *       más la marca diacrítica (p. ej. {@code 'á'} → {@code 'a' + '´'}).</li>
     *   <li>{@code replaceAll("\\p{M}", "")} elimina las marcas diacríticas sueltas
     *       que generó el paso anterior.</li>
     *   <li>{@code toLowerCase(Locale.ROOT)} convierte a minúsculas de forma
     *       independiente al idioma del sistema operativo.</li>
     * </ol>
     *
     * @return la palabra en minúsculas y sin tildes ni diacríticos.
     */
    protected String ObtenerPalabraVerificada() {
        String GuardarPalabra = palabraField.getText();

        /*
         * Normalizer.normalize con Form.NFD separa letras de sus tildes.
         * Ejemplo: 'á' -> 'a' + acento combinado.
         */
        String Normalizar = Normalizer.normalize(GuardarPalabra, Normalizer.Form.NFD);

        /*
         * \p{M} captura las "Marks" (marcas combinadas) sueltas del NFD.
         * Se eliminan con replaceAll y luego se convierte a minúsculas.
         * Locale.ROOT garantiza independencia del idioma del sistema.
         */
        Normalizar = Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        return Normalizar;
    }
}
