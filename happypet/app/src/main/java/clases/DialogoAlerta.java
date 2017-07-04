package clases;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.happypet.movil.happypet.MascotaDetalleActivity;

/**
 * Created by Farly on 03/07/2017.
 */

public class DialogoAlerta extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("En esta primera fase debes completar tus datos para poder iniciar la adopción de la mascota seleccionada")
                .setTitle("FASE 1")
                .setPositiveButton("Comenzar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
                        ((MascotaDetalleActivity)getActivity()).PrimeraFaseClick();
                    }
                });

        return builder.create();
    }
}