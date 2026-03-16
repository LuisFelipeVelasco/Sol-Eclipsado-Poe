package com.example.soleclipsado;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
    private String PalabraSecreta;
    private List<TextField> textFields = new ArrayList<>();


    //Metodo que va a orquestar la interfaz de adivinar la palabra
    public void AdivinarPalabra(String Palabra) {
        PalabraSecreta=Palabra;
        CrearCamposdeTexto();
    }

    //Metodo para crear los campos de texto de acuerdo a la longitud de la palabra
    public void CrearCamposdeTexto(){

        Integer GuardarLongitudPalabraSecreta= PalabraSecreta.length();
        for(Integer i=0; i< GuardarLongitudPalabraSecreta; i++){
            TextField textField= new TextField(); //Instancia un nuevo texfield
            textField.setStyle( "-fx-font-size: 13; " + "-fx-font-family: 'Arial'; " + "-fx-font-weight: bold; " + //Estilo para textfield
                            "-fx-alignment: center; "  + "-fx-border-color: #34495e; " + "-fx-border-width: 1; " + "-fx-border-radius: 2;" + "-fx-pref-height: 20;"
            );
            textFields.add(textField); //Añade al objeto textField en la lista de textFields
            hbox.getChildren().add(textField); //Agrega el textField en el hbox
        }

    }


}

