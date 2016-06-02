package com.eowise.recyclerview.stickyheaders.samples;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sanjay on 12/29/2015.
 */
public class AlertDialog
{
    public void showAlertDialog(Context context) throws Exception {
       String title="Network Error",message="It seems you don't have Internet connection";

        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public Dialog loadingDialog(Context context)
    {
        final Dialog dialog = new Dialog(context,R.style.DialogSlideAnim3);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Comments");

        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.loading_dialog_layout);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#70be4d66")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
