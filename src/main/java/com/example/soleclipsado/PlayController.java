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


/*
@author Luis Felipe Velasco Chilito 2517245-3743
@version 1.0.4

 */


public class PlayController {
    @FXML
    private HBox hbox;
    @FXML
    private AnchorPane AnchorRoot;
    @FXML
    private ImageView imageSol;

    @FXML
    private Label AdvertenciaText;
    private String PalabraSecreta;
    private List<TextField> textFields = new ArrayList<>();
    int ContadorPistasDadas=0;
    boolean valorDeIntento;
    int ContadorEstadoImagenSolEclipsado=1;
    int ContadorLetrasAcertadas=0;
    boolean JugadorAcertoTodasLasLetras=false;


    @FXML
    //Funcion que se activa cada vex que el boton pista es precionado
    protected void onActionButtonClicked(){
        int Contador=ContadorPista();// Este contador asegura que el boton no pueda ser usado mas de 3 veces
        if (Contador<=3){
            for(TextField textField : textFields){//Recorre toda la lista de textfields
                if(textField.getText().isEmpty()){//encuentra el primer textfield vacio
                    PistaAgregarLetra(textField);//llama a la funcion PistaAgregarLetra pasandole como parametro dicho textfield
                    break;//detiene la funcion
                }
            }
        }
        System.out.println("Boton pista funcional");
    }

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
        textField.setStyle( "-fx-font-size: 15; " + "-fx-font-family: 'Arial'; " + "-fx-font-weight: bold; " + //Estilo para textfield
                        "-fx-alignment: center; "  + "-fx-border-color: #34495e; " + "-fx-border-width: 1; " + "-fx-border-radius: 2;" +"-fx-min-height: 40; " +
                "-fx-max-height: 40; " +
                "-fx-pref-height: 40; "
        );
        return textField;
    }

    //Le asigna a cada texfield el event listener setOnKeyTyped y hace que ControladorCampoDeTexto se encargue del evento
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
        if(IngresoDeSoloLetrasEnCampoDeTexto(textField,Entrada)==true){
            valorDeIntento=VerificarEntradaCoincideEnPalabraSecreta(textField,Entrada);//variable que guarda si el usuario se equivoco o no
            CambiarSolEclipsado();
            try {
                CambiarVistaFinal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Evita que el usuario ingrese un caracter especial o un numero a un campo de texto
    protected boolean IngresoDeSoloLetrasEnCampoDeTexto(TextField textField,String Entrada){

        if (!Entrada.matches("\\p{L}+") && !Entrada.isEmpty()) {
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: red;");
            AdvertenciaText.setText("Ojo..  nada de numeros o caracteres especiales");
            textField.setText("");
            return false;
        }
        return true;
    }
    //Verifica que la entrada en un campo de texto coincida con la letra de la palabra secreta en esa posicion
    protected  boolean VerificarEntradaCoincideEnPalabraSecreta(TextField textField,String Entrada){

        String LetraCorrecta= LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(textField);
        if(Objects.equals(LetraCorrecta, Entrada)){
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: green;");
            AdvertenciaText.setText("Bien , le atinaste");
            textField.setDisable(true); //Ya no se puede moficar el textField
            ContadorLetrasAcertadas+=1;
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

    //Funcion para contar las veces que el usuario puede usar una pista
    protected int ContadorPista(){

        if(ContadorPistasDadas<=3){
            ContadorPistasDadas+=1;
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: green;");
            AdvertenciaText.setText("numero de pistas restantes: "+(3-ContadorPistasDadas)+" ");
        }else{
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: red;");
            AdvertenciaText.setText("numero de pistas acabados");
        }
    return ContadorPistasDadas;}

    //Funcion para qque cada vez que se unda el boton de pista se agregue la letra correspondiente en el campo en el cual se encuentre el usuario
    protected void PistaAgregarLetra(TextField textField){
        String LetraPalabraSecreta=LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(textField);
        textField.setText(LetraPalabraSecreta);//Asigna al textField en el que se posicione el usuario la letra correcta
        ContadorLetrasAcertadas+=1;

        System.out.println("Todo chido");

    }
    //Funcion para cambair la imagen del sol en caso de que el usuario se equivoque
    protected void CambiarSolEclipsado(){
        if(valorDeIntento!=true && ContadorEstadoImagenSolEclipsado<=5){
             ContadorEstadoImagenSolEclipsado+=1;
                imageSol.setImage(new Image(getClass().getResource(
                        "/com/example/soleclipsado/IMAGENES/Sol_"+ContadorEstadoImagenSolEclipsado+".png").toExternalForm()));
        }
    }

    //Funcion que cambia a la pantalla final dependiendo si el jugador gana o pierde
    protected void CambiarVistaFinal() throws IOException {

        boolean JugadorPierde = (ContadorEstadoImagenSolEclipsado==6);
        boolean JugadorGana = (ContadorEstadoImagenSolEclipsado<6 && ValidarJugadorAcertoTodasLasLetras());

        if(JugadorPierde || JugadorGana){

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaFinal.fxml"));
            Parent root = fxmlLoader.load();
            FinalController finalController=fxmlLoader.getController();

            if(JugadorPierde) finalController.cambiarLabelPerdedor();
            else  finalController.cambiarLabelGanador();

            Stage stage = (Stage) AnchorRoot.getScene().getWindow();
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
        }
    }

    //funcion que guarda el Exito del jugador en caso de que haya acertado todas las letras de la palabra secreta
    protected boolean ValidarJugadorAcertoTodasLasLetras() {

        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        return ContadorLetrasAcertadas == ListaLetrasPalabraSecreta.size();         // si el ContadorLetrasAcertadas  es igual a la cantidad de letras en ListaLetrasPalabraSecreta, retorna verdader
    }

    protected String LetraDePalabraSecretaSegunCampoDeTextoSeleccionado(TextField textField){
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        int PosicionCampodeTexto=textFields.indexOf(textField);
        return ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);
    }
}



