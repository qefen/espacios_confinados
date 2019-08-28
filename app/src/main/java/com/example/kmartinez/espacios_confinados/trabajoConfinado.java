package com.example.kmartinez.espacios_confinados;

/**
 * Clase la cual contiene los datos de los trabajadores, su actividad en la que
 * están trabajando y su tiempo de confinamiento.
 * TODO: Analizar qué otros posibles datos se piensan guardar
 */
public class trabajoConfinado {
    // Id de la actividad en el que el trabajador está trabajando
    private int id_actividad;
    // Id del trabajador
    private int id_trabajador;
    // Nombre de la actividad en el espacio confinado
    private String rnombre;
    // Seguro social del trabajador
    private String rnumeross;
    // La hora en la que entró el trabajador al espacio confinado
    private String rhrEnt;
    // Tiempo maximo (milisegundos) que tiene permitido el trabajador dentro del espacio confinado
    private long rhora;



    public trabajoConfinado(int id_trabajador, int id_actividad, String rnombre, String rnumeross, String rhrEnt, long rhora) {
        this.id_trabajador = id_trabajador;
        this.id_actividad = id_actividad;
        this.rnombre = rnombre;
        this.rnumeross = rnumeross;
        this.rhrEnt = rhrEnt;
        this.rhora = rhora;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public int getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
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
