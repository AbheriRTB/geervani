package com.abheri.san.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by prasanna.ramaswamy on 27/03/16.
 */
public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if (!networkAvailable){
            Toast.makeText(
                    context,
                    "Network not available. Using stored data",
                    Toast.LENGTH_SHORT).show();
        }
        //return networkAvailable;
        return true;

    }
}
