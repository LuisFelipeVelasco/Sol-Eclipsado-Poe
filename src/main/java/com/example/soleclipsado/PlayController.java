package com.example.soleclipsado;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

/**
 * Controlador de la vista de juego {@code VistaAdivinarPalabra.fxml}.
 * <p>
 * Gestiona toda la lógica del juego "Sol Eclipsado": crea dinámicamente
 * los campos de texto según la longitud de la palabra secreta, valida cada
 * letra ingresada por el jugador, actualiza la imagen del sol a medida que
 * el jugador comete errores y determina cuándo la partida termina (victoria
 * o derrota), transfiriendo el control a {@link FinalController}.
 * </p>
 *
 * @author Luis Felipe Velasco Chilito 2517245-3743
 * @version 1.0.4
 * @see HelloController
 * @see FinalController
 */
public class PlayController {

    /** Contenedor horizontal que aloja los {@link TextField} de cada letra. */
    @FXML
    private HBox hbox;

    /** Panel raíz de la vista; usado para obtener el {@link Stage} activo. */
    @FXML
    private AnchorPane AnchorRoot;

    /** Vista de imagen que muestra el estado actual del sol eclipsado. */
    @FXML
    private ImageView imageSol;

    /** Etiqueta que muestra mensajes de advertencia o retroalimentación al jugador. */
    @FXML
    private Label AdvertenciaText;

    /** Palabra secreta que el jugador debe adivinar (sin tildes y en minúsculas). */
    public String PalabraSecreta;

    /** Lista ordenada de los {@link TextField} generados para cada letra de la palabra. */
    private List<TextField> textFields = new ArrayList<>();

    /** Número de pistas utilizadas en la partida actual. */
    int ContadorPistasDadas = 0;

    /** Resultado del último intento del jugador: {@code true} si acertó, {@code false} si falló. */
    boolean valorDeIntento;

    /**
     * Índice de la imagen del sol actualmente mostrada (1 = sol completo, 6 = eclipsado).
     * Se incrementa con cada error del jugador.
     */
    int ContadorEstadoImagenSolEclipsado = 1;

    /** Cantidad de letras adivinadas correctamente hasta el momento. */
    int ContadorLetrasAcertadas = 0;

    /** Número máximo de pistas que el jugador puede solicitar durante la partida. */
    int NumeroMaximoPistasDadas = 3;

