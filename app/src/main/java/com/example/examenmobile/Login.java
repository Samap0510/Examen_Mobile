package com.example.examenmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText textemail;
    private TextInputLayout textpassword;
    private Button btnlogin;
    private TextView lblRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.textemail = findViewById(R.id.txtMail);
        this.textpassword = findViewById(R.id.txtPassword);
        this.btnlogin = findViewById(R.id.btnLogin);
        this.lblRegister = findViewById(R.id.lblRegister);

        this.auth = FirebaseAuth.getInstance();

        //En la acción de CADA botón, se hace un IF con la intencion de que valide la conectividad
        //si el metodo validarConexion es true, continua con su acción
        //En caso de ser false, retorna al MainActivity
        this.btnlogin.setOnClickListener(view -> {
            if(validarConexion()){
                userLogin();
            }else{
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        this.lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarConexion()){
                    openRegisterActivity();
                }else{
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void userLogin(){
        String mail = this.textemail.getText().toString();
        String password = this.textpassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(mail)){

            textemail.setError("Ingrese un correo");
            textemail.requestFocus();


        }else if (TextUtils.isEmpty(password)){

            auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                public void onComplete(@NonNull Task<AuthResult> task){
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this,"Bienvenido",Toast.LENGTH_LONG).show();

                        //se activa este pedazo de codigo cuando la actividad DashBoard este construida
                        //startActivity(new Intent(Login.this,DashBoard.class));
                    }
                }
            });

        }else{
            Intent intentMenu = new Intent(Login.this,MainMenu.class);
            startActivity(intentMenu);
        }
    }

    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
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