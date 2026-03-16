package com.example.soleclipsado;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;


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
        String Entrada=textField.getText();
        if(ManejarIngresoDeUnCaracterEnCampoDeTexto(textField,Entrada)&& ManejarIngresoDeSoloLetrasEnCampoDeTexto(textField,Entrada)){
            VerificarEntradaCoincideEnPalabraSecreta(textField);
        }

    }

    //Evita que el usuario ingrese mas de un caracter a un campo de texto
    protected boolean ManejarIngresoDeUnCaracterEnCampoDeTexto(TextField textField,String Entrada){
        if (Entrada.length()>1){
                AdvertenciaText.setText("Relax.. Solo 1 caracter por campo");
                textField.setText("");
                return false;
        }
        return true;
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
    protected  void VerificarEntradaCoincideEnPalabraSecreta(TextField textField){
        System.out.println(textField);
    }
}



