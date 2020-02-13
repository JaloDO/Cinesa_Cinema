/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import static com.itextpdf.text.Annotation.FILE;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import modelo.claseFacturado;
import modelo.claseSala;
import modelo.conectarBD;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ControladorSalas implements Initializable {
public int filas;
public int columnas;
public String Sesion;
public int sala;
public Button[] arrayBotones;
ArrayList<claseSala> listaCesta=new ArrayList<>();
ArrayList<claseSala> listaSala=new ArrayList<>();
ArrayList<claseFacturado> listaFactura =new ArrayList<>();
static int Precio=6;
public Stage ventanaSalas;
conectarBD cnx=new conectarBD();
    @FXML
    private Label lbSala;
    @FXML
    private Label lbSesion;
    @FXML
    private AnchorPane ac;
    @FXML
    private Button btnCargar;
    @FXML
    private Button btnPagar;
    @FXML
    private AnchorPane acBotonera;
    @FXML
    private AnchorPane acButacas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    

   

    @FXML
    private void Cargar_onAction(ActionEvent event) {
         
    }

    @FXML
    private void btnPagar_OnAction(ActionEvent event) {
        int totalPagar=listaCesta.size()*Precio;
        System.out.println(totalPagar);
        for (int i=0;i<listaCesta.size();i++){
            cnx.insertarButacas(listaCesta.get(i).getFilas(),listaCesta.get(i).getColumnas(), sala,Sesion);
         //   generarCodigoBarras();
           imprimirTicket( sala,Sesion,listaCesta.get(i).getFilas(),listaCesta.get(i).getColumnas());
            String cadenaFile=Sesion.replaceAll("-", "_").replaceFirst(":", "_");
            String nombreFichero="C:\\Users\\jaliy\\Desktop\\"+cadenaFile+"_"+listaCesta.get(i).getFilas()+"_"+listaCesta.get(i).getColumnas()+".pdf";
            enviar_correo(nombreFichero);
        }
                 //      

             

        for (int i=0;i<arrayBotones.length;i++){
             //botonX.setDisable(true);
                         if (arrayBotones[i].getStyle().contains("-fx-background-color:red")){
                             
                             arrayBotones[i].setDisable(true);
                            
                         }
        }
        
        ventanaSalas.close();
    }

    void CargarButacas() {
        //public Button[] arrayBotones;
//ArrayList<claseSala> listaCesta=new ArrayList<>();
           lbSesion.setText(Sesion);
           lbSala.setText(String.valueOf(sala));
            listaFactura=  cnx.CargarSala(Sesion,sala);
           GridPane gp=new GridPane();
      
           gp.setHgap(5);
           gp.setVgap(5);
           gp.setPadding(new Insets(10,10,10,10));
           gp.setAlignment(Pos.CENTER);
           arrayBotones=new Button[filas*columnas];
           int k=0;
           for (int i=0;i<filas;i++){
               for (int j=0;j<columnas;j++){
                   arrayBotones[k]=new Button();
                    arrayBotones[k].setText(String.valueOf("F"+i+"-"+"C"+j));
                    arrayBotones[k].setStyle("-fx-background-color:green");
                   for (int indF=0;indF<listaFactura.size();indF++){
                       if ((listaFactura.get(indF).getFila()==i) && (listaFactura.get(indF).getColumna()==j))
                      {
                          System.out.println("encontrado");
                         arrayBotones[k].setStyle("-fx-background-color:yellow");
                         arrayBotones[k].setDisable(true);
                      } 
                   }
                   
                  
                         claseSala cs=new claseSala();
                         cs.setFilas(i);
                         cs.setColumnas(j);
                   arrayBotones[k].setOnAction(new EventHandler<ActionEvent>() {
                       @Override
                       public void handle(ActionEvent event) {
                         Button botonX=(Button) event.getSource();
                  
                        
                         //botonX.setDisable(true);
                         if (botonX.getStyle().contains("-fx-background-color:red")){
                             botonX.setStyle("-fx-background-color:green;"); 
                             listaCesta.remove(cs);
                         }
                         else{
                              botonX.setStyle("-fx-background-color:red;");
                               listaCesta.add(cs);
                         }
                        
                        System.out.println("añade a la cesta "+cs.getFilas());
                           
                       }
                   });
                  
                           
                   gp.add(arrayBotones[k], j, i);
                   k++;
               }
           }
            
            acButacas.getChildren().add(gp);
           
           
      AnchorPane.setRightAnchor(gp, 10d);
      AnchorPane.setLeftAnchor(gp, 10d);
      AnchorPane.setTopAnchor(gp,10d);
      AnchorPane.setBottomAnchor(gp, 10d);
    
    }

   



    private void imprimirTicket(int sala, String Sesion, int filas, int columnas) {
        
        Document document=new Document();
        String cadenaFile=Sesion.replaceAll("-", "_").replaceFirst(":", "_");
        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\jaliy\\Desktop\\"+cadenaFile+"_"+filas+"_"+columnas+".pdf")) {
            PdfWriter.getInstance(document, fos);
            document.open();
 
        String cadenaFila=""+filas;
        String cadenaCol=""+columnas;
        String cadenaSala=""+sala;
        if (sala<9) cadenaSala="0"+sala;
        if (filas<9) cadenaFila="0"+filas;
        if (columnas<9) cadenaCol="0"+columnas;
        String cadena=Sesion+cadenaSala+cadenaFila+cadenaCol;
        BarcodeQRCode barcodeQrcode = new BarcodeQRCode(cadena, 1, 1, null);
        Image qrcodeImage = barcodeQrcode.getImage();
        //      qrcodeImage.setAbsolutePosition(50, 50);
        qrcodeImage.scalePercent(300);
        document.add(qrcodeImage);
        document.add(new Paragraph("sala: "+sala));
        document.add(new Paragraph("Sesion: "+Sesion));
        document.add(new Paragraph("Fila: "+filas));
        document.add(new Paragraph("Columna: "+columnas));
        document.add(new Paragraph(new Date().toString()));
        
        //mandar ticket por correo
        String nombreFichero="C:\\Users\\jaliy\\Desktop\\"+cadenaFile+"_"+filas+"_"+columnas+".pdf";
        File ficheroPDF=new File(nombreFichero);
        Desktop.getDesktop().open(ficheroPDF);
        enviar_correo(nombreFichero);
        
        
    document.close();
    
    
  } catch (BadElementException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } catch (DocumentException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    }
  //EDIT start
 
  //EDIT end

          
      /*  OutputStream file=null;
    try {
        System.out.println("imprimir tick");
        String cadenaFile=Sesion.replaceAll("-", "_").replaceFirst(":", "_");
        file = new FileOutputStream(new File("C:\\Users\\David\\Desktop\\"+cadenaFile+"_"+filas+"_"+columnas+".pdf"));
        Document document = new Document();
         document.open();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, file);
       
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
       // PdfWriter.getInstance(document, file);
       // document.open();
        String cadenaFila="";
        String cadenaCol="";
        String cadenaSala="";
        if (sala<9) cadenaSala="0"+sala;
        if (filas<9) cadenaFila="0"+filas;
        if (columnas<9) cadenaCol="0"+columnas;
        String cadena=Sesion+cadenaSala+cadenaFila+cadenaCol;
        BarcodeQRCode barcodeQrcode = new BarcodeQRCode(cadena, 1, 1, null);
        Image qrcodeImage = barcodeQrcode.getImage();
        //      qrcodeImage.setAbsolutePosition(50, 50);
        qrcodeImage.scalePercent(300);
        document.add(qrcodeImage);
        document.add(new Paragraph("sala: "+sala));
        document.add(new Paragraph("Sesion: "+Sesion));
        document.add(new Paragraph("Fila: "+filas));
        document.add(new Paragraph("Columna: "+columnas));
        document.add(new Paragraph(new Date().toString()));
        //String nombreFichero="C:\\Users\\David\\Desktop\\"+cadenaFile+"_"+filas+"_"+columnas+".pdf";
        //   File ficheroPDF=new File(nombreFichero);
        
        //    Desktop.getDesktop().open(ficheroPDF);
        //enviar_correo(nombreFichero);
        
        
        document.close();
        file.close();
    } catch (FileNotFoundException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } catch (DocumentException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
          */
            
            
    
            
        }
         
        
    

    
    private void enviar_correo(String nombreFichero) {

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", "jaliyodiaz96@gmail.com");//mi correo
        props.put("mail.smtp.clave", "gmail de jalo5");    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google


        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");





        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        
        BodyPart texto = new MimeBodyPart();
        BodyPart adjunto = new MimeBodyPart();
        MimeMultipart multiParte = new MimeMultipart();
       
       
        try {
            message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user"),props.getProperty("mail.smtp.clave")));
            message.setRecipients(Message.RecipientType.BCC, props.getProperty("mail.smtp.user"));
            message.setSubject("Entradas ...");
            
            texto.setContent("cuerpo","text/html");

            //construct the pdf body part
            adjunto.setDataHandler(new DataHandler(new FileDataSource(nombreFichero)));
            adjunto.setFileName("fichero.pdf");
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);
            message.setContent(multiParte);
            
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", props.getProperty("mail.smtp.user"),props.getProperty("mail.smtp.clave"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
                    
        }catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex) { 
        Logger.getLogger(ControladorSalas.class.getName()).log(Level.SEVERE, null, ex);
    } 
    }
}