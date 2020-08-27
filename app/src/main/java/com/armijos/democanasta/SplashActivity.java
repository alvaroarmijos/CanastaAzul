package com.armijos.democanasta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    String nombre;
    String clave;
    //Contexto
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //recuperamos los datos de la memoria interna
        context=getApplicationContext();
        SharedPreferences mPrefs = context.getSharedPreferences("fact", context.MODE_PRIVATE);
        nombre = mPrefs.getString("usr", "");
        clave=mPrefs.getString("clave", "");

        //creamos los intent
        Intent IntentLogin = new Intent(this, MainActivity.class);
        Intent IntentPrincipal = new Intent(this,Principal.class);

        if (!nombre.isEmpty() && !clave.isEmpty()){
            startActivity(IntentPrincipal);
        } else {
            startActivity(IntentLogin);
        }
        finish();


    }


}
