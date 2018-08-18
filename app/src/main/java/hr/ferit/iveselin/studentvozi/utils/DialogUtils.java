package hr.ferit.iveselin.studentvozi.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import hr.ferit.iveselin.studentvozi.R;

public class DialogUtils {
    public static void showDialog(Context from, final String address) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(from);
        alertBuilder.setMessage(address);

        alertBuilder.setPositiveButton(R.string.dialog_positive_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertBuilder.setNegativeButton(R.string.dialog_negative_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
