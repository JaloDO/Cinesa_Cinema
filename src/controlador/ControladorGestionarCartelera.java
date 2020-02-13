/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import modelo.claseCartelera;
import modelo.claseFacturado;
import modelo.conectarBD;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ControladorGestionarCartelera implements Initializable {
conectarBD cnx=new conectarBD();
File ficheroImagen;
    @FXML
    private AnchorPane ac;
    @FXML
    private TabPane tabPanel1;
    @FXML
    private Tab tab;
    @FXML
    private AnchorPane tabCartelera;
    @FXML
    private Tab tabFacturacion;
    @FXML
    private Button btnNuevo;
    @FXML
    private TextField txtTitulo;
    Stage ventanaGestion;
    @FXML
    private Button btnResetear;
    @FXML
    private TextField txtSala;
    @FXML
    private ImageView imgCartel;
    @FXML
    private DatePicker dateCalendar;
    @FXML
    private TextField txtHora;
    @FXML
    private Button btnModficar;
    @FXML
    private Button btnFacturacion;
    private Label lbFacturacion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnNuevaPelicula(ActionEvent event) {
        String titulo=txtTitulo.getText();
        LocalDate date = dateCalendar.getValue();
    
        if (!txtTitulo.getText().isEmpty() && !txtHora.getText().isEmpty() && !txtSala.getText().isEmpty() && ficheroImagen.exists()){
                String sesion=date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth()+"-"+txtHora.getText();
                if (cnx.comprobarDisponibilidad(sesion,Integer.valueOf(txtSala.getText() ))){
                   cnx.InsertarNuevaPelicula(txtTitulo.getText(),sesion,Integer.valueOf(txtSala.getText()),ficheroImagen);
                    txtHora.setText("");
                    txtSala.setText("");
                    txtTitulo.setText("");
                    imgCartel.setImage(null);

                }else
                {
                   Alert alerta=new Alert(AlertType.ERROR,"Sala ocupada por una sesión anterior");
            alerta.show();
                }
        }else{
            Alert alerta=new Alert(AlertType.ERROR,"Faltan datos");
            alerta.show();
        }
       
        txtTitulo.requestFocus();
        
    }

    @FXML
    private void bteResetear_OnAction(ActionEvent event) {
        cnx.ResetearCartelera();
        
    }

    @FXML
    private void imagen_mouseClicked(MouseEvent event) {
    try {
        FileChooser fileChooser = new FileChooser();
        
        
        FileChooser.ExtensionFilter filtroJPG = new FileChooser.ExtensionFilter("ficheros jpg (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter filtroPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(filtroJPG, filtroPNG);
        
        
         ficheroImagen = fileChooser.showOpenDialog(null);
         
        /*  BufferedImage bufferedImage = ImageIO.read(ficheroImagen);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);*/
        Image image = new Image(ficheroImagen.toURI().toURL().toString());
        imgCartel.setImage(image);
    } catch (MalformedURLException ex) {
        Logger.getLogger(ControladorGestionarCartelera.class.getName()).log(Level.SEVERE, null, ex);
    }
 
        }

    @FXML
    private void btnModificarPelicula(ActionEvent event) {
        LocalDate date = dateCalendar.getValue();
    
        if (!txtTitulo.getText().isEmpty() && !txtHora.getText().isEmpty() && !txtSala.getText().isEmpty() && ficheroImagen.exists()){
                String sesion=date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth()+"-"+txtHora.getText();
            
                if (!cnx.modificarPeliculas(txtTitulo.getText(),sesion,Integer.valueOf(txtSala.getText()),ficheroImagen)) {
                    System.out.println("ENtraaaa2");
                   
                    Alert alerta = new Alert(Alert.AlertType.WARNING, "ERROR al modificar pelicula", ButtonType.OK);
                    alerta.show();
                    
                } else {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Pelicula modificada", ButtonType.OK);
                    alerta.show();
                }
        }else{
            Alert alerta=new Alert(AlertType.ERROR,"Faltan datos");
            alerta.show();
        }
       
        txtTitulo.requestFocus();
        
        txtHora.setText("");
        txtSala.setText("");
        txtTitulo.setText("");
        imgCartel.setImage(null);
    }

    @FXML
    private void btnFacturacion_OnAction(ActionEvent event) {
        ArrayList<claseFacturado> facturacion = cnx.facturacion();
        int total = 0;
        try {
            String fechaFactura = new SimpleDateFormat("dd-MMMM-yyyy_kk-mm").format(new Date().getTime());
            FileOutputStream fos = new FileOutputStream(new File("Factura_"+fechaFactura + ".pdf"));
            
            Document documento = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(documento, fos);
            documento.open();
            PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

            PdfWriter.getInstance(documento, fos);

            Paragraph parrafo;

            for (claseFacturado f : facturacion) {

                parrafo = new Paragraph("Sesión: "+f.getSesion() +"    Sala: " + f.getIdSala()+"    Butaca: F"+f.getFila()+" - C"+f.getColumna());
                parrafo.setAlignment(Element.ALIGN_LEFT);
                documento.add((Element) parrafo);

                total += 5;
            }
            
            parrafo = new Paragraph("Entradas Vendidas: " + facturacion.size()+" --------------------------------------- Beneficio Bruto: " + total + "€");
            parrafo.setAlignment(Element.ALIGN_LEFT);
            documento.add((Element) parrafo);
            
            Desktop.getDesktop().open(new File("Factura_"+fechaFactura + ".pdf"));
            documento.close();
        } catch (FileNotFoundException | DocumentException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
}

