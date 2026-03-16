package com.example.soleclipsado;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.PasswordAuthentication;
import java.text.Normalizer;
import java.util.*;


/*
@author Luis Felipe Velasco Chilito 2517245-3743
@version 1.0.4

 */


public class PlayController {
    @FXML
    private HBox hbox;
    @FXML
    private Label AdvertenciaText;
    private String PalabraSecreta;
    private List<TextField> textFields = new ArrayList<>();


    //Metodo que va a orquestar la interfaz de adivinar la palabra
    public void AdivinarPalabra(String Palabra) {
        PalabraSecreta=Palabra;
        CrearCamposdeTextoEnInterfaz();
        AsignarSetOnKeyTypedACamposDeTexto();
    }

    //Metodo para crear los campos de texto de acuerdo a la longitud de la palabra
    protected void CrearCamposdeTextoEnInterfaz(){

        Integer GuardarLongitudPalabraSecreta= PalabraSecreta.length();
        for(Integer i=0; i< GuardarLongitudPalabraSecreta; i++){
            TextField textField = GetTextField(); //Instancia un campo de texto con estilo
            textFields.add(textField); //Añade al objeto texfield en la lista de textFields
            hbox.getChildren().add(textField); //Agrega el campo de texto en el hbox
        }

    }
    //Retorna un textField con un estilo
    protected TextField GetTextField() {
        TextField textField= new TextField(); //Instancia un nuevo texfield
        textField.setStyle( "-fx-font-size: 20; " + "-fx-font-family: 'Arial'; " + "-fx-font-weight: bold; " + //Estilo para textfield
                        "-fx-alignment: center; "  + "-fx-border-color: #34495e; " + "-fx-border-width: 1; " + "-fx-border-radius: 2;" +"-fx-min-height: 40; " +
                "-fx-max-height: 40; " +
                "-fx-pref-height: 40; "
        );
        return textField;
    }

    //Le asigna a cada texfield el event listener setOnKeyTyped y hace que VerificarPalabra se encargue del evento
    protected void AsignarSetOnKeyTypedACamposDeTexto(){
        for (TextField textField : textFields){
            textField.setOnKeyTyped(this::ControladorCampoDeTexto);
        }
    }
    //controla/verifica las entradas de los campos de texto
    protected void ControladorCampoDeTexto(KeyEvent keyEvent){
        TextField textField=(TextField) keyEvent.getSource();
        String Entrada=Normalizer.normalize(keyEvent.getCharacter(), Normalizer.Form.NFD);// Se llama a la libreria Normalizer para usar su funcion normalize y separar las tildes de las letras
        Entrada = Entrada.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT); // esta funcion utiliza el replaceAll para quitar las tildes y usa la funcion LowerCase para pasar todo a minusculas y guardarlo todo en la variable Entrada
        if(ManejarIngresoDeSoloLetrasEnCampoDeTexto(textField,Entrada)){
            boolean ValorDeIntento=VerificarEntradaCoincideEnPalabraSecreta(textField,Entrada);
        }

    }
    //Evita que el usuario ingrese un caracter especial o un numero a un campo de texto
    protected boolean ManejarIngresoDeSoloLetrasEnCampoDeTexto(TextField textField,String Entrada){
        if (!Entrada.matches("\\p{L}+") && !Entrada.isEmpty()) {
            AdvertenciaText.setText("Ojo..  nada de numeros o caracteres especiales");
            textField.setText("");
            return false;
        }
        return true;
    }
    //Verifica que la entrada en un campo de texto coincida con la letra de la palabra secreta en esa posicion
    protected  boolean VerificarEntradaCoincideEnPalabraSecreta(TextField textField,String Entrada){
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        int PosicionCampodeTexto=textFields.indexOf(textField);
        String LetraCorrecta= ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);
        if(Objects.equals(LetraCorrecta, Entrada)){
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: green;");
            AdvertenciaText.setText("Bien , le atinaste");
            textField.setDisable(true); //Ya no se puede moficar el textField
            return true;
        }
        else if (!LetraCorrecta.equalsIgnoreCase(Entrada) && !Entrada.isEmpty()){ //Si las letras no coinciden y es no vacio entonces retornar falso y dejar vacio el campo de texto
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: red;");
            AdvertenciaText.setText("Nope, la letra no coincide");
            textField.setText("");
            return false;
        }
        return true;


    }
}



