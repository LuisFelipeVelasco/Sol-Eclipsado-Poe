package com.example.soleclipsado;
import java.text.Normalizer;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



public class HelloController {
   /*
   Creamos variables
    */
    @FXML
    private Label welcomeText;
    String GuardarPalabra; //esta variable sera utilizada para obtener el texto que el usuario ponga en el TextField
    int GuardarLongitud;
    String Normalizar; //esta variable sera utilizada para guardar los cambios que se haran para pasar de mayusculas y tildes a solo minusculas

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");


        //GuardarPalabra guarda la palabra escrita pór el usuario
        GuardarPalabra = palabraField.getText();


            Normalizar=Normalizer.normalize(GuardarPalabra,Normalizer.Form.NFD);// Se llama a la libreria Normalizer para usar su funcion normalize y separar las tildes de las letras
           Normalizar= Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT); // esta funcion utiliza el replaceAll para quitar las tildes y usa la funcion LowerCase para pasar todo a minusculas y guardarlo todo en la variable Normalizer
        System.out.println(Normalizar);

    }
    @FXML
    private TextField palabraField;

    @FXML
    protected void onWordRedaction(){
        welcomeText.setText("Welcome to JavaFX Application!");


        GuardarPalabra = palabraField.getText();


        Normalizar=Normalizer.normalize(GuardarPalabra,Normalizer.Form.NFD);
        Normalizar= Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);





    }






}
