package com.example.examenmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenmobile.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ImageView imgNoInternet;
    private Button btnReconectar;

    public static final String BroadCastStringForAction="checkinternet";

    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.auth = FirebaseAuth.getInstance();
        imgNoInternet = findViewById(R.id.imageViewNoInternet);
        btnReconectar = findViewById(R.id.buttonReconectar);
        imgNoInternet.setVisibility(View.INVISIBLE);
        btnReconectar.setVisibility(View.INVISIBLE);

        //----------service-------------
        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent=new Intent(this,ServiceInternet.class);
        startService(serviceIntent);
        if(isOnline(getApplicationContext())){
            setVisible_ON();
        }else {
            setVisible_OFF();
        }
    }

    public BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BroadCastStringForAction)){
                if(intent.getStringExtra("online_status").equals("true")){
                    setVisible_ON();
                }else {
                    setVisible_OFF();
                }
            }
        }
    };

    public boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //Valida si hay coneccion a internet y muestra el mensaje respectivo
        if(networkInfo != null && networkInfo.isConnected()){
            //Conectado a internet
            return true;
        }else{
            //SIN conexión a Internet
            return false;
        }
    }

    public void setVisible_ON(){
        imgNoInternet.setVisibility(View.INVISIBLE);
        btnReconectar.setVisibility(View.INVISIBLE);
        FirebaseUser user = auth.getCurrentUser();
        System.out.println(user);

        // Jeison debera en esta parte hacer la verificacion del internet y darle un valor a la variable verificarInternet
        // para que ingrese al login o que mande un mensaje de error

        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));

        } else {
            startActivity(new Intent(MainActivity.this, MainMenu.class));
            // este else funcionara cuando se cree la actividad Dasboard, este else sirve para cuando el usuario esta logueado
            // ingrese directamente a el menu principal
            // startActivity(new Intent(MainActivity.this, Dasboard.clas));
        }

    }

    public void setVisible_OFF(){
        imgNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(MyReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiver,mIntentFilter);
    }
}