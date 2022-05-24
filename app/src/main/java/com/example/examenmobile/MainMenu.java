package com.example.examenmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    private Button btnCerrarSesion;
    private ImageButton btnLinterna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.btnCerrarSesion = findViewById(R.id.buttonCerrarSeasion);
        this.btnLinterna = findViewById(R.id.imgBLinterna);

        this.btnCerrarSesion.setOnClickListener(view -> {
            cerrarSesion();
        });

        linterna();
    }

    private void cerrarSesion(){
        Intent intentCerrarSesion = new Intent(MainMenu.this,Login.class);
        startActivity(intentCerrarSesion);
    }

    private void linterna(){
        ImageButton blinterna = (ImageButton) findViewById(R.id.imgBLinterna);
        CameraManager cm =(CameraManager) getSystemService(Context.CAMERA_SERVICE);
        blinterna.setImageResource(R.drawable.floff);
        final String[] cameraId = {null};

        final boolean[] turn = {false};

        blinterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accion del Boton

                //Primer If funciona para encerder la linterna
                if(!turn[0]){
                    try {
                        cameraId[0] = cm.getCameraIdList()[0];
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cm.setTorchMode(cameraId[0], true);//true para encerder
                            blinterna.setImageResource(R.drawable.flon);
                            turn[0] = true;
                        }

                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }else {//aqui en el else es por si esta prendida, para apagarla
                    try {
                        cameraId[0] = cm.getCameraIdList()[0];
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cm.setTorchMode(cameraId[0], false);//false para apagar
                            blinterna.setImageResource(R.drawable.floff);
                            turn[0] = false;
                        }

                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}