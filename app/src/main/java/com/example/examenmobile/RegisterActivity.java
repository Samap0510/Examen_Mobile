package com.example.examenmobile;

import android.content.Intent;
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

        this.btnLoguear.setOnClickListener(view -> {
            loguear();
        });
    }

    private void loguear(){
        Intent intentLoguear = new Intent(this, Login.class);
        startActivity(intentLoguear);
    }
}