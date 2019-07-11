package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class RegTrabajadores extends Fragment {
    EditText numseg;
    Button insertar, scanner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_trabajadores, container, false);
        numseg = (EditText) view.findViewById(R.id.edtNss);
        insertar = (Button) view.findViewById(R.id.btnInsertar);
        scanner = (Button) view.findViewById(R.id.btnScanner);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin(numseg.getText().toString());
            }
        });
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            escaner();
            }
        });

        return view;
    }

    private void intentoLogin(String nseg){
        boolean cancel = false;
        View focusView = null;
        //Validar usuario
        if(TextUtils.isEmpty(nseg)){
            numseg.setError("Este campo es obligatorio");
            focusView = numseg;
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

    public void escaner(){
        IntentIntegrator intent = new IntentIntegrator(getActivity());
        intent.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        intent.setPrompt("ESCANEAR CODIGO");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(getActivity(), "La lectura de informacion ha sido cancelada.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(),result.getContents().toString(),Toast.LENGTH_LONG).show();
            }
        } else {

            // super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
