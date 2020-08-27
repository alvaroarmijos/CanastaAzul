package com.armijos.democanasta;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditarPerfilFragment extends Fragment {

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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getArguments().getString("username");
        nid = getArguments().getString("nid");
        empresa_user = getArguments().getString("empresa_user");
        Tit = getArguments().getString("Tit");
        name_fact = getArguments().getString("name_fact");
        direccion_fact = getArguments().getString("direccion_fact");
        ref_dir_fact = getArguments().getString("ref_dir_fact");
        cellphone_fact = getArguments().getString("cellphone_fact");
        phone_fact = getArguments().getString("phone_fact");
        reference_canasta = getArguments().getString("reference_canasta");


        View v=inflater.inflate(R.layout.fragment_editarperfil, null);


        //informacion de facturacion
        tvUsuarioCedula = (EditText) v.findViewById(R.id.textViewCed);
        tvUsuarioCedula.setText(nid);
        tvUsuarioEmpresa = (EditText) v.findViewById(R.id.textViewEmpresa);
        tvUsuarioEmpresa.setText(empresa_user);
        tvUsuarioTit = (EditText) v.findViewById(R.id.textViewTit);
        tvUsuarioTit.setText(Tit);
        tvUsuarioNombreFact = (EditText) v.findViewById(R.id.textViewNameFact);
        tvUsuarioNombreFact.setText(name_fact);
        tvUsuarioDir = (EditText) v.findViewById(R.id.textViewDir);
        tvUsuarioDir.setText(direccion_fact);
        tvUsuarioRefDir = (EditText) v.findViewById(R.id.textViewRef);
        tvUsuarioRefDir.setText(ref_dir_fact);
        tvUsuarioCell = (EditText) v.findViewById(R.id.textViewCell);
        tvUsuarioCell.setText(cellphone_fact);
        tvUsuarioRefC = (EditText) v.findViewById(R.id.textViewRefC);
        tvUsuarioRefC.setText(reference_canasta);
        tvUsuarioPhone = (EditText) v.findViewById(R.id.textViewPhone);
        tvUsuarioPhone.setText(phone_fact);

        //Boton
        GuardarCambios=v.findViewById(R.id.ButtonGuardar);
        GuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModificarUser().execute();
            }
        });


        return v;
    }

    class ModificarUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
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
                    Intent i = new Intent(getActivity(), Principal.class);
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
                Toast.makeText(getContext(), file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

}
