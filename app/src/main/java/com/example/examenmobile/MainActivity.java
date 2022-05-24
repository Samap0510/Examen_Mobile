package com.example.examenmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        }
    }
}