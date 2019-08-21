package com.example.kmartinez.espacios_confinados.utilidades;

public class Utilidades {

    //Constantes de campos tabla Actividades
    public static final String TABLA_ACTIVIDADES = "actividad";
    public static final String CAMPO_ID = "id_actividad";
    public static final String CAMPO_NOMBRE_ACT = "nombreAct";
    public static final String CAMPO_AREA_ACT= "areaAct";
    public static final String CAMPO_LUGAR_ESP= "lugarEsp";
    public static final String CAMPO_TIEMPO_MAX= "tiempoMax";
    public static final String CAMPO_ESTADO_ACT= "estadoAct";

    public static final String CREAR_TABLA_ACTIVIDAD="CREATE TABLE " +TABLA_ACTIVIDADES+" ( "
            +CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_ACT+" TEXT, "
            +CAMPO_AREA_ACT+" TEXT, "
            +CAMPO_LUGAR_ESP+" TEXT, "
            +CAMPO_TIEMPO_MAX+" INTEGER, "
            +CAMPO_ESTADO_ACT+" TEXT) ";

    //Constantes de campos tabla Trabajadores
    public static final String TABLA_TRABAJADOR = "trabajador";
    public static final String CAMPO_ID_TRABAJADOR = "id_trabajador";
    public static final String CAMPO_NSEGURO = "nseguro";
    public static final String CAMPO_H_ENTRADA= "hentrada";
    public static final String CAMPO_H_SALIDA= "hsalida";
    public static final String CAMPO_ID_ACT= "id_act";

    public static final String CREAR_TABLA_TRABAJADOR="CREATE TABLE " +TABLA_TRABAJADOR+" ( "
            +CAMPO_ID_TRABAJADOR+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NSEGURO+" TEXT, "
            +CAMPO_H_ENTRADA+" TEXT, "
            +CAMPO_H_SALIDA+" TEXT, "
            +CAMPO_ID_ACT+" INTEGER)";


}
