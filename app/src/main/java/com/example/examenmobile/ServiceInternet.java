package com.example.examenmobile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import javax.annotation.Nullable;

public class ServiceInternet extends Service {
    public ServiceInternet() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("inside", "oncreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodicUpdate);
        return START_STICKY;
    }

    public boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            //CONECTADO A INTERNET
            return true;
        }else{
            //SIN INTERNET
            return false;
        }
    }

    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate,1*1000-SystemClock.elapsedRealtime()%1000);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.BroadCastStringForAction);
            broadcastIntent.putExtra("online_status", ""+isOnline(ServiceInternet.this));
            sendBroadcast(broadcastIntent);
        }
    };
}