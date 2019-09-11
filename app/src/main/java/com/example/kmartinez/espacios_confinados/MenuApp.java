package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class MenuApp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String var1, var2;
    int act;
    EditText a1,a2,a3,a4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        a1 = (EditText) findViewById(R.id.edtNactividad);
        a2 = (EditText) findViewById(R.id.edtNarea);
        a3 = (EditText) findViewById(R.id.edtLugare);
        a4 = (EditText) findViewById(R.id.edtTiempo);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        boolean trabajadoresActivos = false;
        if (getIntent().hasExtra("trabajadoresActivos")) {
            trabajadoresActivos = getIntent().getExtras().getBoolean("trabajadoresActivos");
        }
        Log.d("IntentFragmentResult",String.valueOf(trabajadoresActivos));

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(trabajadoresActivos) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ListaTrabajadores()).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new RegActividades()).commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_RegAct) {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.contenedor, new RegActividades()).commit();
        } else if (id == R.id.nav_RegTrab) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new RegTrabajadores()).commit();
        } else if (id == R.id.nav_List) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ListaTrabajadores()).commit();
        } else if (id == R.id.nav_Guardar) {
            consultarActividad();
        } else if (id == R.id.nav_Enviar) {
            EnvioDatosServer data = new EnvioDatosServer(getBaseContext());
            data.sendData();
            Log.d("EnvioDatos","Envio de datos");
        } else if (id == R.id.nav_Salir) {
            // TODO: cancelar SharedPreferences

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void consultarActividad() {
        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "eConfinados", null, 1);
        SQLiteDatabase baseD = admin.getReadableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT id_actividad FROM actividad WHERE estado = 'true';", null);
        cursor.moveToFirst();
        var1 = cursor.getString(0);
        //Toast.makeText(this, "Comprobando " + var1 + var2, Toast.LENGTH_LONG).show();
        cursor.close();
        actualizarRegistro();
    }

    private void actualizarRegistro() {

        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "eConfinados", null, 1);
        SQLiteDatabase baseD = admin.getReadableDatabase();
        String varestado = "false";
        ContentValues registro = new ContentValues();
        registro.put("estado",varestado);
        int cantidad = baseD.update("actividad",registro, "id_actividad="+var1,null);

        if (cantidad == 1){
            Toast.makeText(this, "Los Articulos se modificaron", Toast.LENGTH_LONG).show();


        } else{
            Toast.makeText(this, "Los Articulos no se modificaron", Toast.LENGTH_LONG).show();
        }
    }
}
