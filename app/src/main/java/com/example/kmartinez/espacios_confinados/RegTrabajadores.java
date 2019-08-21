package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.kmartinez.espacios_confinados.utilidades.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class RegTrabajadores extends Fragment {
    EditText numseg;
    Button insertar, scanner;
    String numeroSeguro;

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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_reg_trabajadores, container, false);

        numseg = (EditText) view.findViewById(R.id.edtNss);
        insertar = (Button) view.findViewById(R.id.btnInsertar);
        scanner = (Button) view.findViewById(R.id.btnScanner);
        if (numseg.getText().toString().isEmpty()){
            insertar.setEnabled(false);
        }else{
            insertar.setEnabled(true);
        }



        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin(numseg.getText().toString());
                numeroSeguro = numseg.getText().toString();
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
        IntentIntegrator intent = IntentIntegrator.forSupportFragment(RegTrabajadores.this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("COLOQUE EL CODIGO QR EN EL CENTRO DE EL ESCANER");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), "ESCANER INICIADO", Toast.LENGTH_SHORT).show();
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(getContext(), "La lectura de informacion ha sido cancelada.", Toast.LENGTH_SHORT).show();

            } else {
                //convertimos el texto extraido del qr en cadena de texto se separa cada que encuentra una ,
                String[] num=result.getContents().toString().split(",");
                //quitamos espacios
                //numeroSeguro = num[0].trim();
                Toast.makeText(getContext(),result.getContents().toString(),Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void registrarTrabajador(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getContext(),"bd_trabajador",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        ///values.put(Utilidades.CAMPO_NOMBRE_ACT,nactividad.getText().toString());
        //values.put(Utilidades.CAMPO_AREA_ACT,narea.getText().toString());
        //values.put(Utilidades.CAMPO_LUGAR_ESP,lugare.getText().toString());
        //values.put(Utilidades.CAMPO_TIEMPO_MAX,resul_seg);
       // values.put(Utilidades.CAMPO_ESTADO_ACT,"habilitada");

        Long idResultante=db.insert(Utilidades.TABLA_ACTIVIDADES, Utilidades.CAMPO_ID,values);

        Toast.makeText(getContext(), "ID Registro: "+idResultante, Toast.LENGTH_SHORT).show();
        db.close();
    }
}
