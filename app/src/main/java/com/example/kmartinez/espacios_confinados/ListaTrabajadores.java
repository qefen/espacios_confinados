package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaTrabajadores extends AppCompatActivity {
    ListView listView;
    String nombre[] = {"kitzia yanez", "Marvin Tinoco", "Eleazar Saavedra", "Juliana Molina", "Alejandro Mamarre"};
    String numeross[] = {"455445342552", "4564534536456", "546456456546", "45634534533", "45645645346"};
    String hrEnt[] = {"23:56", "65:36", "4:57", "5:36", "10:16"};
    String hora[] = {"23:56", "65:36", "4:57", "5:36", "10:16"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_trabajadores);

        listView = findViewById(R.id.ListaT);

        MyAdapter adapter = new MyAdapter(this, nombre,numeross,hrEnt,hora);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                if (position == 0){
                    Toast.makeText(ListaTrabajadores.this, "trabajador1", Toast.LENGTH_SHORT);
                }
                if (position == 1){
                    Toast.makeText(ListaTrabajadores.this, "trabajador2", Toast.LENGTH_SHORT);
                }
                if (position == 2){
                    Toast.makeText(ListaTrabajadores.this, "trabajador3", Toast.LENGTH_SHORT);
                }
                if (position == 3){
                    Toast.makeText(ListaTrabajadores.this, "trabajador4", Toast.LENGTH_SHORT);
                }
                if (position == 4){
                    Toast.makeText(ListaTrabajadores.this, "trabajador5", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rnombre[];
        String rnumeross[];
        String rhrEnt[];
        String rhora[];


        public MyAdapter(Context c, String nombre1[], String numeross1[], String hrEnt1[], String hora1[]) {
            super(c, R.layout.lista_item_vista, R.id.tvNameE, nombre1);
            this.context = c;
            this.rnombre = nombre1;
            this.rnumeross = numeross1;
            this.rhrEnt = hrEnt1;
            this.rhora = hora1;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.lista_item_vista,parent,false);
            TextView nombre2 = item.findViewById(R.id.tvNameE);
            TextView numero2 = item.findViewById(R.id.tvNss);
            TextView hre2 = item.findViewById(R.id.tvhr);
            TextView hora2 = item.findViewById(R.id.tvTimer);

            nombre2.setText(rnombre[position]);
            numero2.setText(rnumeross[position]);
            hre2.setText(rhrEnt[position]);
            hora2.setText(rhora[position]);





            return item;
        }
    }
}
