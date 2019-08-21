package com.example.kmartinez.espacios_confinados.entidades;

public class trabajadores {
    private Integer id_trabajadores;
    private Integer id_actividad;
    private String nTrabajador;
    private String horaentrada;
    private String hprasalida;

    public trabajadores(Integer id_trabajadores, Integer id_actividad, String nTrabajador, String horaentrada, String hprasalida) {
        this.id_trabajadores = id_trabajadores;
        this.id_actividad = id_actividad;
        this.nTrabajador = nTrabajador;
        this.horaentrada = horaentrada;
        this.hprasalida = hprasalida;
    }

    public Integer getId_trabajadores() {
        return id_trabajadores;
    }

    public void setId_trabajadores(Integer id_trabajadores) {
        this.id_trabajadores = id_trabajadores;
    }

    public Integer getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(Integer id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getnTrabajador() {
        return nTrabajador;
    }

    public void setnTrabajador(String nTrabajador) {
        this.nTrabajador = nTrabajador;
    }

    public String getHoraentrada() {
        return horaentrada;
    }

    public void setHoraentrada(String horaentrada) {
        this.horaentrada = horaentrada;
    }

    public String getHprasalida() {
        return hprasalida;
    }

    public void setHprasalida(String hprasalida) {
        this.hprasalida = hprasalida;
    }
}
