package com.example.kmartinez.espacios_confinados;

/**
 * Clase la cual contiene los datos de los trabajadores, su actividad en la que
 * están trabajando y su tiempo de confinamiento.
 * TODO: Analizar qué otros posibles datos se piensan guardar
 */
public class trabajoConfinado {
    private String rnombre;
    private String rnumeross;
    private String rhrEnt;
    private String rhora;

    public trabajoConfinado(String rnombre, String rnumeross, String rhrEnt, String rhora) {
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

    public String getRhora() {
        return rhora;
    }

    public void setRhora(String rhora) {
        this.rhora = rhora;
    }

}
