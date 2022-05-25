package com.example.examenmobile;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLoguear;
    private EditText eUsuario;
    private EditText eCorreo;
    private EditText eTelefono;
    private TextInputLayout iContraseña;
    private Button bRegistrar;
    private TextView vLogin;

    private String id;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        eUsuario = findViewById(R.id.txtUser);
        eCorreo = findViewById(R.id.txtMail);
        eTelefono = findViewById(R.id.txtPhone);
        iContraseña = findViewById(R.id.txtPassword);
        bRegistrar = findViewById(R.id.btnRegister);
        vLogin = findViewById(R.id.loguear);

        fAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        bRegistrar.setOnClickListener(view -> {
            if (validarConexion()){
                crearUsuario();

            }else{
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        });

        vLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actividadLogin();
            }
        });

    }



    public void actividadLogin(){

        Intent intent = new Intent(this, Login.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    public void crearUsuario(){

        String nombre = eUsuario.getText().toString();
        String correo = eCorreo.getText().toString();
        String telefono = eTelefono.getText().toString();
        String contraseña = iContraseña.getEditText().getText().toString();

        if(TextUtils.isEmpty(nombre)){

            eUsuario.setError("Ingrese un usuario");
            eUsuario.requestFocus();

        }
        if(TextUtils.isEmpty(correo)){

            eCorreo.setError("Ingrese un usuario");
            eCorreo.requestFocus();

        }
        else if(TextUtils.isEmpty(telefono)){

            eTelefono.setError("Ingrese un usuario");
            eTelefono.requestFocus();

        }
        else if(TextUtils.isEmpty(contraseña)){

            iContraseña.setError("Ingrese un usuario");
            iContraseña.requestFocus();

        }else{

            fAuth.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        id = fAuth.getCurrentUser().getUid();
                        DocumentReference dReferencia = db.collection("users").document(id);

                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("Nombre", nombre);
                        usuario.put("Correo", correo);
                        usuario.put("Telefono", telefono);
                        usuario.put("Contraseña", contraseña);

                        dReferencia.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "OnSuccess: Datos registrados:"+id);
                            }
                        });

                        Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this , Login.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else{

                        Toast.makeText(RegisterActivity.this,
                                "Usuario no registrado"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

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