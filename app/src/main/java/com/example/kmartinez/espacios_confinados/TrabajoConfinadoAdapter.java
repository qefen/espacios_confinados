package com.example.kmartinez.espacios_confinados;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.CountDownTimer;
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

public class TrabajoConfinadoAdapter extends RecyclerView.Adapter<TrabajoConfinadoAdapter.ViewHolder> {

    ConexionSQLiteHelper connection;
    SQLiteDatabase DB;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CountDownTimer timer;
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

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder
                        .setMessage("¿El trabajador ha salido?")
                        .setPositiveButton("SI",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //*******************
                                //Actualización del trabajador
                                ContentValues registro = new ContentValues();

                                registro.put("hora_salida",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                registro.put("estado","SALIO");

                                int count = DB.update("trabajador",registro, "id_trabajador=?",new String[]{String.valueOf(trabajoConfinado.getIdTrabajador())});

                                if (count == 1){
                                    Log.d("Update trabajador","Actualización satisfactoria a "+trabajoConfinado.getNombreTrabajador());
                                    mTrabajosConfinados.remove(position);
                                    notifyItemRemoved(position);
                                }
                                else {
                                    Log.d("Update trabajador","No hubo actualización");
                                }
                                //********************

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
    }

    private List<TrabajoConfinado> mTrabajosConfinados;

    public TrabajoConfinadoAdapter(Context context, List<TrabajoConfinado> trabajosConfinados) {
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
        if(viewHolder.timer != null) {
            viewHolder.timer.cancel();
        }
        viewHolder.timer = new CountDownTimer(trabajoconfinado.getTiempoRestanteMillis(),1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              tvTiempoPendiente.setText(trabajoconfinado.getTiempoRestanteHHMMSS());
            }

            @Override
            public void onFinish() {
                listElement.setBackgroundColor(Color.rgb(255,83,13));
                tvTiempoPendiente.setText("00:00:00");
            }
        }.start();
    }
    @Override
    public int getItemCount() {
        return mTrabajosConfinados.size();

    }
}
