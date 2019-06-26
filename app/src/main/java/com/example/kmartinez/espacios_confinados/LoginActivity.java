package com.example.kmartinez.espacios_confinados;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.nempleado);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intentoLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void intentoLogin(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        //valiadar contraseña
        if (TextUtils.isEmpty(password)){
            mPasswordView.setError("Se requiere Contraseña");
            focusView = mPasswordView;
            cancel = true;
        }
        //Validar usuario
        if(TextUtils.isEmpty(email)){
            mEmailView.setError("Este campo es Obligatorio");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            //Mensaje de espera + inicio de tarea para login


            final DoActivity Tarea = new DoActivity(LoginActivity.this);
            Tarea.execute(email, password);

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
        progreso = new Progreso("Iniciando Sesión","La información se está validando. Por favor espere un momento...");
        progreso.show(getSupportFragmentManager(),"Ejemplo");

    }

        protected void onPostExecute(String result) {
            progreso.dismiss();
            progressResult = new Progreso("","");


            if(result.compareTo("true")==0){
                Intent intent = new Intent(getApplicationContext(), Lector.class);
                startActivity(intent);
            }
            else{

                progressResult.title= "Error de autenticación";
                progressResult.message = result;
                progressResult.spinnerVisible = false;
                progressResult.show(getSupportFragmentManager(),"Ejemplo");


            }


        }

        @Override
        protected String doInBackground(String... args) {
            String data;
            String returndata = "Favor de revisar la conexión de su dispositivo.";

            String user = args[0];
            String pass = args[1];

            try {
                // Creating & connection Connection with url and required Header.
                data = "user="+user;
                data += "&password="+pass;
                data += "&function=get_auth";
                data += "&version=042019v1_SegSem";

                URL url = new URL("https://sissmac.arcelormittal.com.mx/logistics/AppMovil/AsistenciaEventos/Asistencia.jsp?"+data);
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

