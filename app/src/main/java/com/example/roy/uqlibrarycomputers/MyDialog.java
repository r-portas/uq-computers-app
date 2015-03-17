package com.example.roy.uqlibrarycomputers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Roy Portas on 18/03/15.
 */
public class MyDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final int[] colours = {R.string.blue, R.string.green};
        String[] options = {"Blue", "Green"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Colour");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.updateColours(getResources().getString(colours[which]));
            }
        });
        return builder.create();
    }
}