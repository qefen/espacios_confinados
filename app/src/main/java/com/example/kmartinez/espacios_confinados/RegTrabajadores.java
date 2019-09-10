package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;


public class RegTrabajadores extends Fragment {
    EditText numseg, nombemp;
    Button insertar, scanner;
    boolean hasInternet;

    //declaramos variables que obtienen datos
    String numeroSeguro, nombree;
    int cntid, usrReg;

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

        //Toast.makeText(getActivity(), "This is The hour: " + time, Toast.LENGTH_SHORT).show();

        //comprueba si hay red
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //condiciones
        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
            hasInternet = true;
            nombemp.setVisibility(View.GONE);
            //Aqui mandamos a pedir el nombre de cierto numero de seguro
        } else {
            // No hay conexión a Internet en este momento
            hasInternet = false;
            nombemp.setVisibility(View.VISIBLE);
        }
        Log.d("RegTrabajadores","hasInternet: " + String.valueOf(hasInternet));

        //al hacer clic
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarNumSeg(numseg.getText().toString());
                if(!hasInternet){
                    numeroSeguro = numseg.getText().toString();
                    nombree = nombemp.getText().toString();
                    Toast.makeText(getActivity(), "Este es el nombre" + nombree, Toast.LENGTH_SHORT).show();
                    registrarTrabajador(numeroSeguro,nombree);
                }
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

    private void validarNumSeg(String nseg) {
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
            if(hasInternet){
                final DoActivity Tarea = new DoActivity(getContext());
                Tarea.execute(nseg);
            }
        }
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) throws java.lang.ArrayIndexOutOfBoundsException{
        //Toast.makeText(getActivity(), "ESCANER INICIADO", Toast.LENGTH_SHORT).show();
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                //Toast.makeText(getContext(), "La lectura de informacion ha sido cancelada.", Toast.LENGTH_SHORT).show();

            } else {
                //convertimos el texto extraido del qr en cadena de texto se separa cada que encuentra una ,
                String[] num = result.getContents().toString().split(",");
                //quitamos espacios
                try {
                    receptor(num[0].trim(), num[2]);
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(getActivity(), "Error en el escaneo. Intente nuevamente o ingrese NSS", Toast.LENGTH_LONG).show();

                }
                //
                // TODO: Validar que solo sean el tipo de credenciales que se manejan, y no otro tipo de código
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    public void receptor(String ns, String nom) {
        numeroSeguro = ns;
        nombree = nom;
        Toast.makeText(getContext(), numeroSeguro + "--" + nombree, Toast.LENGTH_SHORT).show();
        registrarTrabajador(numeroSeguro, nombree);
    }

    private void consultaid() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT id_actividad FROM actividad WHERE estado = 'true';", null);
        cursor.moveToFirst();
        cntid = cursor.getInt(0);
        cursor.close();
    }

    private void consulta_trabExist(String ns) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();
        consultaid();
        Log.d("consulta_trabExist","numero de Seguro: "+ns);
        Log.d("consulta_trabExist","contador id: "+cntid);
        Cursor cursor = baseD.rawQuery("SELECT id_trabajador FROM trabajador WHERE numSegS = "+ns+" AND estado = 'ENTRO' AND id_actividad = "+cntid+" ;", null);
        cursor.moveToFirst();
        usrReg = cursor.getCount();
        Log.d("consulta_trabExist","Contador: "+usrReg);
        cursor.close();
    }

    private void registrarTrabajador(String ns, String nom) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
        SQLiteDatabase baseD = conn.getWritableDatabase();
        //Toast.makeText(getContext(), "En el Registro: "+numeroSeguro + "--" + nombree, Toast.LENGTH_LONG).show();
        consultaid();
        consulta_trabExist(ns);

        int aux = usrReg;
        if (aux > 0) {
            Log.d("registrarTrabajador","Entro en la condicion");

            AlertDialog.Builder confirmarSacarTrabajador = new AlertDialog.Builder(this.getContext());
            confirmarSacarTrabajador
                    .setMessage("El trabajador ya se encuentra adentro del espacio confinado")
                    .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        } else {
            String id_actividad = String.valueOf(cntid);
            String numeroSeguro1 = ns;
            String nombre = nom;
            String hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String estado = "ENTRO";
            ContentValues registro = new ContentValues();
            registro.put("id_actividad", id_actividad);
            registro.put("numSegS", numeroSeguro1);
            registro.put("nombre", nombre);
            registro.put("hora", hora);
            registro.put("estado", estado);
            baseD.insert("trabajador", null, registro);
            baseD.close();

            //Toast.makeText(getActivity(), "Registo Exitoso", Toast.LENGTH_SHORT).show();
            nombemp.setText("");
            numseg.setText("");
        }
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    class DoActivity extends AsyncTask<String, String, String> {

        private Context context;
        protected Progreso progreso;
        protected Progreso progressResult;


        public DoActivity(Context context) {
            this.context = context;
        }


        protected void onPreExecute() {
            progreso = new Progreso("Busqueda de trabajador","Buscando la información. Por favor espere un momento...");
            progreso.show(getFragmentManager(),"Ejemplo");
        }

        protected void onPostExecute(String result) {
            progreso.dismiss();

            nombree = result;
            if(result.compareTo("")==0){

                AlertDialog.Builder busquedaTrabajador = new AlertDialog.Builder(getContext());
                busquedaTrabajador
                        .setMessage("Trabajador No Encontrado\nIntente de nuevo")
                        .setPositiveButton("Continuar",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();


            }
            else {
                registrarTrabajador(numseg.getText().toString(),result);
                AlertDialog.Builder busquedaTrabajador = new AlertDialog.Builder(getContext());
                busquedaTrabajador
                        .setMessage("Trabajador Encontrado\n(" + result + ")")
                        .setPositiveButton("Continuar",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }
        }

        @Override
        protected String doInBackground(String... args) {
            String data;
            String returndata = "Favor de revisar la conexión de su dispositivo.";

            String user = args[0];

            try {
                // Creating & connection Connection with url and required Header.
                data = "user="+user;
                data += "&function=get_name";
                data += "&version=082019v1_EspaciosConf";

                URL url = new URL("https://sissmac.arcelormittal.com.mx/logistics/AppMovil/espaciosConfinados/EspaciosConfinados.jsp?"+data);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");   //POST or GET
//                    urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.connect();

                // Write Request to output stream to server.
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                out.close();
                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();
                String statusMsg = urlConnection.getResponseMessage();

                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    returndata = dta.toString();
                    Log.d("Check", returndata);

                }
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d("Check1", returndata);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("Check2", returndata);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Check3", returndata);
            } catch (Exception e) {

                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
            }

            return returndata.trim();
        }


    }

}
