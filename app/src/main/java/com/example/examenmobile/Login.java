package com.example.examenmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

        this.btnlogin.setOnClickListener(view -> {
            userLogin();
        });

        this.lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });

    }

    private void userLogin(){

        String mail = this.textemail.getText().toString();
        String password = this.textpassword.getEditText().getText().toString();



        if (TextUtils.isEmpty(mail)){

            textemail.setError("Ingrese un correo");
            textemail.requestFocus();


        }else if (TextUtils.isEmpty(password)) {

            Toast.makeText(Login.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            textpassword.requestFocus();
        }else{
            auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                public void onComplete(@NonNull Task<AuthResult> task){
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this,"Bienvenido",Toast.LENGTH_SHORT).show();


                        //se activa este pedazo de codigo cuando la actividad DashBoard este construida
                        //startActivity(new Intent(Login.this,DashBoard.class));
                    }else{

                        Toast.makeText(Login.this,"El usuario no existe",Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }


    }

    private void openRegisterActivity(){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }
}