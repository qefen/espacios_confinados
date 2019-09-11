package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class RegActividades extends Fragment {
    EditText nactividad, narea, lugare, tiempo;
    Button insertar;
    Spinner sp;
    String resul_seg;
    int cnt, cont;
    int tiempo_int;
    int mul_tiem;
    boolean op = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_actividades, container, false);
        nactividad = (EditText) view.findViewById(R.id.edtNactividad);
        narea = (EditText) view.findViewById(R.id.edtNarea);
        lugare = (EditText) view.findViewById(R.id.edtLugare);
        tiempo = (EditText) view.findViewById(R.id.edtTiempo);
        insertar = (Button) view.findViewById(R.id.btnInsertar);
        sp = (Spinner) view.findViewById(R.id.Spn1);
        String[] opciones = {"Hrs", "Min"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_registo_actividad, opciones);
        sp.setAdapter(adapter);


        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseD = admin.getReadableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT * FROM actividad ;", null);
        cont = cursor.getCount();
        Log.d("RegActividades", "onCreateView: count actividades " + cont);
        cursor.close();
        // Si ya existe datos registrados de la actividad
        if (cont > 0) {
            checar();
        } else {
            Log.d("RegActividades", "onCreateView: Ingresa Los Datos " + cnt);
        }


        //Accion de el boton registrar
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Avisa que hay campos vacios
                intentoLogin(nactividad.getText().toString(), narea.getText().toString(), lugare.getText().toString(), tiempo.getText().toString());
                if (tiempo.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "El Valor de el tiempo no puede ser: 0", Toast.LENGTH_SHORT).show();
                    tiempo.setText("");
                } else {
                    //se inicia cuando no hay ningun campo vacio
                    if (op == false) {
                        calcularTmax();

                        //Toast.makeText(getActivity(), "Esto funciona.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "TODOS LOS CAMPOS DEBEN ESTAR LLENOS", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        return view;
    }


    private void intentoLogin(String actividad, String area, String lugar, String tiempo2) {
        boolean cancel = false;
        View focusView = null;

        //valiadar contraseña
        if (TextUtils.isEmpty(actividad)) {
            nactividad.setError("Este campo es obligatorio");
            focusView = nactividad;
            cancel = true;
            op = true;
        }
        //Validar usuario
        if (TextUtils.isEmpty(area)) {
            narea.setError("Este campo es obligatorio");
            focusView = narea;
            cancel = true;
            op = true;
        }
        if (TextUtils.isEmpty(lugar)) {
            lugare.setError("Este campo es obligatorio");
            focusView = lugare;
            cancel = true;
            op = true;
        }
        //Validar usuario
        if (TextUtils.isEmpty(tiempo2)) {
            tiempo.setError("Este campo es obligatorio");
            focusView = tiempo;
            cancel = true;
            op = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            op = false;
        }

    }

    public void calcularTmax() {
        String seleccion = sp.getSelectedItem().toString();
        tiempo_int = (int) Integer.parseInt(tiempo.getText().toString());
        int conv;

        if (seleccion.equals("Hrs")) {
            conv = (tiempo_int * 3600);
            resul_seg = String.valueOf(conv);
            Log.d("RegActividades", "Tiempo en seg" + conv + "seg");
        }
        if (seleccion.equals("Min")) {
            conv = (tiempo_int * 60);
            resul_seg = String.valueOf(conv);
            Log.d("RegActividades", "Tiempo en seg" + conv + "seg");
        }

        registrarActividad();
    }

    public void checar() {
        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseD = admin.getReadableDatabase();
        // Busca todas las actividades activas
        Cursor cursor = baseD.rawQuery("SELECT * FROM actividad WHERE estado = 'true';", null);
        cursor.moveToFirst();
        cnt = cursor.getCount();
        Log.d("RegActividades", "checar(): Comprobando actividades activas " + cnt);
        //Toast.makeText(getContext(), "Comprobando " + cnt, Toast.LENGTH_LONG).show();
        cursor.close();
        // Si cuenta con actividades activas, traer otra vez los datos de la actividad (?)
        if (cnt >= 1) {
            Cursor c = baseD.rawQuery("SELECT nombre, area, luEsp, tiempoMax FROM actividad WHERE estado = 'true';", null);
            if (c.moveToFirst()) {

                nactividad.setText(c.getString(0));
                narea.setText((c.getString(1)));
                lugare.setText(c.getString(2));

                int tim = Integer.parseInt(c.getString(3));


                long secons = tim;
                long hours = TimeUnit.SECONDS.toHours(secons);
                secons -= TimeUnit.HOURS.toSeconds(hours);
                long minutes = TimeUnit.SECONDS.toMinutes(secons);
                secons -= TimeUnit.MINUTES.toSeconds(minutes);

                String tim1 = String.format(Locale.ENGLISH, "%02d:%02d", hours, minutes);

                tiempo.setText("" + tim1);
                c.close();
            } else {
                c.close();
            }

            // Desactivar campos cuando ya hay datos
            nactividad.setEnabled(false);
            narea.setEnabled(false);
            lugare.setEnabled(false);
            tiempo.setEnabled(false);
            insertar.setEnabled(false);
            sp.setEnabled(false);
        } else {
            // Si no hay datos registrados, habilitar campos
            if (cnt == 0) {
                nactividad.setEnabled(true);
                narea.setEnabled(true);
                lugare.setEnabled(true);
                tiempo.setEnabled(true);
                insertar.setEnabled(true);
                sp.setEnabled(true);
            }
        }
    }

    private void registrarActividad() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();

        String nombAct = nactividad.getText().toString();
        String nombArea = narea.getText().toString();
        String lugarEsp = lugare.getText().toString();
        String tiempoAct = resul_seg.toString();
        String estadoAct = "true";
        String fechaCre = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        ContentValues registro = new ContentValues();
        registro.put("nombre", nombAct);
        registro.put("area", nombArea);
        registro.put("luEsp", lugarEsp);
        registro.put("tiempoMax", tiempoAct);
        registro.put("estado", estadoAct);
        registro.put("fecha_creacion",fechaCre);

        baseD.insert("actividad", null, registro);
        baseD.close();

        nactividad.setEnabled(false);
        narea.setEnabled(false);
        lugare.setEnabled(false);
        tiempo.setEnabled(false);
        sp.setEnabled(false);
        Toast.makeText(getActivity(), "Registo Exitoso", Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor, new RegTrabajadores());
        transaction.addToBackStack(null);
        // Commit a la transacción
        transaction.commit();
    }


}
