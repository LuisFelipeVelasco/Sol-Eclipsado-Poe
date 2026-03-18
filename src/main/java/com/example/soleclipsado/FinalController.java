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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.text.Normalizer;
import java.util.*;

public class FinalController {
@FXML
    private Button buttonFinal;
    @FXML
    private Label TextoFinal;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    protected void onHelloButtonClicked(){

    }
    public void cambiarLabelPerdedor(){
        TextoFinal.setText("Intentos terminados,Perdiste");
    }
    public void cambiarLabelGanador(){
        TextoFinal.setText("¡¡GANASTE!!");
    }



}
