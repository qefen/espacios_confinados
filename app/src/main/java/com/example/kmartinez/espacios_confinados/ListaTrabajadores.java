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



        //consulta para ver cuantos registros hay
        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(getContext(), "trabajador", null, 1);
        SQLiteDatabase baseD = admin.getReadableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT count(*) FROM trabajador;", null);
        cursor.moveToFirst();
        cnt = cursor.getInt(0);
        Toast.makeText(getContext(), "Cuantos Registros Hay: " + cnt, Toast.LENGTH_LONG).show();
        cursor.close();

        //consulta para ver el tiempo máximo permitido por actividad
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "actividades", null, 1);
        SQLiteDatabase baseDActividad = conn.getWritableDatabase();
        Cursor cursorActividad = baseDActividad.rawQuery("SELECT tiempoMax FROM actividad WHERE estado = 'true';", null);
        if(cursorActividad.moveToFirst()){
            tiempoMaximoActividad = cursorActividad.getLong(0);

            //ciclo del tamaño de los registros
            for (int i = 1; i <= cnt; i++) {
                //consulta que obtiene ciertos parametros de la bd
                Cursor c = baseD.rawQuery("SELECT numSegS, nombre, hora FROM trabajador WHERE id_trabajador = '" + i + "';", null);
                c.moveToFirst();
                //puede fallar
                //en la base de datos esta como integer, puede ser causa del error
                // Toast.makeText(getContext(), "-No Paso-", Toast.LENGTH_LONG).show();
                String numeroseguro = c.getString(0);//aqui marca el error
                //Toast.makeText(getContext(), "-paso-", Toast.LENGTH_LONG).show();

                String Nombre = c.getString(1);
                Toast.makeText(getContext(), Nombre, Toast.LENGTH_LONG).show();
                String hora = c.getString(2);

                listatrabajosConfinados.add(new trabajoConfinado(Nombre, numeroseguro, hora, tiempoMaximoActividad * 1000));
                c.close();
            }
            tCAdapter = new trabajoConfinadoAdapter(ListaTrabajadores.this.getContext(), listatrabajosConfinados);
            listView.setAdapter(tCAdapter);
        }
        cursorActividad.close();
        Log.d("Actividad","Duración:");



        return view;
}
}
