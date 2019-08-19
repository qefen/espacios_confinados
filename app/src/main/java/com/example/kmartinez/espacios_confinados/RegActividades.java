package com.example.kmartinez.espacios_confinados;

import android.content.Intent;
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


public class RegActividades extends Fragment {
    EditText nactividad, narea, lugare, tiempo;
    Button insertar;
    Spinner sp;

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
        insertar = (Button) view.findViewById(R.id.btnIngresar);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin(nactividad.getText().toString(), narea.getText().toString(), lugare.getText().toString(), tiempo.getText().toString());
            }
        });

        sp = (Spinner) view.findViewById(R.id.Spn1);
        String [] opciones = {"Hrs","Min"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_registo_actividad, opciones);
        sp.setAdapter(adapter);

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getContext(),"bd_actividad",null,1);
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
        }
        //Validar usuario
        if(TextUtils.isEmpty(area)){
            narea.setError("Este campo es obligatorio");
            focusView = narea;
            cancel = true;
        }
        if (TextUtils.isEmpty(lugar)){
            lugare.setError("Este campo es obligatorio");
            focusView = lugare;
            cancel = true;
        }
        //Validar usuario
        if(TextUtils.isEmpty(tiempo2)){
            tiempo.setError("Este campo es obligatorio");
            focusView = tiempo;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            //Mensaje de espera + inicio de tarea para login


            //Intent intent = new Intent(getActivity(), RegTrabajadores.class);
            //startActivity(intent);

        }

    }
}