    /**
     * Maneja el evento del botón "Pista".
     * <p>
     * Si aún no se ha alcanzado el límite de pistas ({@link #NumeroMaximoPistasDadas}),
     * incrementa el contador, busca el primer {@link TextField} vacío de izquierda a
     * derecha y lo rellena con la letra correcta mediante {@link #PistaAgregarLetra(TextField)}.
     * Tras asignar la pista, verifica si la palabra quedó completa y cambia a la
     * vista final si corresponde.
     * Si ya se usaron todas las pistas, muestra un mensaje de aviso en rojo.
     * </p>
     */
    @FXML
    protected void onActionButtonClicked() {
        if (ContadorPistasDadas < NumeroMaximoPistasDadas) {
            ContadorPistasDadas += 1;
            for (TextField textField : textFields) {
                if (textField.getText().isEmpty()) {
                    PistaAgregarLetra(textField);
                    break;
                }
            }
            try {
                CambiarVistaFinal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            DiseñoLabelText(AdvertenciaText, "No puedes usar mas pistas", "red");
        }
    }

    /**
     * Crea dinámicamente los campos de texto en la interfaz según la longitud de la palabra secreta.
     * <p>
     * Por cada carácter de {@link #PalabraSecreta} se instancia un {@link TextField}
     * estilizado (mediante {@link #GetTextField()}), se agrega a la lista interna
     * {@code textFields} y al {@link HBox} de la vista.
     * </p>
     */
    protected void CrearCamposdeTextoEnInterfaz() {
        Integer GuardarLongitudPalabraSecreta = PalabraSecreta.length();
        for (Integer i = 0; i < GuardarLongitudPalabraSecreta; i++) {
            TextField textField = GetTextField();
            textFields.add(textField);
            hbox.getChildren().add(textField);
        }
    }

    /**
     * Crea y devuelve un {@link TextField} con el estilo visual del juego.
     * <p>
     * El campo se configura con fuente Arial en negrita, alineación centrada,
     * borde oscuro redondeado y altura fija de 40 px para mantener una
     * apariencia uniforme en todos los campos de la palabra.
     * </p>
     *
     * @return un nuevo {@link TextField} con los estilos predefinidos del juego.
     */
    protected TextField GetTextField() {
        TextField textField = new TextField();
        textField.setStyle(
                "-fx-font-size: 15; " + "-fx-font-family: 'Arial'; " + "-fx-font-weight: bold; " +
                "-fx-alignment: center; " + "-fx-border-color: #34495e; " + "-fx-border-width: 1; " +
                "-fx-border-radius: 2;" + "-fx-min-height: 40; " +
                "-fx-max-height: 40; " + "-fx-pref-height: 40; "
        );
        return textField;
    }

    /**
     * Registra un listener de cambio de texto ({@code textProperty}) en cada campo de texto.
     * <p>
     * Cuando el contenido de un {@link TextField} cambia, se invoca
     * {@link #ControladorCampoDeTexto(TextField, String)} para procesar y validar
     * la letra ingresada por el jugador.
     * </p>
     */
    protected void AsignarSetOnKeyTypedACamposDeTexto() {
        for (TextField textField : textFields) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                ControladorCampoDeTexto(textField, newValue);
            });
        }
    }

    /**
     * Procesa la entrada del jugador en un campo de texto específico.
     * <p>
     * Pasos que realiza:
     * </p>
     * <ol>
     *   <li>Ignora el evento si la cadena nueva está vacía (p. ej. al borrar).</li>
     *   <li>Normaliza la letra: descompone diacríticos con NFD, elimina marcas
     *       ({@code \\p{M}}) y convierte a minúsculas con {@code Locale.ROOT}.</li>
     *   <li>Valida que sea una letra Unicode pura ({@link #IngresoDeSoloLetrasEnCampoDeTexto}).</li>
     *   <li>Si es válida, verifica si coincide con la letra de la posición correspondiente
     *       ({@link #VerificarLetraIngresadaUsuarioCoincideEnPalabraSecreta}).</li>
     *   <li>Actualiza la imagen del sol si hubo error ({@link #CambiarSolEclipsado}).</li>
     *   <li>Comprueba si la partida terminó ({@link #CambiarVistaFinal}).</li>
     * </ol>
     *
     * @param textField            el campo de texto que disparó el evento.
     * @param LetraIngresadaUsuario el texto nuevo ingresado por el jugador.
     */
    protected void ControladorCampoDeTexto(TextField textField, String LetraIngresadaUsuario) {
        if (LetraIngresadaUsuario.isEmpty()) return;

        /*
         * Normalización de la letra ingresada:
         * NFD separa la letra de sus diacríticos (é -> e + acento).
         * \p{M} elimina las marcas combinadas resultantes.
         * toLowerCase(Locale.ROOT) convierte a minúsculas de forma universal.
         */
        LetraIngresadaUsuario = Normalizer.normalize(LetraIngresadaUsuario, Normalizer.Form.NFD);
        LetraIngresadaUsuario = LetraIngresadaUsuario.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        System.out.println(LetraIngresadaUsuario);

        if (IngresoDeSoloLetrasEnCampoDeTexto(textField, LetraIngresadaUsuario)) {
            valorDeIntento = VerificarLetraIngresadaUsuarioCoincideEnPalabraSecreta(textField, LetraIngresadaUsuario);
            CambiarSolEclipsado();
            try {
                CambiarVistaFinal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Valida que el texto ingresado en un campo de texto sea únicamente una letra Unicode.
     * <p>
     * Si el texto contiene números, símbolos u otros caracteres no permitidos,
     * muestra un aviso en rojo y limpia el campo.
     * Usa {@code \\p{L}+} para aceptar letras de cualquier alfabeto (incluye ñ, tildes, etc.).
     * </p>
     *
     * @param textField            el campo de texto que se está evaluando.
     * @param LetraIngresadaUsuario la cadena que ingresó el jugador (ya normalizada).
     * @return {@code true} si el texto es una letra válida; {@code false} si contiene
     *         caracteres no permitidos.
     */
    protected boolean IngresoDeSoloLetrasEnCampoDeTexto(TextField textField, String LetraIngresadaUsuario) {
        if (!LetraIngresadaUsuario.matches("\\p{L}+") && !LetraIngresadaUsuario.isEmpty()) {
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: red;");
            AdvertenciaText.setText("Ojo..  nada de numeros o caracteres especiales");
            textField.setText("");
            return false;
        }
        return true;
    }

    /**
     * Compara la letra ingresada por el jugador con la letra correcta en esa posición.
     * <p>
     * Si coinciden: muestra retroalimentación positiva en verde, deshabilita el campo
     * (ya no es editable) e incrementa {@link #ContadorLetrasAcertadas}.
     * Si no coinciden: muestra retroalimentación negativa en rojo y limpia el campo
     * para que el jugador pueda intentarlo de nuevo.
     * </p>
     *
     * @param textField            el campo de texto cuya posición se está evaluando.
     * @param LetraIngresadaUsuario la letra que ingresó el jugador (normalizada).
     * @return {@code true} si la letra es correcta; {@code false} si es incorrecta
     *         o si el campo quedó vacío tras la validación.
     */
    protected boolean VerificarLetraIngresadaUsuarioCoincideEnPalabraSecreta(TextField textField, String LetraIngresadaUsuario) {
        String LetraCorrecta = LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(textField);
        if (Objects.equals(LetraCorrecta, LetraIngresadaUsuario)) {
            DiseñoLabelText(AdvertenciaText, "Bien ahi, le atinaste", "green");
            textField.setDisable(true);
            ContadorLetrasAcertadas += 1;
            return true;
        } else if (!LetraCorrecta.equalsIgnoreCase(LetraIngresadaUsuario) && !LetraIngresadaUsuario.isEmpty()) {
            DiseñoLabelText(AdvertenciaText, "Nope,Esa no es la letra", "red");
            textField.setText("");
            return false;
        }
        return true;
    }

    /**
     * Rellena un campo de texto con la letra correcta de la palabra secreta (modo pista).
     * <p>
     * Obtiene la letra que corresponde a la posición del {@code textField} dentro de la
     * palabra secreta y la escribe en dicho campo. El listener de cambio de texto
     * se disparará automáticamente, completando la validación.
     * </p>
     *
     * @param textField el campo de texto vacío al que se le asignará la letra correcta.
     */
    protected void PistaAgregarLetra(TextField textField) {
        String LetraPalabraSecreta = LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(textField);
        textField.setText(LetraPalabraSecreta);
    }

    /**
     * Avanza la imagen del sol hacia el estado eclipsado cuando el jugador comete un error.
     * <p>
     * Solo actualiza la imagen si el último intento fue incorrecto
     * ({@code valorDeIntento == false}) y si el contador de errores aún no llegó al
     * estado máximo (imagen 5, antes de quedar completamente eclipsado en imagen 6).
     * Los recursos de imagen siguen el patrón {@code Sol_N.png} donde N va de 1 a 6.
     * </p>
     */
    protected void CambiarSolEclipsado() {
        if (!valorDeIntento && ContadorEstadoImagenSolEclipsado <= 5) {
            ContadorEstadoImagenSolEclipsado += 1;
            imageSol.setImage(new Image(getClass().getResource(
                    "/com/example/soleclipsado/IMAGENES/Sol_" + ContadorEstadoImagenSolEclipsado + ".png")
                    .toExternalForm()));
        }
    }

    /**
     * Evalúa si la partida terminó y, de ser así, carga la vista final.
     * <p>
     * La partida termina si:
     * </p>
     * <ul>
     *   <li><b>Derrota:</b> {@link #ContadorEstadoImagenSolEclipsado} es igual a 6
     *       (sol completamente eclipsado, se agotaron los intentos).</li>
     *   <li><b>Victoria:</b> el sol aún no está completamente eclipsado y el jugador
     *       adivinó todas las letras ({@link #ValidarJugadorAcertoTodasLasLetras()}).</li>
     * </ul>
     * <p>
     * Si alguna condición se cumple, carga {@code VistaFinal.fxml}, configura la imagen
     * del sol y el mensaje correspondiente en {@link FinalController}, y cambia la escena.
     * </p>
     *
     * @throws IOException si el archivo {@code VistaFinal.fxml} no puede cargarse.
     */
    protected void CambiarVistaFinal() throws IOException {
        boolean JugadorPierde = (ContadorEstadoImagenSolEclipsado == 6);
        boolean JugadorGana = (ContadorEstadoImagenSolEclipsado < 6 && ValidarJugadorAcertoTodasLasLetras());

        if (JugadorPierde || JugadorGana) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaFinal.fxml"));
            Parent root = fxmlLoader.load();
            FinalController finalController = fxmlLoader.getController();

            finalController.setImageSol(ContadorEstadoImagenSolEclipsado);
            if (JugadorPierde) finalController.cambiarLabelPerdedor();
            else finalController.cambiarLabelGanador();

            Stage stage = (Stage) AnchorRoot.getScene().getWindow();
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Determina si el jugador adivinó todas las letras de la palabra secreta.
     * <p>
     * Compara {@link #ContadorLetrasAcertadas} con el número total de caracteres
     * en {@link #PalabraSecreta}. El método devuelve {@code true} únicamente cuando
     * ambos valores son iguales, indicando que cada posición fue resuelta correctamente.
     * </p>
     *
     * @return {@code true} si el jugador completó la palabra; {@code false} en caso contrario.
     */
    protected boolean ValidarJugadorAcertoTodasLasLetras() {
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split(""));
        return ContadorLetrasAcertadas == ListaLetrasPalabraSecreta.size();
    }

    /**
     * Retorna la letra de la palabra secreta que corresponde a la posición del campo de texto dado.
     * <p>
     * Divide {@link #PalabraSecreta} carácter a carácter, obtiene el índice del
     * {@code textField} dentro de {@link #textFields} y devuelve el elemento en esa posición.
     * </p>
     *
     * @param textField el campo de texto cuya posición se quiere consultar.
     * @return la letra (como {@code String}) de la palabra secreta en la posición correspondiente.
     */
    protected String LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(TextField textField) {
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split(""));
        int PosicionCampodeTexto = textFields.indexOf(textField);
        return ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);
    }

    /**
     * Aplica un estilo de color y texto a una etiqueta de la interfaz.
     * <p>
     * Agrega la propiedad CSS {@code -fx-text-fill} al estilo existente del
     * {@link Label} para cambiar el color del texto, y asigna el mensaje indicado.
     * </p>
     *
     * @param label   el {@link Label} al que se le aplicará el nuevo estilo y mensaje.
     * @param Mensaje el texto que se mostrará en la etiqueta.
     * @param Color   el color CSS del texto (p. ej. {@code "red"}, {@code "green"}).
     */
    protected void DiseñoLabelText(Label label, String Mensaje, String Color) {
        label.setStyle(label.getStyle() + "-fx-text-fill:" + Color + ";");
        label.setText(Mensaje);
    }
}
