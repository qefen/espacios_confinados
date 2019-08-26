package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class prueba extends Fragment {
    TextView tv1, tv2, tv3, tv4;
    Button consult;
    ConexionSQLiteHelper conn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prueba, container, false);

        conn = new ConexionSQLiteHelper(getContext(), "bd_actividad", null, 1);

        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);

        consult = (Button) view.findViewById(R.id.btnconsulta);

        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultar();
            }
        });


        return view;
    }

    private void consultar() {

        ConexionSQLiteHelper admin = new ConexionSQLiteHelper(getContext(), "actividades", null, 1);
        SQLiteDatabase baseD = admin.getReadableDatabase();
        Cursor cursor = baseD.rawQuery("SELECT id_actividad FROM actividad WHERE estado = 'true';", null);
        cursor.moveToFirst();
        int cnt = cursor.getInt(0);
        Toast.makeText(getContext(), "texto: " + cnt, Toast.LENGTH_LONG).show();
        cursor.close();


        //Cursor cursor = baseD.rawQuery("SELECT id_actividad FROM actividad WHERE estado = 'true';",new String[],{cate);
        //String codigo =  ;

        //Cursor fila =

        /*SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {"1"};
        String[] campos = {Utilidades.CAMPO_NOMBRE_ACT,Utilidades.CAMPO_AREA_ACT,Utilidades.CAMPO_LUGAR_ESP,Utilidades.CAMPO_TIEMPO_MAX};
        try{
            Cursor cursor = db.query(Utilidades.TABLA_ACTIVIDADES,campos,Utilidades.CAMPO_ID,parametros,null,null,null);
            cursor.moveToFirst();
            tv1.setText(cursor.getString(0));
            tv2.setText(cursor.getString(1));
            tv3.setText(cursor.getString(2));
            tv4.setText(cursor.getString(3));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(getContext(),"El Dato no Existe",Toast.LENGTH_LONG).show();
            limpiar();
        }*/
    }

    private void limpiar() {
        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
        tv4.setText("");
    }

}
