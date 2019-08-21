package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kmartinez.espacios_confinados.utilidades.Utilidades;


public class RegActividades extends Fragment {
    EditText nactividad, narea, lugare, tiempo;
    Button insertar;
    Spinner sp;
    String  resul_seg,estadoact;
    int tiempo_int;
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
        estadoact = "true";
        //Accion de el boton registrar
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Avisa que hay campos vacios
                intentoLogin(nactividad.getText().toString(), narea.getText().toString(), lugare.getText().toString(), tiempo.getText().toString());

                //se inicia cuando no hay ningun campo vacio
                if (op == false){
                    calcularTmax();
                    registrarActividad();
                    //Toast.makeText(getActivity(), "Esto funciona.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sp = (Spinner) view.findViewById(R.id.Spn1);
        String [] opciones = {"Hrs","Min"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_registo_actividad, opciones);
        sp.setAdapter(adapter);


        return view;
    }



    private void intentoLogin(String actividad, String area, String lugar, String tiempo2){
        boolean cancel = false;
        View focusView = null;

        //valiadar contraseña
        if (TextUtils.isEmpty(actividad)){
            nactividad.setError("Este campo es obligatorio");
            focusView = nactividad;
            cancel = true;
            op = true;
        }
        //Validar usuario
        if(TextUtils.isEmpty(area)){
            narea.setError("Este campo es obligatorio");
            focusView = narea;
            cancel = true;
            op = true;
        }
        if (TextUtils.isEmpty(lugar)){
            lugare.setError("Este campo es obligatorio");
            focusView = lugare;
            cancel = true;
            op = true;
        }
        //Validar usuario
        if(TextUtils.isEmpty(tiempo2)){
            tiempo.setError("Este campo es obligatorio");
            focusView = tiempo;
            cancel = true;
            op = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            op = false;
            //Mensaje de espera + inicio de tarea para login


            //Intent intent = new Intent(getActivity(), RegTrabajadores.class);
            //startActivity(intent);

        }

    }

    private void calcularTmax(){
        String seleccion = sp.getSelectedItem().toString();
        tiempo_int = (int) Integer.parseInt(tiempo.getText().toString());
        int conv;

        if (seleccion.equals("Hrs")){
            conv = (tiempo_int * 3600);
            resul_seg = String.valueOf(conv);
            Toast.makeText(getActivity(), "Tiempo en seg"+conv+"seg", Toast.LENGTH_SHORT).show();
        }else {
            if (seleccion.equals("Min")){
                conv = (tiempo_int * 60);
                resul_seg = String.valueOf(conv);
                Toast.makeText(getActivity(), "Tiempo en seg"+conv+"seg", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void registrarActividad(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getContext(),"bd_actividad",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_ACT,nactividad.getText().toString());
        values.put(Utilidades.CAMPO_AREA_ACT,narea.getText().toString());
        values.put(Utilidades.CAMPO_LUGAR_ESP,lugare.getText().toString());
        values.put(Utilidades.CAMPO_TIEMPO_MAX,resul_seg);
        values.put(Utilidades.CAMPO_ESTADO_ACT,estadoact.toString());

        Long idResultante=db.insert(Utilidades.TABLA_ACTIVIDADES, Utilidades.CAMPO_ID,values);

        Toast.makeText(getContext(), "ID Registro: "+idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }
}
