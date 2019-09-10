package com.example.kmartinez.espacios_confinados;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
// TODO: Implementación del RecyclerView
public class ListaTrabajadores extends Fragment {
    private TrabajoConfinadoAdapter tCAdapter;
    int cnt;
    long tiempoMaximoActividad;
    ArrayList<TrabajoConfinado> listatrabajosConfinados = new ArrayList<TrabajoConfinado>();

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
        View view = inflater.inflate(R.layout.recyclerview_trabajo_confinado, container, false);

        RecyclerView rvTrabajoConfinado = (RecyclerView) view.findViewById(R.id.rvTConfinado);

        //*******************
        // Traer los trabajadores que están activos
        ConexionSQLiteHelper connection = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseTrabajadores = connection.getReadableDatabase();
        Cursor cursorTrabajadores = baseTrabajadores.rawQuery("SELECT t.numSegS, t.nombre, hora, act.tiempoMax, t.id_trabajador, act.id_actividad, t.estado  FROM trabajador t INNER JOIN actividad act ON act.id_actividad = t.id_actividad WHERE act.estado='true' AND t.estado != 'SALIO'", null);
        Log.d("Carga de trabajadores",String.valueOf(cursorTrabajadores.getCount()));

        if(cursorTrabajadores.moveToFirst()) {
            do {
                Log.d("nseg",cursorTrabajadores.getString(0));
                Log.d("nombre",cursorTrabajadores.getString(1));
                Log.d("hora",cursorTrabajadores.getString(2));
                Log.d("milis",cursorTrabajadores.getString(3));
                Log.d("estado",cursorTrabajadores.getString(6));


                String numeroseguro = cursorTrabajadores.getString(0);
                String Nombre = cursorTrabajadores.getString(1);
                String hora = cursorTrabajadores.getString(2);
                long tiempoMaximoActividad = cursorTrabajadores.getLong(3);
                int id_trabajador = cursorTrabajadores.getInt(4);
                int id_actividad= cursorTrabajadores.getInt(5);

                listatrabajosConfinados.add(new TrabajoConfinado(id_actividad, id_trabajador, Nombre, numeroseguro, hora, tiempoMaximoActividad * 1000));
            } while(cursorTrabajadores.moveToNext());
        }
        cursorTrabajadores.close();
        //********************

        tCAdapter = new TrabajoConfinadoAdapter(getContext(), listatrabajosConfinados);
        rvTrabajoConfinado.setAdapter(tCAdapter);
        // La manera en la que se distribuyen los elementos
        rvTrabajoConfinado.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvTrabajoConfinado.addItemDecoration(itemDecoration);




        return view;
    }
}