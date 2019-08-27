package com.example.kmartinez.espacios_confinados;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class trabajoConfinadoAdapter extends ArrayAdapter<trabajoConfinado> {

    private Context mContext;
    private List<trabajoConfinado> trabajoConfinadoList = new ArrayList<>();

    public trabajoConfinadoAdapter(@NonNull Context context, ArrayList<trabajoConfinado> list){
        super(context, 0, list);
        mContext = context;
        trabajoConfinadoList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.lista_item_vista,parent,false);

        trabajoConfinado currentTrabajoConfinado = trabajoConfinadoList.get(position);

        TextView nombre2 = (TextView) listItem.findViewById(R.id.tvNameE);
        TextView numero2 = (TextView) listItem.findViewById(R.id.tvNss);
        TextView hre2 = (TextView) listItem.findViewById(R.id.tvhr);
        TextView hora2 = (TextView) listItem.findViewById(R.id.tvTimer);

        nombre2.setText(currentTrabajoConfinado.getRnombre());
        numero2.setText(currentTrabajoConfinado.getRnumeross());
        hre2.setText(currentTrabajoConfinado.getRhrEnt());
        hora2.setText(currentTrabajoConfinado.getRhora());

        return listItem;
    }
}
