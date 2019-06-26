package com.example.kmartinez.espacios_confinados;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class Progreso extends AppCompatDialogFragment {
    protected String title = "Iniciando Sesión";
    protected String message = "La informacion se esta validando, por favor espere un momento";
    protected boolean spinnerVisible = true;
    protected ProgressBar loading;


    public Progreso(String title, String message){
         this.title = title;
         this.message = message;
     }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.progresdialog,null);
        builder.setView(customView);
        loading = (ProgressBar) customView.findViewById(R.id.progressBar);

        if(!spinnerVisible) loading.setVisibility(View.GONE);

        builder.setTitle(title)
                .setMessage(message);
             /*   .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
              */
    return builder.create();

    }

}
