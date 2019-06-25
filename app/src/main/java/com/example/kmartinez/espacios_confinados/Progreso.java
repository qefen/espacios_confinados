package com.example.kmartinez.espacios_confinados;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class Progreso extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.progresdialog,null);
        builder.setView(customView);

        builder.setTitle("Iniciando sesión")
                .setMessage("La informacion se esta validando, por favor espere un momento");
             /*   .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
              */
    return builder.create();

    }

}
