package com.hd.wallpaper.background.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hd.wallpaper.background.R;


public class NetworkManager
{
    public static boolean isInternetConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null)
        {
            NetworkInfo netinfo = cm.getActiveNetworkInfo();

            if (netinfo != null && netinfo.isConnectedOrConnecting()) {

                return true;
            }
        }

        return false;
    }

    public static boolean isWifiConnected(Context context)
    {
        // Create object for ConnectivityManager class which returns network
        // related info
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // If connectivity object is not null
        if (connectivity != null)
        {
            // Get network info - WIFI internet access
            NetworkInfo info = connectivity
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                // Look for whether device is currently connected to WIFI
                // network
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void internetSettings(final Context context)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.app_name);
        alertDialog
                .setMessage("Please Check Interner Connection");
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ((Activity)context).finish();
            }
        });
        alertDialog.show();
    }
}
