/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import modelo.claseCartelera;
import modelo.claseSala;
import modelo.conectarBD;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ControladorSeleccionarSesion implements Initializable  {
ObservableList <claseCartelera> ListaDatos=FXCollections.observableArrayList();
   ArrayList<claseCartelera> lista=new ArrayList<>();
   ArrayList <claseSala> listaSalas=new ArrayList<>();

conectarBD cnx=new conectarBD();
public Stage ventana2;
public String titulo;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    @FXML
    private AnchorPane ac;
    @FXML
    private ImageView imgPelicula;
    @FXML
    public Label lbTitulo;
    @FXML
    private ListView<claseCartelera> lstSesiones;

    

    
  
    @Override
    public void initialize(URL location, ResourceBundle resources) {



           }



    @FXML
    private void OnMouse_seleccionarSesion(MouseEvent event) {
  
        //    String[] verSesion=lista.get(0).getSesion().split("-");
        int seleccion=lstSesiones.getSelectionModel().getSelectedIndex();
        int Sala=lista.get(seleccion).getSala();
        System.out.println("Sala: "+Sala);
        String sesion=lista.get(0).getSesion();
        //   System.out.println(verSesion[3]);
        System.out.println(Integer.toString(Sala));
        
        
        cargarSala(Sala,sesion);
     }

    private void cargarSala(int Sala,String Sesion) {
    try {
        listaSalas=cnx.MostrarSalas();
        int filas=0;
        int col=0;
        for (int i=0;i<listaSalas.size();i++){
            if (listaSalas.get(i).getIndice()==Sala){
                filas=listaSalas.get(i).getFilas();
                col=listaSalas.get(i).getColumnas();
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLSalas.fxml"));
        
        Parent root =  loader.load();
        ControladorSalas controlador2 = loader.getController();
        controlador2.filas=filas;
        controlador2.columnas=col;
        controlador2.Sesion =Sesion;
        controlador2.sala=Sala;
        controlador2.CargarButacas();
        
                
        Stage ventana = new Stage();
        controlador2.ventanaSalas=ventana;
          ventana.setFullScreen(true);
        ventana.setTitle("Seleccionar butaca...");
      
        Scene scene = new Scene(root);
        ventana.setScene(scene);
       
        //  controlador2.setCnx(cnx);
        //  controlador2.setIdPelicula(tit);
          

    
        ventana.show();
      
       ventana2.close();
        
       
       
    } catch (IOException ex) {
        Logger.getLogger(ControladorSeleccionarSesion.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Throwable ex) {
        Logger.getLogger(ControladorSeleccionarSesion.class.getName()).log(Level.SEVERE, null, ex);
    } 
    }
    public void ivan(){
            lista=cnx.MostrarTodaSesiones(titulo);
            InputStream entrada=new ByteArrayInputStream(lista.get(0).getCartel());
            lbTitulo.setText(titulo);
            imgPelicula.setImage(new Image(entrada));
            ListaDatos=cnx.MostrarCartelera2(titulo);
            lstSesiones.setItems(ListaDatos); 
    }
   
    }
    

