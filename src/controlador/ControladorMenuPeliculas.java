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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.claseCartelera;
import modelo.conectarBD;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ControladorMenuPeliculas implements Initializable {
ArrayList <claseCartelera> listaCartelera=new ArrayList<>();
public ImageView[] arrayImagenes;
public Button[] arrayBotones;
String cadenaQR="";
//public GridPane panel;
conectarBD cnx=new conectarBD();
    @FXML
    private AnchorPane ac;
    private GridPane gp;
    @FXML
    private AnchorPane acGestion;
    @FXML
    private TextField txtLectorQr;
    @FXML
    private Label lbBandera;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        listaCartelera=cnx.MostrarCartelera();
        cargarCarteles();
    }    

    private void cargarCarteles() {
    
      arrayImagenes=new ImageView[listaCartelera.size()];
      int Npeliculas=arrayImagenes.length;
      int filas=(int) Math.round(Math.sqrt(Npeliculas))+1;
      int k=0;
      
      gp=new GridPane();
     
      gp.setHgap(10);
      gp.setVgap(10);
      gp.setPadding(new Insets(10,10,10,10));
      gp.setAlignment(Pos.CENTER);
        
    
       
      for (int i=0;i<filas;i++)
      {
          for (int j=0;j<filas;j++)
          {
              if (k<Npeliculas)
              {
                  InputStream entrada=new ByteArrayInputStream(listaCartelera.get(k).getCartel());
                  arrayImagenes[k]=new ImageView();
                  arrayImagenes[k].setFitHeight(1500/Npeliculas);
                  arrayImagenes[k].setFitWidth(1500/Npeliculas);
                  arrayImagenes[k].setPreserveRatio(false);
                  arrayImagenes[k].setImage(new Image(entrada));
                  String tit=listaCartelera.get(k).getTitulo();
                  
                  arrayImagenes[k].setOnMousePressed(new EventHandler<MouseEvent>() {
                      @Override
                      public void handle(MouseEvent event) {
                          try 
                          {
                              System.out.println(tit);
                              FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLSeleccionarSesion.fxml"));
                              Parent root =  loader.load();
                              ControladorSeleccionarSesion controlador2 = loader.getController();
                              controlador2.setTitulo(tit);
                              controlador2.ivan();
                              Stage ventana = new Stage();
                              controlador2.ventana2=ventana;
                              ventana.setResizable(false);
                              ventana.setTitle("Seleccionar sesión...");
                              
                              Scene scene = new Scene(root);
                              ventana.setScene(scene);
                              ventana.setWidth(700);
                              ventana.setHeight(400);
                              ventana.setX(0);
                              ventana.setY(0);

                              ventana.show();
                          } catch (IOException ex) {
                              Logger.getLogger(ControladorMenuPeliculas.class.getName()).log(Level.SEVERE, null, ex);
                          }
                      }
                  });
                  gp.add(arrayImagenes[k], j, i);
                  k++;
              }
          }
      }
      ac.getChildren().add(gp);
      AnchorPane.setRightAnchor(gp, 10d);
      AnchorPane.setLeftAnchor(gp, 10d);
      AnchorPane.setTopAnchor(gp,10d);
      AnchorPane.setBottomAnchor(gp, 10d);
    }


    @FXML
    private void KeyType_barcode(KeyEvent event) {
 //  if (event.getCharacter().charAt(0)==1) txtLectorQr.setText("");
        cadenaQR+=event.getCharacter();
           
           if (cadenaQR.length()>21){
               
               String codigo=cadenaQR.replace("'", "-").replaceFirst("Ñ", ":");
               System.out.println(codigo);
               System.out.println("sesion: "+codigo.substring(0,16));
               String sesion=codigo.substring(0,16);
               System.out.println("sala:"+codigo.substring(16,18));
               int sala=Integer.valueOf(codigo.substring(16,18));
               System.out.println("Sala integer; "+sala);
               System.out.println("fila "+codigo.substring(18,20));
               int fila=Integer.valueOf(codigo.substring(18,20));
               System.out.println("columna "+codigo.substring(20,22));
               int columna=Integer.valueOf(codigo.substring(20,22));
               
               if (cnx.comprobarAsiento(sesion,sala,fila,columna))
               {
                cnx.ocuparButaca(sesion,sala,fila,columna);
                lbBandera.setStyle("-fx-background-color:green");
                lbBandera.setText("Asiento LIBRE");
           
               }else
               {
                   lbBandera.setStyle("-fx-background-color:red");
                   lbBandera.setText("Asiento OCUPADO");
               }
               
               cadenaQR="";
               System.out.println("despues borrar" +cadenaQR);
               txtLectorQr.clear();
         
               txtLectorQr.requestFocus();
               txtLectorQr.setText("");
           }
        
    }
    

  

  

    @FXML
    private void verGestion(ScrollEvent event) {
       
           
           acGestion.setVisible(true);
       txtLectorQr.requestFocus();
         
          
         
       
    }

    @FXML
    private void entradaDatos(InputMethodEvent event) {
      
    }


    @FXML
    private void gestionarEscape(KeyEvent event) {
        if (event.getCode()==KeyCode.BACK_SPACE)
          System.out.println("gestionar");
    }

    @FXML
    private void gestionarMenu(ContextMenuEvent event) {
        System.out.println("gestión");
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLGestionarCartelera.fxml"));
        
        Parent root =  loader.load();
        ControladorGestionarCartelera controladorGestion = loader.getController();
       
        Stage ventana = new Stage();
        controladorGestion.ventanaGestion=ventana;
        ventana.setResizable(false);
        
        ventana.setTitle("Gestión");
        //ventana.initOwner(((Node)event.getSource()).getScene().getWindow());
        Scene scene = new Scene(root);
        ventana.setScene(scene);
        ventana.setWidth(800);
        ventana.setHeight(400);
        ventana.setX(0);
        ventana.setY(0);
        ventana.show();
    } catch (IOException ex) {
        Logger.getLogger(ControladorMenuPeliculas.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    
    
}
