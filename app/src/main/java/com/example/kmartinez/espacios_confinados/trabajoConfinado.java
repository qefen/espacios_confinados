package com.example.kmartinez.espacios_confinados;

/**
 * Clase la cual contiene los datos de los trabajadores, su actividad en la que
 * están trabajando y su tiempo de confinamiento.
 * TODO: Analizar qué otros posibles datos se piensan guardar
 */
public class trabajoConfinado {
    // Nombre de la actividad en el espacio confinado
    private String rnombre;
    // Seguro social del trabajador
    private String rnumeross;
    // La hora en la que entró el trabajador al espacio confinado
    private String rhrEnt;
    // Tiempo maximo (milisegundos) que tiene permitido el trabajador dentro del espacio confinado
    private long rhora;


    public trabajoConfinado(String rnombre, String rnumeross, String rhrEnt, long rhora) {
        this.rnombre = rnombre;
        this.rnumeross = rnumeross;
        this.rhrEnt = rhrEnt;
        this.rhora = rhora;
    }

    public String getRnombre() {
        return rnombre;
    }

    public void setRnombre(String rnombre) {
        this.rnombre = rnombre;
    }

    public String getRnumeross() {
        return rnumeross;
    }

    public void setRnumeross(String rnumeross) {
        this.rnumeross = rnumeross;
    }

    public String getRhrEnt() {
        return rhrEnt;
    }

    public void setRhrEnt(String rhrEnt) {
        this.rhrEnt = rhrEnt;
    }

    public long getRhora() {
        return rhora;
    }

    public void setRhora(long rhora) {
        this.rhora = rhora;
    }

}
