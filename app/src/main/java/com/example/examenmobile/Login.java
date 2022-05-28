package com.example.examenmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText textemail;
    private TextInputLayout textpassword;
    private Button btnlogin;
    private TextView lblRegister;

    public static final String BroadCastStringForAction="checkinternet";
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.textemail = findViewById(R.id.txtMail);
        this.textpassword = findViewById(R.id.txtPassword);
        this.btnlogin = findViewById(R.id.btnLogin);
        this.lblRegister = findViewById(R.id.lblRegister);


        this.auth = FirebaseAuth.getInstance();


        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent=new Intent(this,ServiceInternet.class);
        startService(serviceIntent);
        if(isOnline(getApplicationContext())){
            setVisible_ON();
        }else {
            setVisible_OFF();
        }

        //En la acción de CADA botón, se hace un IF con la intencion de que valide la conectividad
        //si el metodo validarConexion es true, continua con su acción
        //En caso de ser false, retorna al MainActivity
        this.btnlogin.setOnClickListener(view -> {
            if(isOnline(getApplicationContext())){
                userLogin();
<<<<<<< HEAD
            }else{
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
=======
            }else {
                setVisible_OFF();
>>>>>>> 0fc6c04 (Agregué la conexión a internet como servicio)
            }
        });

        this.lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline(getApplicationContext())){
                    openRegisterActivity();
<<<<<<< HEAD
                }else{
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
=======
                }else {
                    setVisible_OFF();
>>>>>>> 0fc6c04 (Agregué la conexión a internet como servicio)
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


        }else if (TextUtils.isEmpty(password)) {

            Toast.makeText(Login.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            textpassword.requestFocus();
        }else{
            auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                public void onComplete(@NonNull Task<AuthResult> task){
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this,"Bienvenido",Toast.LENGTH_SHORT).show();

                        Intent intentMenu = new Intent(Login.this,MainMenu.class);
                        intentMenu.addFlags(intentMenu.FLAG_ACTIVITY_CLEAR_TASK | intentMenu.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentMenu);

                    }else{

                        Toast.makeText(Login.this,"El usuario no existe",Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }

    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }




    //-------------------------------------------

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
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
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