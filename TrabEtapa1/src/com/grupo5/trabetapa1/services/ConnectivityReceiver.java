package com.grupo5.trabetapa1.services;

import com.grupo5.trabetapa1.main.YambApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {
	boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(ConnectivityReceiver.class.getSimpleName(), "action: " + intent.getAction());
        haveNetworkConnection(context);
        ((YambApplication)context.getApplicationContext()).setNetworkAvailability(haveConnectedMobile, haveConnectedWifi);
    }
    
    private void haveNetworkConnection(Context context) {
        haveConnectedWifi = false;
        haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)   context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
    }
}
