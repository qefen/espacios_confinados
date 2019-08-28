package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaTrabajadores extends Fragment {
    private ListView listView;
    private trabajoConfinadoAdapter tCAdapter;
    int cnt;
    long tiempoMaximoActividad;
    ArrayList<trabajoConfinado> listatrabajosConfinados = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_lista_trabajadores, container, false);
        listView = (ListView) view.findViewById(R.id.ListaT);

        //*******************
        //consulta para ver cuantos registros hay
        ConexionSQLiteHelper connection = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseTrabajadores = connection.getReadableDatabase();
        Cursor cursorTrabajadores = baseTrabajadores.rawQuery("SELECT t.numSegS, t.nombre, strftime('%H:%M',hora) AS hora, act.tiempoMax, t.id_trabajador, act.id_actividad  FROM trabajador t INNER JOIN actividad act ON act.id_actividad = t.id_actividad WHERE act.estado='true' AND t.estado = 'ENTRO'", null);
        Log.d("pre","pre");
        Log.d("count",String.valueOf(cursorTrabajadores.getCount()));

        if(cursorTrabajadores.moveToFirst()) {
            Log.d("count",String.valueOf(cursorTrabajadores.getCount()));
            do {

                Log.d("nseg",cursorTrabajadores.getString(0));
                Log.d("nombre",cursorTrabajadores.getString(1));
                Log.d("hora",cursorTrabajadores.getString(2));
                Log.d("milis",cursorTrabajadores.getString(3));

                String numeroseguro = cursorTrabajadores.getString(0);//aqui marca el error
                String Nombre = cursorTrabajadores.getString(1);
                String hora = cursorTrabajadores.getString(2);
                long tiempoMaximoActividad = cursorTrabajadores.getLong(3);
                int id_trabajador = cursorTrabajadores.getInt(4);
                int id_actividad= cursorTrabajadores.getInt(5);


                listatrabajosConfinados.add(new trabajoConfinado(id_trabajador, id_actividad, Nombre, numeroseguro, hora, tiempoMaximoActividad * 1000));

            } while(cursorTrabajadores.moveToNext());
        }
        cursorTrabajadores.close();
        //********************
            tCAdapter = new trabajoConfinadoAdapter(ListaTrabajadores.this.getContext(), listatrabajosConfinados);
            listView.setAdapter(tCAdapter);
        return view;
}
}
