package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaTrabajadores extends Fragment {
    private ListView listView;
    private trabajoConfinadoAdapter tCAdapter;

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
        View view = inflater.inflate(R.layout.activity_lista_trabajadores, container, false);
        listView = (ListView) view.findViewById(R.id.ListaT);
        ArrayList<trabajoConfinado> listatrabajosConfinados = new ArrayList<>();
        listatrabajosConfinados.add(new trabajoConfinado("kitzia yanez","455445342552","23:56",3 * 60 * 1000));
        listatrabajosConfinados.add(new trabajoConfinado("Marvin Tinoco","4564534536456","65:36",3 * 60 * 1000));
        listatrabajosConfinados.add(new trabajoConfinado("Eleazar Saavedra","45634534533","4:57",2 * 60 * 1000));
        listatrabajosConfinados.add(new trabajoConfinado("Juliana Molina","45634534533","5:36",2 * 60 * 1000));
        listatrabajosConfinados.add(new trabajoConfinado("Alejandro Mamarre","45645645346","10:16",1 * 30 * 1000));
        tCAdapter = new trabajoConfinadoAdapter(ListaTrabajadores.this.getContext(), listatrabajosConfinados);
        listView.setAdapter(tCAdapter);
        return view;
    }
}
