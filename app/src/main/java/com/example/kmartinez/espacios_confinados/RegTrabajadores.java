package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.Calendar;
import java.util.Date;


public class RegTrabajadores extends Fragment {
    EditText numseg, nombemp;
    Button insertar, scanner;
    String numeroSeguro, nombree, time;
    int cntid;

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

        nombemp = (EditText) view.findViewById(R.id.edtNomEmp);
        numseg = (EditText) view.findViewById(R.id.edtNss);
        insertar = (Button) view.findViewById(R.id.btnInsertar);
        scanner = (Button) view.findViewById(R.id.btnScanner);
        hora();
        Toast.makeText(getActivity(), "This is The hour: " + time, Toast.LENGTH_SHORT).show();

        //comprueba si hay red
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //condiciones
        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
            nombemp.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Si hay internet", Toast.LENGTH_SHORT).show();
        } else {
            // No hay conexión a Internet en este momento
            nombemp.setVisibility(View.VISIBLE);
            nombree = nombemp.getText().toString();
            Toast.makeText(getActivity(), "No hay internet", Toast.LENGTH_SHORT).show();
        }

        //al hacer clic
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin(numseg.getText().toString());
                numeroSeguro = numseg.getText().toString();
                registrarTrabajador();
            }
        });
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escaner();
                registrarTrabajador();
            }
        });

        return view;
    }

    private void intentoLogin(String nseg) {
        boolean cancel = false;
        View focusView = null;
        //Validar usuario
        if (TextUtils.isEmpty(nseg)) {
            numseg.setError("Este campo es obligatorio");
            focusView = numseg;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            //Mensaje de espera + inicio de tarea para login


            //Intent intent = new Intent(getActivity(), RegTrabajadores.class);
            //startActivity(intent);
        }
    }

    public void hora() {
        Calendar calendario = Calendar.getInstance();
        long ahora = System.currentTimeMillis();
        calendario.setTimeInMillis(ahora);
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        time = hora + ":" + minuto;
    }

    public void escaner() {
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
                String[] num = result.getContents().toString().split(",");
                //quitamos espacios
                numeroSeguro = num[0].trim();
                nombree = num[2];
                Toast.makeText(getContext(), numeroSeguro + "--" + nombree, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void consultaid() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "actividades", null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT id_actividad FROM actividad WHERE estado = 'true';", null);
        cursor.moveToFirst();
        cntid = cursor.getInt(0);
        cursor.close();
    }

    private void registrarTrabajador() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "trabajador", null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();
        consultaid();
        String id_actividad = String.valueOf(cntid);
        String numeroSeguro = numseg.getText().toString();
        String nombre = nombree;
        String hora = time.toString();
        String estado = "ENTRO";
        ContentValues registro = new ContentValues();
        registro.put("id_actividad", id_actividad);
        registro.put("numSegS", numeroSeguro);
        registro.put("nombre", nombre);
        registro.put("hora", hora);
        registro.put("estado", estado);

        baseD.insert("trabajador", null, registro);
        baseD.close();

        Toast.makeText(getActivity(), "Registo Exitoso", Toast.LENGTH_SHORT).show();
        nombemp.setText("");
        numseg.setText("");
    }
}
