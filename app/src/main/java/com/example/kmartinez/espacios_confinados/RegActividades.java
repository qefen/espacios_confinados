package com.example.kmartinez.espacios_confinados;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class RegActividades extends Fragment {
    EditText nactividad, narea, lugare, tiempo;
    Button insertar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_actividades, container, false);;
        nactividad = (EditText) view.findViewById(R.id.edtNactividad);
        narea = (EditText) view.findViewById(R.id.edtNarea);
        lugare = (EditText) view.findViewById(R.id.edtLugare);
        tiempo = (EditText) view.findViewById(R.id.edtTiempo);
        insertar = (Button) view.findViewById(R.id.btnIngresar);

        //Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin(nactividad.getText().toString(), narea.getText().toString(), lugare.getText().toString(), tiempo.getText().toString());
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
