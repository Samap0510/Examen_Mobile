package com.example.examenmobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvLoguear;
    private TextView btnLoguear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.btnLoguear = findViewById(R.id.loguear);


        //En la acción de CADA botón, se hace un IF con la intencion de que valide la conectividad
        //si el metodo validarConexion es true, continua con su acción
        //En caso de ser false, retorna al MainActivity
        this.btnLoguear.setOnClickListener(view -> {
            if(validarConexion()){
                loguear();
            }else{
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loguear(){
        Intent intentLoguear = new Intent(this, Login.class);
        startActivity(intentLoguear);
    }

    private boolean validarConexion(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}