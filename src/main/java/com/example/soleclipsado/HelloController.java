package com.example.soleclipsado;
import java.text.Normalizer;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/*
@author Jeronimo Rojas 2021517760-3743
@version 1.0.4

 */
public class HelloController {
    /*
    Creamos variables
     */
    @FXML
    private Label welcomeText;
    @FXML
    private TextField palabraField;
    String GuardarPalabra; //esta variable sera utilizada para obtener el texto que el usuario ponga en el TextField
    int GuardarLongitud;
    String Normalizar; //esta variable sera utilizada para guardar los cambios que se haran para pasar de mayusculas y tildes a solo minusculas

    @FXML
    protected boolean onHelloButtonClick() {
        return  VerificarCaracteresEspeciales() && VerificarLongitudPalabra();

    }
    //revisa si la palabra guardada tiene entre 6 y 12 letras, caso contrario tira advertencia y reinicia la palabra en blanco
    protected boolean VerificarLongitudPalabra() {
        GuardarLongitud = palabraField.getLength(); //guarda la longitud de la palabra escrita pór el usuario
        if (GuardarLongitud < 6 || GuardarLongitud > 12) {
            welcomeText.setText("La palabra debe ser entre 6 y 12 letras");
            palabraField.setText("");
            return false;
        }
        return true;
    }
    //revisa si la palabra tiene espacios o caracteres especiales, caso contrario tira una advertencia y reinicia la palabra a blanco
    protected  boolean VerificarCaracteresEspeciales(){
        GuardarPalabra = palabraField.getText(); //guarda la palabra escrita pór el usuario
        if (!GuardarPalabra.matches("\\p{L}+")) {
            welcomeText.setText("La palabra no puede tener espacios ni caracteres especiales");
            palabraField.setText("");
            return false;
        }
        return true;
    }
    protected String ObtenerPalabraVerificada(){
        welcomeText.setText("Welcome to JavaFX Application!");
        Normalizar = Normalizer.normalize(GuardarPalabra, Normalizer.Form.NFD);// Se llama a la libreria Normalizer para usar su funcion normalize y separar las tildes de las letras
        Normalizar = Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT); // esta funcion utiliza el replaceAll para quitar las tildes y usa la funcion LowerCase para pasar todo a minusculas y guardarlo todo en la variable Normalizer
        System.out.println(Normalizar);
        return Normalizar;
    }
}