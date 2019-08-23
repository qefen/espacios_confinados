package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    String  resul_seg;
    int cnt, cont;
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
        sp = (Spinner) view.findViewById(R.id.Spn1);
        String [] opciones = {"Hrs","Min"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_registo_actividad, opciones);
        sp.setAdapter(adapter);

        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(getContext(),"actividades",null,1 );
        SQLiteDatabase baseD = admin.getReadableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT * FROM actividad ;",null);
        cont = cursor.getCount();
        Toast.makeText(getContext(),"primer toast "+cnt,Toast.LENGTH_LONG).show();
        cursor.close();
        if (cont > 0){
            checar();
        }else{
            Toast.makeText(getContext(),"Ingresa Los Datos "+cnt,Toast.LENGTH_LONG).show();
        }


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
                }else {
                    Toast.makeText(getActivity(), "TODOS LOS CAMPOS DEBEN ESTAR LLENOS", Toast.LENGTH_SHORT).show();
                }
            }
        });





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

    private void checar(){
        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(getContext(),"actividades",null,1 );
        SQLiteDatabase baseD = admin.getReadableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT id_actividad FROM actividad WHERE estado = 'true';",null);
        cursor.moveToFirst();
        cnt = cursor.getInt(0);
        Toast.makeText(getContext(),"Comprobando "+cnt,Toast.LENGTH_LONG).show();
        cursor.close();

        if (cnt >= 1){
            Cursor c = baseD.rawQuery("SELECT nombre, area, luEsp, tiempoMax FROM actividad WHERE id_actividad = '"+cnt+"';",null);
            if(c.moveToFirst()){
                nactividad.setText(c.getString(0));
                narea.setText((c.getString(1)));
                lugare.setText(c.getString(2));
                tiempo.setText((c.getString(3)));
                c.close();
            } else{
                Toast.makeText(getContext(),"Quita este pinche error "+cnt,Toast.LENGTH_LONG).show();
                c.close();
            }


            nactividad.setEnabled(false);
            narea.setEnabled(false);
            lugare.setEnabled(false);
            tiempo.setEnabled(false);
            insertar.setEnabled(false);
            sp.setEnabled(false);
        }else{
            if (cnt == 0){
                nactividad.setEnabled(true);
                narea.setEnabled(true);
                lugare.setEnabled(true);
                tiempo.setEnabled(true);
                insertar.setEnabled(true);
                sp.setEnabled(true);
            }
        }
    }

    private void registrarActividad(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(),"actividades",null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();

        String nombAct = nactividad.getText().toString();
        String nombArea = narea.getText().toString();
        String lugarEsp = lugare.getText().toString();
        String tiempoAct = tiempo.getText().toString();
        String estadoAct = "true";
        //Toast.makeText(getContext(),nombAct,Toast.LENGTH_LONG).show();
        ContentValues registro = new ContentValues();
        registro.put("nombre",nombAct);
        registro.put("area",nombArea);
        registro.put("luEsp",lugarEsp);
        registro.put("tiempoMax",tiempoAct);
        registro.put("estado",estadoAct);

        baseD.insert("actividad",null, registro);
        baseD.close();

        nactividad.setEnabled(false);
        narea.setEnabled(false);
        lugare.setEnabled(false);
        tiempo.setEnabled(false);
        sp.setEnabled(false);
        Toast.makeText(getActivity(), "Registo Exitoso", Toast.LENGTH_SHORT).show();

    }
}
