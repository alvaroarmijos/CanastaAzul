package com.armijos.democanasta;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditarPerfil extends AppCompatActivity {

    private EditText tvUsuarioCedula;
    private EditText tvUsuarioEmpresa;
    private EditText tvUsuarioTit;
    private EditText tvUsuarioNombreFact;
    private EditText tvUsuarioDir;
    private EditText tvUsuarioRefDir;
    private EditText tvUsuarioCell;
    private EditText tvUsuarioPhone;
    private EditText tvUsuarioRefC;

    //Boton
    Button GuardarCambios;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //direccion servicio web
    private static final String REGISTER_URL = "http://canastaazul.atwebpages.com/editUsuario.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    // JSON Node names
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
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editarperfil);

        username = getIntent().getStringExtra("username");
        nid = getIntent().getStringExtra("nid");
        empresa_user = getIntent().getStringExtra("empresa_user");
        Tit = getIntent().getStringExtra("Tit");
        name_fact = getIntent().getStringExtra("name_fact");
        direccion_fact = getIntent().getStringExtra("direccion_fact");
        ref_dir_fact = getIntent().getStringExtra("ref_dir_fact");
        cellphone_fact = getIntent().getStringExtra("cellphone_fact");
        phone_fact = getIntent().getStringExtra("phone_fact");
        reference_canasta = getIntent().getStringExtra("reference_canasta");




        //informacion de facturacion
        tvUsuarioCedula = (EditText) findViewById(R.id.textViewCed);
        tvUsuarioCedula.setText(nid);
        tvUsuarioEmpresa = (EditText) findViewById(R.id.textViewEmpresa);
        tvUsuarioEmpresa.setText(empresa_user);
        tvUsuarioTit = (EditText) findViewById(R.id.textViewTit);
        tvUsuarioTit.setText(Tit);
        tvUsuarioNombreFact = (EditText) findViewById(R.id.textViewNameFact);
        tvUsuarioNombreFact.setText(name_fact);
        tvUsuarioDir = (EditText) findViewById(R.id.textViewDir);
        tvUsuarioDir.setText(direccion_fact);
        tvUsuarioRefDir = (EditText) findViewById(R.id.textViewRef);
        tvUsuarioRefDir.setText(ref_dir_fact);
        tvUsuarioCell = (EditText) findViewById(R.id.textViewCell);
        tvUsuarioCell.setText(cellphone_fact);
        tvUsuarioRefC = (EditText) findViewById(R.id.textViewRefC);
        tvUsuarioRefC.setText(reference_canasta);
        tvUsuarioPhone = (EditText) findViewById(R.id.textViewPhone);
        tvUsuarioPhone.setText(phone_fact);

        //Boton
        GuardarCambios=findViewById(R.id.ButtonGuardar);
        GuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(EditarPerfil.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Si hay conexión a Internet en este momento
                    new ModificarUser().execute();
                } else {
                    // No hay conexión a Internet en este momento
                    Toast.makeText(EditarPerfil.this,"Sin conexión a Internet",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EditarPerfil.this,Principal.class);
                    startActivity(i);
                }

            }
        });





    }

    class ModificarUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Modificando Datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;


            //informacion de facturacion cedula, empresa, titulo, Fnombre, direccion, RefDireccion, celular, TelFijo, RefCanasta;
            @SuppressLint("WrongThread") String Cedula = tvUsuarioCedula.getText().toString();
            @SuppressLint("WrongThread") String Empresa = tvUsuarioEmpresa.getText().toString();
            @SuppressLint("WrongThread") String Titulo = tvUsuarioTit.getText().toString();
            @SuppressLint("WrongThread") String Facnombre = tvUsuarioNombreFact.getText().toString();
            @SuppressLint("WrongThread") String Direccion = tvUsuarioDir.getText().toString();
            @SuppressLint("WrongThread") String RDireccion = tvUsuarioRefDir.getText().toString();
            @SuppressLint("WrongThread") String Celular = tvUsuarioCell.getText().toString();
            @SuppressLint("WrongThread") String TFijo = tvUsuarioPhone.getText().toString();
            @SuppressLint("WrongThread") String RCanasta = tvUsuarioRefC.getText().toString();


            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("Cedula", Cedula));
                params.add(new BasicNameValuePair("Empresa", Empresa));
                params.add(new BasicNameValuePair("Titulo", Titulo));
                params.add(new BasicNameValuePair("Facnombre", Facnombre));
                params.add(new BasicNameValuePair("Direccion", Direccion));
                params.add(new BasicNameValuePair("RDireccion", RDireccion));
                params.add(new BasicNameValuePair("Celular", Celular));
                params.add(new BasicNameValuePair("TFijo", TFijo));
                params.add(new BasicNameValuePair("RCanasta", RCanasta));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    Intent i = new Intent(EditarPerfil.this, Principal.class);
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(EditarPerfil.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
