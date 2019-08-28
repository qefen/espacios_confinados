package com.example.kmartinez.espacios_confinados;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import java.util.Date;
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
            final TextView tv1 = (TextView) listItem.findViewById(R.id.tvNameE);
            final TextView tv2 = (TextView) listItem.findViewById(R.id.tvNss);
            final TextView tv3 = (TextView) listItem.findViewById(R.id.tvhr);
            final TextView tv4 = (TextView) listItem.findViewById(R.id.tvTimer);
            final TextView tv5 = (TextView) listItem.findViewById(R.id.tvLinea);

            final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             //   v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            //} else {
                //deprecated in API 26
            //    v.vibrate(500);
            //}


            //layoutElement.setBackgroundColor(Color.RED);

            TextView numero2 = (TextView) listItem.findViewById(R.id.tvNss);
            TextView hre2 = (TextView) listItem.findViewById(R.id.tvhr);

            final TextView hora2 = (TextView) listItem.findViewById(R.id.tvTimer);

            nombre2.setText(currentTrabajoConfinado.getRnombre());
            numero2.setText(currentTrabajoConfinado.getRnumeross());
            hre2.setText(currentTrabajoConfinado.getRhrEnt());
            hora2.setText(String.valueOf(currentTrabajoConfinado.getRhora()));

            //*******************
            //consulta para ver cuantos registros hay
            ConexionSQLiteHelper connection = new ConexionSQLiteHelper(getContext(), "eConfinados", null, 1);
            SQLiteDatabase baseTrabajadores = connection.getReadableDatabase();
            Cursor cursorTrabajadores = baseTrabajadores.rawQuery("SELECT hora from trabajador WHERE id_trabajador = "+currentTrabajoConfinado.getId_trabajador(), null);
            if(cursorTrabajadores.moveToFirst()) {
                String hora_inicio = cursorTrabajadores.getString(0);//aqui marca el error

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long difTimeMili = 0;
                try {
                    Date date_inicio = sdf.parse(hora_inicio);
                    Date date_actual = new Date();

                    Log.d("DateTimeInicio: ",hora_inicio);
                    Log.d("DateTimeActual: ",date_actual.toString());

                    Log.d("Hora actual: ",String.valueOf(date_actual.getTime()));
                    Log.d("Duración de la act: ",String.valueOf(currentTrabajoConfinado.getRhora()));

                    Log.d("Difetencia",String.valueOf(date_actual.getTime() - date_inicio.getTime()));
                    difTimeMili = currentTrabajoConfinado.getRhora() + 8000 - (date_actual.getTime() - date_inicio.getTime());

                }catch (java.text.ParseException e) {
                    Log.d("ParseException","No se pudo formatear la fecha");
                }
                CountDownTimer timerTrabajoConfinado = new CountDownTimer(difTimeMili, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //Log.d("Timer","seconds remaining: " + millisUntilFinished / 1000);
                        long millis = millisUntilFinished;
                        long hours = TimeUnit.MILLISECONDS.toHours(millis);
                        millis -= TimeUnit.HOURS.toMillis(hours);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                        millis -= TimeUnit.MINUTES.toMillis(minutes);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

                        String tiempoPendiente = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
                        hora2.setText(tiempoPendiente);
                    }
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onFinish() {
                        Log.d("Timer","Timer ended");
                        hora2.setText("00:00:00");
                        layoutElement.setBackgroundColor(Color.rgb(255,83,13));
                        tv1.setTextColor(Color.rgb(189,195,199));
                        tv2.setTextColor(Color.rgb(189,195,199));
                        tv3.setTextColor(Color.rgb(189,195,199));
                        tv4.setTextColor(Color.rgb(189,195,199));
                        v.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE));


                    }
                }.start();
            }
            cursorTrabajadores.close();
            //********************



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
                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
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
                                    tv5.setVisibility(View.INVISIBLE);
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
