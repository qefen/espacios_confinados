package com.example.kmartinez.espacios_confinados;

import android.os.CountDownTimer;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Clase la cual contiene los datos de los trabajadores, su actividad en la que
 * están trabajando y su tiempo de confinamiento.
 * TODO: Analizar qué otros posibles datos se piensan guardar
 */
public class TrabajoConfinado {
    // Id de la actividad en el que el trabajador está trabajando
    private int idActividad;
    // Id del trabajador
    private int idTrabajador;
    // Nombre de la actividad en el espacio confinado
    private String nombreTrabajador;
    // Seguro social del trabajador
    private String numeroSeguroSocial;
    // timestamp de la hora en la que entró el trabajador al espacio confinado
    private String horaEntrada;
    // Tiempo maximo (segundos) que tiene permitido el trabajador dentro del espacio confinado
    private long duracionTrabajo;

    CountDownTimer timer;

    public TrabajoConfinado(int idActividad, int idTrabajador, String nombreTrabajador, String numeroSeguroSocial, String horaEntrada, long duracionTrabajo) {
        this.idActividad = idActividad;
        this.idTrabajador = idTrabajador;
        this.nombreTrabajador = nombreTrabajador;
        this.numeroSeguroSocial = numeroSeguroSocial;
        this.horaEntrada = horaEntrada;
        this.duracionTrabajo = duracionTrabajo;
    }
    public int getIdActividad() {
        return idActividad;
    }
    public void setIdActividad(int idActividad) {

        this.idActividad = idActividad;
    }
    public int getIdTrabajador() {

        return idTrabajador;
    }
    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;

    }
    public String getNombreTrabajador() {
        return nombreTrabajador;
    }
    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }
    public String getNumeroSeguroSocial() {
        return numeroSeguroSocial;

    }
    public void setNumeroSeguroSocial(String numeroSeguroSocial) {
        this.numeroSeguroSocial = numeroSeguroSocial;
    }
    public String getHoraEntrada() {
        return horaEntrada;

    }
    public String getHoraEntradaHHMM() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat horaMinutoFormat = new SimpleDateFormat("HH:mm");
        String horaMinutoEntrada;
        try {
            Date timestampEntrada = timestampFormat.parse(this.getHoraEntrada());
            horaMinutoEntrada = horaMinutoFormat.format(timestampEntrada);
            Log.d("Hora",horaMinutoEntrada);
        } catch(java.text.ParseException e) {
            Log.d("ParseExceptionHHMM","No se pudo formatear la fecha: " + this.getHoraEntrada());
            return "00:00";
        }
        return horaMinutoEntrada;
    }
    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;

    }
    public long getDuracionTrabajo() {
        return duracionTrabajo;

    }
    public void setDuracionTrabajo(long duracionTrabajo) {
        this.duracionTrabajo = duracionTrabajo;
    }
    public String getTiempoRestanteHHMMSS() {
        long millisUntilFinished = this.getTiempoRestanteMillis();

        long millis = millisUntilFinished;
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
    }
    public long getTiempoRestanteMillis() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nowInMillis = new Date().getTime();
        long horaEntradaInMillis = 0;
        try {
            horaEntradaInMillis = timestampFormat.parse(this.getHoraEntrada()).getTime();
        } catch(java.text.ParseException e) {
            Log.d("ParseException","No se pudo formatear la fecha: " + this.getHoraEntrada());
            return 0;
        }
        return this.getDuracionTrabajo() - (nowInMillis - horaEntradaInMillis);
    }
    public boolean tieneTiempoPendiente() {
        return this.getTiempoRestanteMillis() > 0 ;
    }
}
