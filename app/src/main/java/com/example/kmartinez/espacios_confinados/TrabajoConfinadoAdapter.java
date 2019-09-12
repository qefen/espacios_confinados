package com.example.kmartinez.espacios_confinados;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class TrabajoConfinadoAdapter extends RecyclerView.Adapter<TrabajoConfinadoAdapter.ViewHolder> {

    ConexionSQLiteHelper connection;
    SQLiteDatabase DB;
    Context context;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;

        LinearLayout wrapper;
        TextView nombreEmpleado;
        TextView numeroSeguroSocial;
        TextView horaEntrada;
        TextView tiempoPendiente;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            wrapper = (LinearLayout) itemView.findViewById(R.id.linearElement);
            nombreEmpleado = (TextView) itemView.findViewById(R.id.tvNameE);
            numeroSeguroSocial = (TextView) itemView.findViewById(R.id.tvNss);
            horaEntrada = (TextView) itemView.findViewById(R.id.tvhr);
            tiempoPendiente = (TextView) itemView.findViewById(R.id.tvTimer);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(final View v) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final TrabajoConfinado trabajoConfinado = mTrabajosConfinados.get(position);
                Log.d("Clicked",trabajoConfinado.getNombreTrabajador());
                Log.d("Clicked.activity",String.valueOf(trabajoConfinado.getIdActividad()));
                Log.d("Clicked.id_Trabajador",String.valueOf(trabajoConfinado.getIdTrabajador()));
                AlertDialog.Builder confirmarSacarTrabajador = new AlertDialog.Builder(v.getContext());
                confirmarSacarTrabajador
                        .setMessage("¿El trabajador ha salido?\n(" + trabajoConfinado.getNombreTrabajador() + ")")
                        .setPositiveButton("SI",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                sacarTrabajador(trabajoConfinado, position);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                if(trabajoConfinado.tieneTiempoPendiente()){
                    confirmarSacarTrabajador.show();
                }
                else {
                    sacarTrabajador(trabajoConfinado, position);
                }

            }
        }
        public void sacarTrabajador(TrabajoConfinado tc, int listPosition){
            //*******************
            //Actualización del trabajador
            ContentValues registro = new ContentValues();

            registro.put("hora_salida",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            registro.put("estado","SALIO");

            int count = DB.update("trabajador",registro, "id_trabajador=?",new String[]{String.valueOf(tc.getIdTrabajador())});

            if (count == 1){
                Log.d("Update trabajador","Actualización satisfactoria a "+tc.getNombreTrabajador());
                mTrabajosConfinados.remove(listPosition);
                notifyItemRemoved(listPosition);
                if (tc.timer != null) tc.timer.cancel();
            }
            else {
                Log.d("Update trabajador","No hubo actualización");
            }
            //********************
        }
    }

    private List<TrabajoConfinado> mTrabajosConfinados;

    public TrabajoConfinadoAdapter(Context context, List<TrabajoConfinado> trabajosConfinados) {
        this.context = context;
        connection =  new ConexionSQLiteHelper(context, "eConfinados", null, 1);
        DB = connection.getWritableDatabase();
        mTrabajosConfinados = trabajosConfinados;
        Log.d("Adapter","Object adapter Created");

    }

    // Genera la vista del xml solamente, regresando el Holder(Vista de cada elemento)
    @Override
    public TrabajoConfinadoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Holder","ViewHolderCreated");
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Genera la vista individual
        View espacioConfinadoView = inflater.inflate(R.layout.lista_item_vista, parent, false);

        // Regresa la instancia del holder
        ViewHolder viewHolder = new ViewHolder(parent.getContext(), espacioConfinadoView);
        return viewHolder;
    }

    // Rellena con datos de la BD
    @Override
    public void onBindViewHolder(final TrabajoConfinadoAdapter.ViewHolder viewHolder, int position) {
        // Obtiene el trabajo confinado
        final TrabajoConfinado trabajoconfinado = mTrabajosConfinados.get(position);
        Log.d("Bind","Binding data"+trabajoconfinado.getNombreTrabajador());
        Log.d("Bind: Hora entrada", trabajoconfinado.getHoraEntrada());
        Log.d("CountDown","Tiempo pendiente: " + trabajoconfinado.getTiempoRestanteMillis());

        // Muestra los datos del modelo en el elemento de la lista
        final LinearLayout listElement = viewHolder.wrapper;
        final TextView tvNombreEmpleado = viewHolder.nombreEmpleado;
        final TextView tvHoraEntrada = viewHolder.horaEntrada;
        final TextView tvNss = viewHolder.numeroSeguroSocial;
        final TextView tvTiempoPendiente = viewHolder.tiempoPendiente;

        tvNombreEmpleado.setText(trabajoconfinado.getNombreTrabajador());
        tvHoraEntrada.setText(trabajoconfinado.getHoraEntradaHHMM());
        tvNss.setText(trabajoconfinado.getNumeroSeguroSocial());
        tvTiempoPendiente.setText(trabajoconfinado.getTiempoRestanteHHMMSS());

        //CountDowmTimer
        if(trabajoconfinado.timer != null) {
            trabajoconfinado.timer.cancel();
        }
        // Si todavia no se vence el tiempo
        if (trabajoconfinado.tieneTiempoPendiente()) {
            Log.d("pendiente","tiene tiempo pendiente");

            listElement.setBackgroundColor(Color.TRANSPARENT); // Previene que el elemento se pinte de rojo

            trabajoconfinado.timer = new CountDownTimer(trabajoconfinado.getTiempoRestanteMillis(),1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTiempoPendiente.setText(trabajoconfinado.getTiempoRestanteHHMMSS());
                }

                @Override
                public void onFinish() {
                    Log.d("onFinish","triggered");
                    listElement.setBackgroundColor(Color.rgb(255,83,13));

                    tvTiempoPendiente.setText("00:00:00");
                    // NOTIFICACIÓN
                    //createNotification(trabajoconfinado.getIdTrabajador(),"Trabajo confinado","Tiempo terminado: " +trabajoconfinado.getNombreTrabajador(),"10");
                    // VIBRACIÓN
                    final Vibrator v = (Vibrator)  context.getSystemService(VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(600, 255));
                    } else {
                        ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(600);
                    }
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.soun_final);
                    mediaPlayer.start();
                }
            }.start();
        }
        else {
            listElement.setBackgroundColor(Color.rgb(255,83,13));
            tvTiempoPendiente.setText("00:00:00");
            if(trabajoconfinado.timer != null) {
                trabajoconfinado.timer.cancel();
            }
        }
    }
    @Override
    public int getItemCount() {
        return mTrabajosConfinados.size();

    }

    private void createNotification(int nId,String title, String body, String channelId) {
        // Usage: createNotification(trabajoconfinado.getIdTrabajador(),"Trabajo confinado","Tiempo terminado: " +trabajoconfinado.getNombreTrabajador(),"10");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context, channelId).setSmallIcon(R.drawable.mittal)
                .setContentTitle(title)
                .setContentText(body);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder.build());
    }
}
