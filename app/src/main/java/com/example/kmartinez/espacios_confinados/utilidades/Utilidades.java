package com.example.kmartinez.espacios_confinados.utilidades;

public class Utilidades {

    //Constantes de campos tabla Actividades
    public static final String TABLA_ACTIVIDADES = "actividad";
    public static final String CAMPO_ID = "id_actividad";
    public static final String CAMPO_NOMBRE_ACT = "nombreAct";
    public static final String CAMPO_AREA_ACT= "areaAct";
    public static final String CAMPO_LUGAR_ESP= "lugarEsp";
    public static final String CAMPO_TIEMPO_MAX= "tiempoMax";

    public static final String CREAR_TABLA_USUARIOS="CREATE TABLE "+TABLA_ACTIVIDADES+" ( "+CAMPO_ID+" INTEGER, "+CAMPO_NOMBRE_ACT+" TEXT, "+CAMPO_AREA_ACT+" TEXT, "+CAMPO_LUGAR_ESP+" TEXT, "+CAMPO_TIEMPO_MAX+" INTEGER)";



}
