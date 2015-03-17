package com.example.roy.uqlibrarycomputers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Roy Portas on 18/03/15.
 */

public class AboutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Created by Roy Portas")
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        // Do something here
                    }
                });
        return builder.create();
    }
}