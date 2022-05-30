package com.example.examenmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainMenu extends AppCompatActivity {

    private Button btnCerrarSesion;
    private ImageButton btnLinterna;
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    TextView nombre, email;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    public static final String BroadCastStringForAction="checkinternet";
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mFirestore = FirebaseFirestore.getInstance();

        this.auth = FirebaseAuth.getInstance();

        this.btnCerrarSesion = findViewById(R.id.buttonCerrarSeasion);
        this.btnLinterna = findViewById(R.id.imgBLinterna);

        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent=new Intent(this,ServiceInternet.class);
        startService(serviceIntent);
        if(isOnline(getApplicationContext())){
            setVisible_ON();
        }else {
            setVisible_OFF();
        }

        nombre = findViewById(R.id.name);
        email = findViewById(R.id.email);

        Bundle recibo = getIntent().getExtras();
        String valor = recibo.getString("valor");
        String correo = recibo.getString("email");

        if(valor.equals("true")){

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            gsc= GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account!=null){
                String Name = account.getDisplayName();
                String Email = account.getEmail();

                nombre.setText(Name);
                email.setText(Email);
            }




        }else{

           // Query query = mFirestore.collection("users");
            String hola = "hola1";
            nombre.setText(hola.toString());
            System.out.println("hola ");
            Toast.makeText(MainMenu.this,"Hola1",Toast.LENGTH_SHORT).show();

            mFirestore.collection("users")
                    .whereEqualTo("Correo", correo.toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    nombre.setText(document.getString("Nombre"));
                                    email.setText(document.getString("Correo"));
                                }

                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });



        }

        String hola = "hola3";
        //nombre.setText(hola.toString());


        this.btnCerrarSesion.setOnClickListener(view -> {
            if(isOnline(getApplicationContext())){
                if(valor.equals(true)){
                    SignOut();
                }else{

                    cerrarSesion();
                }


            }else {
                setVisible_OFF();
            }
        });

        linterna();
    }

    private void cerrarSesion(){
        auth.signOut();
        Intent intentCerrarSesion = new Intent(MainMenu.this,Login.class);
        intentCerrarSesion.addFlags(intentCerrarSesion.FLAG_ACTIVITY_CLEAR_TASK | intentCerrarSesion.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentCerrarSesion);
    }

    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
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