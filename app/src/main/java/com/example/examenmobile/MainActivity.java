package com.example.examenmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.auth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        System.out.println(user);
        boolean internetAuth = true;

        // Jeison debera en esta parte hacer la verificacion del internet y darle un valor a la variable internetAuth
        // para que ingrese al login o que mande un mensaje de error
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //Valida si hay coneccion a internet y muestra el mensaje respectivo
        if(networkInfo != null && networkInfo.isConnected()){
            //Conectado a internet
            internetAuth = true;
        }else{
            //SIN conexión a Internet
            internetAuth = false;
        }


        if (internetAuth==true) {


            if (user == null) {
                startActivity(new Intent(MainActivity.this, Login.class));

            } else {
                // este else funcionara cuando se cree la actividad Dasboard, este else sirve para cuando el usuario esta logueado
                // ingrese directamente a el menu principal
                // startActivity(new Intent(MainActivity.this, Dasboard.clas));

            }
        }else{
            // en este else deberia de mandar un mensaje de que no esta conectado a internet y que deberia de conectarse o
            // si el usuario quiere utilizar datos
            Toast.makeText(this, "Revise su conección a INTERNET", Toast.LENGTH_SHORT).show();

        }
    }
}