package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import javax.net.ssl.HttpsURLConnection;


public class EnvioDatosServer {
    ConexionSQLiteHelper connection;
    SQLiteDatabase DB;
    Context context;
    JSONObject dataForm;

    public EnvioDatosServer(Context context) {
        this.context = context;
        connection =  new ConexionSQLiteHelper(this.context, "eConfinados", null, 1);
        DB = connection.getReadableDatabase();
    }

    public void sendData() {
        dataForm = JSONBuild();
        EnvioDatosServer.sendServerDB dataUpload = new EnvioDatosServer.sendServerDB(context);
        dataUpload.execute(dataForm);
    }
    /***
     * GENERA EL JSON QUE SE ENVIA AL SERVIDOR:
     * {
     *     empleadoEncargado : "",
     *     actividades: [{
     *         nombreActividad : "",
     *         area : "",
     *         lugarEspecifico : "",
     *         duracion : "",
     *         fechaCreacion : "",
     *         trabajadores : [{
     *             nombre : "",
     *             numsegS : "",
     *             horaEntrada : "",
     *             horaSalida : ""
     *         },{...}]
     *     },{...}]
     * }
     *
     * */
    public JSONObject JSONBuild() {
        JSONObject trabajosEspaciosConfinados = new JSONObject();
        SharedPreferences credentialsPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String empleadoEncargado = credentialsPreferences.getString("nEmpleado","undefined");
        try {
            trabajosEspaciosConfinados.put("empleadoEncargado", empleadoEncargado);
            JSONArray actividades = new JSONArray();
            Cursor actividadCursor = DB.rawQuery("SELECT nombre, area, luEsp, tiempoMax,fecha_creacion, id_actividad FROM actividad",null);
            if(actividadCursor.moveToFirst()) {
                do {
                    JSONObject actividad = new JSONObject();
                    actividad.put("nombreActividad", actividadCursor.getString(0));
                    actividad.put("area", actividadCursor.getString(1));
                    actividad.put("lugarEspecifico", actividadCursor.getString(2));
                    actividad.put("duracion", actividadCursor.getString(3));
                    actividad.put("fechaCreacion", actividadCursor.getString(4));

                    //actividad.put("fechaCreacion", "2019-08-08 13:40:32");
                    // ...
                    JSONArray trabajadores = new JSONArray();
                    Cursor trabajadorCursor = DB.rawQuery("SELECT nombre, numSegS, hora, hora_salida FROM trabajador WHERE id_actividad = ?", new String[]{actividadCursor.getString(5)});
                    if(trabajadorCursor.moveToFirst()) {
                        do {
                            JSONObject trabajador = new JSONObject();
                            trabajador.put("nombre", trabajadorCursor.getString(0));
                            trabajador.put("numsegS ", trabajadorCursor.getString(1));
                            trabajador.put("horaEntrada", trabajadorCursor.getString(2));
                            trabajador.put("horaSalida", trabajadorCursor.getString(3));
                            trabajadores.put(trabajador);
                        } while (trabajadorCursor.moveToNext());
                    }
                    actividad.put("trabajadores", trabajadores);
                    actividades.put(actividad);
                } while (actividadCursor.moveToNext());
            }
            trabajosEspaciosConfinados.put("actividades", actividades);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        DB.close();
        return trabajosEspaciosConfinados;
    }

    class sendServerDB extends AsyncTask<JSONObject, String, String> {

        private Context context;

        public sendServerDB(Context context) {
            this.context = context;
        }


        protected void onPreExecute() {
            Toast.makeText(context, "Enviando datos, por favor espere", Toast.LENGTH_SHORT).show();

        }

        protected void onPostExecute(String result) {
            if(Boolean.valueOf(result)){
                Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                ConexionSQLiteHelper connection =  new ConexionSQLiteHelper(context, "eConfinados", null, 1);
                SQLiteDatabase DB = connection.getWritableDatabase();
                DB.execSQL("DELETE FROM trabajador;");
                DB.execSQL("DELETE FROM actividad;");
                DB.close();
                Log.d("Delete","Datos borrados");
            }
            else {
                Toast.makeText(context, "Estado: " + result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(JSONObject... SQLiteDatabaseJSON) {
            String data;
            String returndata = "";
            JSONObject JSONData = SQLiteDatabaseJSON[0];
            Log.d("JSONObject",JSONData.toString());
            try {
                // Creating & connection Connection with url and required Header.
                data = "function=set_data";
                data += "&version=082019v1_EspaciosConf";

                URL url = new URL("https://sissmac.arcelormittal.com.mx/logistics/AppMovil/espaciosConfinados/EspaciosConfinados.jsp?"+data);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                StringBuffer ds;
                // Write Request to output stream to server.
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(JSONData.toString());
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
