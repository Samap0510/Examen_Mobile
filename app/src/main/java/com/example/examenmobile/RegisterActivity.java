package com.example.examenmobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    public static final String BroadCastStringForAction="checkinternet";
    private IntentFilter mIntentFilter;

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

        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent=new Intent(this,ServiceInternet.class);
        startService(serviceIntent);

        if(isOnline(getApplicationContext())){
            setVisible_ON();
        }else {
            setVisible_OFF();
        }


        bRegistrar.setOnClickListener(view -> {
            if(isOnline(getApplicationContext())){
                crearUsuario();
            }else {
                setVisible_OFF();
            }
        });

        vLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline(getApplicationContext())){
                    actividadLogin();
                }else {
                    setVisible_OFF();
                }
            }
        });

    }



    public void actividadLogin(){

        //hola

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

    //----------------------------------------------------------------------------------------------
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

    }

    public void setVisible_OFF(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
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