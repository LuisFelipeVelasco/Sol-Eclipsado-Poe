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
    protected boolean onHelloButtonClick() {
        //GuardarPalabra guarda la palabra escrita pór el usuario
        GuardarPalabra = palabraField.getText();
        //GuardarLongitud guarda la longitud de la palabra escrita pór el usuario
        GuardarLongitud= palabraField.getLength();
        //este if revisa si la palabra guardada tiene entre 6 y 12 letras, caso contrario tira una advertencia y reinicia la palabra a blanco
        if(GuardarLongitud<6 || GuardarLongitud>12){
            welcomeText.setText("La palabra debe ser entre 6 y 12 letras");
            palabraField.setText("");
            return false;


            //este if revisa si la palabra tiene espacios o caracteres especiales, caso contrario tira una advertencia y reinicia la palabra a blanco
        }if (!GuardarPalabra.matches("\\p{L}+")){
            welcomeText.setText("La palabra no puede tener espacios ni caracteres especiales");
            palabraField.setText("");
            return false;

        } else{
            welcomeText.setText("Welcome to JavaFX Application!");






            Normalizar=Normalizer.normalize(GuardarPalabra,Normalizer.Form.NFD);// Se llama a la libreria Normalizer para usar su funcion normalize y separar las tildes de las letras
            Normalizar= Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT); // esta funcion utiliza el replaceAll para quitar las tildes y usa la funcion LowerCase para pasar todo a minusculas y guardarlo todo en la variable Normalizer

            System.out.println(Normalizar);
            return true;
        }



    }
    @FXML
    private TextField palabraField;

    @FXML
    protected boolean onWordRedaction(){
        //GuardarPalabra guarda la palabra escrita pór el usuario
        GuardarPalabra = palabraField.getText();
        //GuardarLongitud guarda la longitud de la palabra escrita pór el usuario
        GuardarLongitud= palabraField.getLength();
        //este if revisa si la palabra guardada tiene entre 6 y 12 letras, caso contrario tira una advertencia y reinicia la palabra a blanco
        if(GuardarLongitud<6 || GuardarLongitud>12){
            welcomeText.setText("La palabra debe ser entre 6 y 12 letras");
            palabraField.setText("");
            return false;


            //este if revisa si la palabra tiene espacios o caracteres especiales, caso contrario tira una advertencia y reinicia la palabra a blanco
        }if (!GuardarPalabra.matches("\\p{L}+")){
            welcomeText.setText("La palabra no puede tener espacios ni caracteres especiales");
            palabraField.setText("");
            return false;

        } else{
            welcomeText.setText("Welcome to JavaFX Application!");






            Normalizar=Normalizer.normalize(GuardarPalabra,Normalizer.Form.NFD);// Se llama a la libreria Normalizer para usar su funcion normalize y separar las tildes de las letras
            Normalizar= Normalizar.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT); // esta funcion utiliza el replaceAll para quitar las tildes y usa la funcion LowerCase para pasar todo a minusculas y guardarlo todo en la variable Normalizer

            System.out.println(Normalizar);
            return true;
        }





    }






}
