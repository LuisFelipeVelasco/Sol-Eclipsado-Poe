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
    int i=0;//contador para las pistas
    boolean valorDeIntento;
    int c=1;// contador para el cambio de la imagen del sol eclipsado
    int g=0;// contador para el numero de letras acertadas
    int Exito;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    //Funcion que se activa cada vex que el boton pista es precionado
    protected void onActionButtonClicked(){
        int Contador=ContadorPista();// Este contador asegura que el boton no pueda ser usado mas de 3 veces
        if (Contador<=3){
            for(TextField tf : textFields){//Recorre toda la lista de textfields
                if(tf.getText().isEmpty()){//encuentra el primer textfield vacio
                    PistaAgregarLetra(tf);//llama a la funcion PistaAgregarLetra pasandole como parametro dicho textfield
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
        JugadorGana(textField,Entrada);
        GuardarGana(textField,Entrada);
        if(ManejarIngresoDeSoloLetrasEnCampoDeTexto(textField,Entrada)){
            valorDeIntento=VerificarEntradaCoincideEnPalabraSecreta(textField,Entrada);//variable que guarda si el usuario se equivoco o no



            CambiarSolEclipsado();
            try {
                CambiarVistaFinal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }//funcion que cambia la imagen del sol en caso de quivocacion


    }
    //Evita que el usuario ingrese un caracter especial o un numero a un campo de texto
    protected boolean ManejarIngresoDeSoloLetrasEnCampoDeTexto(TextField textField,String Entrada){
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

    //Funcion para contar las veces que el usuario puede usar una pista
    protected int ContadorPista(){

        if(i<=3){
            i+=1;
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: green;");
            AdvertenciaText.setText("numero de pistas restantes: "+(3-i)+" ");
        }else{
            AdvertenciaText.setStyle(AdvertenciaText.getStyle() + "-fx-text-fill: red;");
            AdvertenciaText.setText("numero de pistas acabados");

        }

    return i;}
    //Funcion para qque cada vez que se unda el boton de pista se agregue la letra correspondiente en el campo en el cual se encuentre el usuario
    protected void PistaAgregarLetra(TextField textField){
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta

        int PosicionCampodeTexto=textFields.indexOf(textField);//le asigna un numero que actua como su posicion a cada textfield creado
        String LetraCorrecta= ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);////Esto le asigna un numero, a cada letra de la lista PalabraSecreta, de acuerdo con su posicion
        textField.setText(LetraCorrecta);//Asigna al textField en el que se posicione el usuario la letra correcta
        g+=1;

        System.out.println("Todo chido");

    }
    //Funcion para cambair la imagen del sol en caso de que el usuario se equivoque
    protected void CambiarSolEclipsado(){
        if(valorDeIntento!=true && c<=5){
             c+=1;


                imageSol.setImage(new Image(getClass().getResource("/com/example/soleclipsado/IMAGENES/Sol_"+c+".png").toExternalForm()));


        }


    }
    //Funcion que cambia a la pantalla final dependiendo si el jugador gana o pierde
    protected void CambiarVistaFinal() throws IOException {
        if(c==6){


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaFinal.fxml"));
            Parent root = fxmlLoader.load();
            FinalController finalController=fxmlLoader.getController();
            finalController.cambiarLabelPerdedor();


            Stage stage = (Stage) AnchorRoot.getScene().getWindow();

            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();





        }
        if (c<6 && Exito!=0){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaFinal.fxml"));
            Parent root = fxmlLoader.load();
            FinalController finalController=fxmlLoader.getController();
            finalController.cambiarLabelGanador();


            Stage stage = (Stage) AnchorRoot.getScene().getWindow();

            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();

        }

    }
    //funcion que guarda los aciertos del jugador
    protected int JugadorGana(TextField textField, String Entrada){
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        int PosicionCampodeTexto=textFields.indexOf(textField);
        String LetraCorrecta= ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);
        if(Objects.equals(LetraCorrecta, Entrada)){
            g+=1;
            return g;
        }


    return g;}
    //funcion que guarda el Exito del jugador en caso de que haya acertado todas las letras de la palabra secreta
    protected int GuardarGana(TextField textField, String Entrada){
        List<String> ListaLetrasPalabraSecreta = Arrays.asList(PalabraSecreta.split("")); //Cada letra de la palabra secreta es un elemento de la lista ListaLetrasPalabraSecreta
        int PosicionCampodeTexto=textFields.indexOf(textField);
        String LetraCorrecta= ListaLetrasPalabraSecreta.get(PosicionCampodeTexto);
        if (g==ListaLetrasPalabraSecreta.size()){ // si el contador g proveniente de la funcion JugadorGana es igual a la cantidad de letras en ListaLetrasPalabraSecreta, cambia el valor de Exito para que la funcion CambiarVistaFinal lo tome distinto de cero y arroje la vista ganador
            Exito=g;
            return Exito;

        }
    return Exito;}



}



