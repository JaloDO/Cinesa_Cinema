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
public class claseCartelera {
    private int indice;
    private String titulo;
    
    private String sesion;
    private int sala;
    private byte[] cartel;

    claseCartelera() {
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public int getSala() {
        return sala;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public byte[] getCartel() {
        return cartel;
    }

    public void setCartel(byte[] cartel) {
        this.cartel = cartel;
    }

    public claseCartelera(int indice, String titulo, String sesion, int sala, byte[] cartel) {
        this.indice = indice;
        this.titulo = titulo;
        this.sesion = sesion;
        this.sala = sala;
        this.cartel = cartel;
    }
    @Override
public String toString() {
    return this.getSesion();
}
    
}
