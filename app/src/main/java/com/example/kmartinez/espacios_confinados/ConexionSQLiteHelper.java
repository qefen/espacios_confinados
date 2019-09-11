package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase EspaciosConfinados) {
        EspaciosConfinados.execSQL("CREATE TABLE actividad(id_actividad INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, area TEXT, luEsp TEXT, tiempoMax TEXT, estado TEXT, fecha_creacion TEXT)");
        EspaciosConfinados.execSQL("CREATE TABLE trabajador(id_trabajador INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, id_actividad INTEGER, numSegS TEXT, nombre TEXT, hora TEXT, hora_salida TEXT, estado TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
