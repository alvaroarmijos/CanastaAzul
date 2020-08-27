package com.armijos.democanasta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FinalizarActivity extends AppCompatActivity {

    String total;
    private TextView tvTotal;
    List<Carrito> carritoList = new ArrayList<>();
    float precio, cantidad;
    float sumaparcial = 0;

    public Bundle args = new Bundle();


    private TextView tvUsuarioCedula;
    private TextView tvUsuarioEmpresa;
    private TextView tvUsuarioTit;
    private TextView tvUsuarioNombreFact;
    private TextView tvUsuarioDir;
    private TextView tvUsuarioRefDir;
    private TextView tvUsuarioCell;
    private TextView tvUsuarioPhone;
    private TextView tvUsuarioRefC;

    public String username;
    public String name;
    public String mail;
    public String nid;
    public String empresa_user;
    public String Tit;
    public String name_fact;
    public String direccion_fact;
    public String ref_dir_fact;
    public String cellphone_fact;
    public String phone_fact;
    public String reference_canasta;

    //Boton
    ImageView btnEditPerfil;
    Button btnFinalizarCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar);

        //Boton de regresar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Finalizar");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Calculamos el total de la compra

        SharedPreferences mPrefs = getSharedPreferences("carritolist", MODE_PRIVATE);
        String json = mPrefs.getString("lista", "");
        Gson gson = new Gson();
        if (json.isEmpty()) {
            total = "0$";

        } else {
            Type type = new TypeToken<ArrayList<Carrito>>() {
            }.getType();
            carritoList = gson.fromJson(json, type);
            for (Carrito carritoList : carritoList) {
                precio = Float.parseFloat(carritoList.getPrecio());
                //String precio = carritoList.getPrecio();
                cantidad = Float.parseFloat(carritoList.getCantidad());
                sumaparcial = sumaparcial + precio * cantidad;
            }
            total = Float.toString(sumaparcial);
        }

        //Cargamos los datos de facturacion
        mPrefs = getSharedPreferences("fact", MODE_PRIVATE);
        username = mPrefs.getString("usr", "");
        nid = mPrefs.getString("ced", "");
        empresa_user = mPrefs.getString("emp", "");
        Tit = mPrefs.getString("tit", "");
        name_fact = mPrefs.getString("namef", "");
        direccion_fact = mPrefs.getString("dir", "");
        ref_dir_fact = mPrefs.getString("ref", "");
        cellphone_fact = mPrefs.getString("cell", "");
        phone_fact = mPrefs.getString("phone", "");
        reference_canasta = mPrefs.getString("canasta", "");

        //Inflamos los datos
        tvTotal = (TextView) findViewById(R.id.textView_Total);
        tvTotal.setText(total + " $");

        //Datos de facturacion
        tvUsuarioCedula = (TextView) findViewById(R.id.textViewCed);
        tvUsuarioCedula.setText(nid);
        tvUsuarioEmpresa = (TextView) findViewById(R.id.textViewEmpresa);
        tvUsuarioEmpresa.setText(empresa_user);
        tvUsuarioTit = (TextView) findViewById(R.id.textViewTit);
        tvUsuarioTit.setText(Tit);
        tvUsuarioNombreFact = (TextView) findViewById(R.id.textViewNameFact);
        tvUsuarioNombreFact.setText(name_fact);
        tvUsuarioDir = (TextView) findViewById(R.id.textViewDir);
        tvUsuarioDir.setText(direccion_fact);
        tvUsuarioRefDir = (TextView) findViewById(R.id.textViewRef);
        tvUsuarioRefDir.setText(ref_dir_fact);
        tvUsuarioCell = (TextView) findViewById(R.id.textViewCell);
        tvUsuarioCell.setText(cellphone_fact);
        tvUsuarioRefC = (TextView) findViewById(R.id.textViewRefC);
        tvUsuarioRefC.setText(reference_canasta);
        tvUsuarioPhone = (TextView) findViewById(R.id.textViewPhone);
        tvUsuarioPhone.setText(phone_fact);

        //Boton
        btnEditPerfil = findViewById(R.id.button_EditarDatos);
        btnEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //colocamos informacion de facturacion

                Intent intent = new Intent(FinalizarActivity.this, EditarPerfil.class);
                intent.putExtra("username",username );
                intent.putExtra("nid",nid );
                intent.putExtra("empresa_user",empresa_user );
                intent.putExtra("Tit",Tit );
                intent.putExtra("name_fact",name_fact );
                intent.putExtra("direccion_fact",direccion_fact );
                intent.putExtra("ref_dir_fact",ref_dir_fact );
                intent.putExtra("cellphone_fact",cellphone_fact );
                intent.putExtra("phone_fact",phone_fact );
                intent.putExtra("reference_canasta",reference_canasta );
                startActivity(intent);

            }

        });
        btnFinalizarCompra=findViewById(R.id.button_finalizarCompra);
        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(FinalizarActivity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Si hay conexión a Internet en este momento
                    Toast.makeText(getApplicationContext(),"Compra finalizada", Toast.LENGTH_SHORT).show();
                    SharedPreferences mPrefs = getSharedPreferences("carritolist", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.clear().apply();
                } else {
                    Toast.makeText(getApplicationContext(),"Sin conexión a Internet", Toast.LENGTH_LONG).show();
                }

                Intent i = new Intent(FinalizarActivity.this, Principal.class);
                startActivity(i);

            }
        });




    }

}

