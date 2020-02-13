/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author David
 */
public class claseFacturado {
    private int indice;
    private int idSala;
    private int fila;
    private int columna;
    private String sesion;

    claseFacturado(int fila, int columna) {
      this.fila = fila;
        this.columna = columna;
    }

    claseFacturado() {

    }

   

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public claseFacturado(int indice, int idSala, int fila, int columna, String sesion) {
        this.indice = indice;
        this.idSala = idSala;
        this.fila = fila;
        this.columna = columna;
        this.sesion = sesion;
    }
}
