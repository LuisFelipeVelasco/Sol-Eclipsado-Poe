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


/*
@author Jeronimo Rojas 2021517760-3743
@version 1.0.4

 */
public class HelloController {

    @FXML
    private Label welcomeText;
    @FXML
    private TextField palabraField;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        if (VerificarCaracteresEspeciales() && VerificarLongitudPalabra()){ // Si el usuario escribe la palabra secreta correctamente entonces llama a playcontroller y le pasa la palabra
            String PalabraSecreta=ObtenerPalabraVerificada();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaAdivinarPalabra.fxml")); //Carga el archivo fxmLoader
            root=fxmlLoader.load(); // Carga los componentes de fxmLoader
            PlayController playController = fxmlLoader.getController(); // Crea una instancia del controlador de VistaAdivinarPalabra.fxml
            playController.AdivinarPalabra(PalabraSecreta);
            stage=(Stage)palabraField.getScene().getWindow(); //palabraField sabe en que escena vive
            scene=new Scene(root);
            stage.setScene(scene); // Cambia la escena
            stage.show();
        };
    }
    //Valida si la palabra guardada tiene entre 6 y 12 letras, caso contrario tira advertencia y reinicia la palabra en blanco
    protected boolean VerificarLongitudPalabra() {
        Integer GuardarLongitudPalabra = palabraField.getLength(); //guarda la longitud de la palabra escrita pór el usuario
        if (GuardarLongitudPalabra < 6 || GuardarLongitudPalabra > 12) {
            welcomeText.setText("La palabra debe ser entre 6 y 12 letras");
            palabraField.setText("");
            return false;
        }
        return true;
    }
    //Valida si la palabra tiene espacios o caracteres especiales, caso contrario tira una advertencia y reinicia la palabra a blanco
    protected  boolean VerificarCaracteresEspeciales(){
        String GuardarPalabra = palabraField.getText(); //guarda la palabra escrita pór el usuario
        if (!GuardarPalabra.matches("\\p{L}+")) {
            welcomeText.setText("La palabra no puede tener espacios ni caracteres especiales");
            palabraField.setText("");
            return false;
        }
        return true;
    }
    //Retorna la palabra escrita por el usuario en minusculas y sin tildes
    protected String ObtenerPalabraVerificada(){
        String GuardarPalabra=palabraField.getText();
        String Normalizar = Normalizer.normalize(GuardarPalabra, Normalizer.Form.NFD);// Se llama a la libreria Normalizer para usar su funcion normalize y separar las tildes de las letras
        Normalizar = Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT); // esta funcion utiliza el replaceAll para quitar las tildes y usa la funcion LowerCase para pasar todo a minusculas y guardarlo todo en la variable Normalizer
        return Normalizar;
    }
}