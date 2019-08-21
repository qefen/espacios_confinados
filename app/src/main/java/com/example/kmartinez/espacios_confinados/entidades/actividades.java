package com.example.kmartinez.espacios_confinados.entidades;

public class actividades {
    private Integer id_actividad;
    private String nombreAct;
    private String areaAct;
    private String lugarEsp;
    private Integer tiempoMax;
    private String estadoAct;

    public actividades(Integer id_actividad, String nombreAct, String areaAct, String lugarEsp, Integer tiempoMax, String estadoAct) {
        this.id_actividad = id_actividad;
        this.nombreAct = nombreAct;
        this.areaAct = areaAct;
        this.lugarEsp = lugarEsp;
        this.tiempoMax = tiempoMax;
        this.estadoAct = estadoAct;
    }

    public Integer getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(Integer id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getNombreAct() {
        return nombreAct;
    }

    public void setNombreAct(String nombreAct) {
        this.nombreAct = nombreAct;
    }

    public String getAreaAct() {
        return areaAct;
    }

    public void setAreaAct(String areaAct) {
        this.areaAct = areaAct;
    }

    public String getLugarEsp() {
        return lugarEsp;
    }

    public void setLugarEsp(String lugarEsp) {
        this.lugarEsp = lugarEsp;
    }

    public Integer getTiempoMax() {
        return tiempoMax;
    }

    public void setTiempoMax(Integer tiempoMax) {
        this.tiempoMax = tiempoMax;
    }

    public String getEstadoAct() {
        return estadoAct;
    }

    public void setEstadoAct(String estadoAct) {
        this.estadoAct = estadoAct;
    }
}
