package com.armijos.democanasta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
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

public class Registrarse extends AppCompatActivity {

    private Button registrarse;

    //informacion de usuario
    private EditText user, pass,nom,corr;

    //informacion de facturacion
    private EditText ced, empr, tit, Fnom, dir, RefDir, cel, Tel, RefCa;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //direccion servicio web
    private static final String REGISTER_URL = "http://canastaazul.atwebpages.com/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrarse");
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Boton
        registrarse = findViewById(R.id.ButtonREgistrare);

        //informacion de usuario
        user=(EditText)findViewById(R.id.editTextUsuario);
        nom=(EditText)findViewById(R.id.editTextNombre);
        pass=(EditText)findViewById(R.id.editTextClave);
        corr=(EditText)findViewById(R.id.editTextCorreo);

        //informacion de facturacion
        ced=(EditText)findViewById(R.id.editTextCedula);
        empr=(EditText)findViewById(R.id.editTextEmpresa);
        tit=(EditText)findViewById(R.id.editTextTitulo);
        Fnom=(EditText)findViewById(R.id.editTextFNombre);
        dir=(EditText)findViewById(R.id.editTextDireccion);
        RefDir=(EditText)findViewById(R.id.editTextReferenciaDireccion);
        cel=(EditText)findViewById(R.id.editTextCelular);
        Tel=(EditText)findViewById(R.id.editTextTelFijo);
        RefCa=(EditText)findViewById(R.id.editTextComentario);


        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Registrarse.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Si hay conexión a Internet en este momento
                    new CreateUser().execute();
                } else {
                    // No hay conexión a Internet en este momento
                    Toast.makeText(Registrarse.this,"Sin conexión a Internet",Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registrarse.this);
            pDialog.setMessage("Creando Usuario...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            //Informacion de usuario
            String username = user.getText().toString();
            String password = pass.getText().toString();
            String Nombre = nom.getText().toString();
            String Correo = corr.getText().toString();

            //informacion de facturacion cedula, empresa, titulo, Fnombre, direccion, RefDireccion, celular, TelFijo, RefCanasta;
            String Cedula = ced.getText().toString();
            String Empresa = empr.getText().toString();
            String Titulo = tit.getText().toString();
            String Facnombre = Fnom.getText().toString();
            String Direccion = dir.getText().toString();
            String RDireccion = RefDir.getText().toString();
            String Celular = cel.getText().toString();
            String TFijo = Tel.getText().toString();
            String RCanasta = RefCa.getText().toString();


            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("Nombre", Nombre));
                params.add(new BasicNameValuePair("Correo", Correo));
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
                    finish();

                    Intent i = new Intent(Registrarse.this, MainActivity.class);
                    finish();
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
                Toast.makeText(Registrarse.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
