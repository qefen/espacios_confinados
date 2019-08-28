package com.example.kmartinez.espacios_confinados;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class trabajoConfinadoAdapter extends ArrayAdapter<trabajoConfinado> {

    private Context mContext;
    private List<trabajoConfinado> trabajoConfinadoList = new ArrayList<>();

    public trabajoConfinadoAdapter(@NonNull Context context, @LayoutRes ArrayList<trabajoConfinado> list){
        super(context, 0, list);
        mContext = context;
        trabajoConfinadoList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        RecyclerView.ViewHolder holder = null;

        View listItem = convertView;
        if(listItem == null) {

            listItem = LayoutInflater.from(mContext).inflate(R.layout.lista_item_vista,parent,false);

            final trabajoConfinado currentTrabajoConfinado = trabajoConfinadoList.get(position);

            final TextView nombre2 = (TextView) listItem.findViewById(R.id.tvNameE);
            // Modificación de la lista
            final LinearLayout layoutElement = (LinearLayout) listItem.findViewById(R.id.linearElement);
            //layoutElement.setBackgroundColor(Color.RED);

            TextView numero2 = (TextView) listItem.findViewById(R.id.tvNss);
            TextView hre2 = (TextView) listItem.findViewById(R.id.tvhr);

            final TextView hora2 = (TextView) listItem.findViewById(R.id.tvTimer);

            nombre2.setText(currentTrabajoConfinado.getRnombre());
            numero2.setText(currentTrabajoConfinado.getRnumeross());
            hre2.setText(currentTrabajoConfinado.getRhrEnt());
            hora2.setText(String.valueOf(currentTrabajoConfinado.getRhora()));

            CountDownTimer timerTrabajoConfinado = new CountDownTimer(currentTrabajoConfinado.getRhora(), 1000) {
                    public void onTick(long millisUntilFinished) {
                        Log.d("Timer","seconds remaining: " + millisUntilFinished / 1000);
                        long millis = millisUntilFinished;
                        long hours = TimeUnit.MILLISECONDS.toHours(millis);
                        millis -= TimeUnit.HOURS.toMillis(hours);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                        millis -= TimeUnit.MINUTES.toMillis(minutes);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

                        String tiempoPendiente = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
                        hora2.setText(tiempoPendiente);
                    }
                    public void onFinish() {
                        Log.d("Timer","Timer ended");
                        hora2.setText("00:00:00");
                        layoutElement.setBackgroundColor(Color.RED);

                    }
                }.start();


            listItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final View item = view;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder
                            .setMessage("¿El trabajador ha salido?")
                            .setPositiveButton("SI",  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //*******************
                                    //Actualización del trabajador
                                    ConexionSQLiteHelper connection = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
                                    SQLiteDatabase baseTrabajadores = connection.getReadableDatabase();
                                    String estado = "SALIO";
                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
                                    ContentValues registro = new ContentValues();
                                    registro.put("hora_salida",timeStamp);
                                    registro.put("estado",estado);
                                    int count = baseTrabajadores.update("trabajador",registro, "id_trabajador="+currentTrabajoConfinado.getId_trabajador(),null);
                                    if (count == 1){
                                        Log.d("Update trabajador","Actualización satisfactoria");
                                    }
                                    else {
                                        Log.d("Update trabajador","Actualización satisfactoria");
                                    }
                                    //********************
                                    item.setVisibility(View.INVISIBLE);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            })
                            .show();

                    Log.d("clickListener", " onclick  in listItem: " + currentTrabajoConfinado.getRnombre());





                }
            });


        }
        else {
            // Reclicla la vista
            holder = (RecyclerView.ViewHolder) convertView.getTag();
        }
        return listItem;
    }

}
