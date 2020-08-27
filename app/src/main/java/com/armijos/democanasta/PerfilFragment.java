package com.armijos.democanasta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class PerfilFragment extends Fragment {
    String usuario ="";
    private TextView tvUsuario;
    private TextView tvNombre;
    private TextView tvCorreo;
    private TextView tvUsuarioPerfil;
    private TextView tvUsuarioCedula;
    private TextView tvUsuarioEmpresa;
    private TextView tvUsuarioTit;
    private TextView tvUsuarioNombreFact;
    private TextView tvUsuarioDir;
    private TextView tvUsuarioRefDir;
    private TextView tvUsuarioCell;
    private TextView tvUsuarioPhone;
    private TextView tvUsuarioRefC;

    //Boton
    TextView btnEditPerfil;
    TextView btnSalir;

    //Contexto
    Context context;



    public Bundle args = new Bundle();

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    public JSONObject c;

    ArrayList<HashMap<String, String>> empresaList;

    // url to get all products list
    private static String url_all_empresas = "http://canastaazul.atwebpages.com/getUsuario.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "usuario";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NOMBRE = "name";
    private static final String TAG_MAIL = "mail";
    private static final String TAG_NID = "nid";
    private static final String TAG_EMPRESA = "empresa_user";
    private static final String TAG_TIT = "Tit";
    private static final String TAG_NAMEFACT = "name_fact";
    private static final String TAG_DIR = "direccion_fact";
    private static final String TAG_REF = "ref_dir_fact";
    private static final String TAG_CELL = "cellphone_fact";
    private static final String TAG_PHONE = "phone_fact";
    private static final String TAG_REFC = "reference_canasta";

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

    // products JSONArray
    JSONArray products = null;

    public ProgressDialog pDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cargamos el usuario de la memoria interna
        SharedPreferences mPrefs = getContext().getSharedPreferences("fact", getContext().MODE_PRIVATE);
        String user = mPrefs.getString("usr", "");
        username=user;


        //cargamos los datos
        new LoadAllProducts().execute();

        View v=inflater.inflate(R.layout.fragment_perfil, null);


        //Boton Editar
        btnEditPerfil=v.findViewById(R.id.EditarPerfil);
        btnSalir=v.findViewById(R.id.Salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context=getContext();
                SharedPreferences mPrefs = context.getSharedPreferences("fact", context.MODE_PRIVATE);
                mPrefs.edit().clear().apply();

                Intent i = new Intent(context, MainActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        });
        btnEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //colocamos informacion de facturacion
                Intent intent = new Intent(getActivity(), EditarPerfil.class);
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

            private boolean loadFragment(Fragment fragment) {
                if (fragment != null) {

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.framelayout,fragment)
                            .commit();
                    return  true;
                }

                return  false;
            }
        });

        return v;
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }



        /**
         * obteniendo el usuario
         * */
        protected String doInBackground (String... params) {

            try {

                // Building Parameters
                List params1 = new ArrayList();
                params1.add(new BasicNameValuePair("username", username));
                // getting JSON string from URL

                Log.d("request!", "starting");
                JSONObject json = jParser.makeHttpRequest(url_all_empresas, "POST", params1);

                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());


                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        c = products.getJSONObject(i);

                        // Storing each json item in variable
                        name = c.getString(TAG_NOMBRE);
                        mail = c.getString(TAG_MAIL);
                        nid=c.getString(TAG_NID);
                        empresa_user=c.getString(TAG_EMPRESA);
                        Tit=c.getString(TAG_TIT);
                        name_fact=c.getString(TAG_NAMEFACT);
                        direccion_fact=c.getString(TAG_DIR);
                        ref_dir_fact=c.getString(TAG_REF);
                        cellphone_fact=c.getString(TAG_CELL);
                        phone_fact=c.getString(TAG_PHONE);
                        reference_canasta=c.getString(TAG_REFC);

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            //informacion de usuario
            tvNombre = (TextView) getView().findViewById(R.id.textViewNombre1);
            tvNombre.setText(name);
            tvUsuarioPerfil = (TextView) getView().findViewById(R.id.textViewUsuarioPerfil);
            tvUsuarioPerfil.setText(username);
            tvCorreo = (TextView) getView().findViewById(R.id.textViewCorreo1);
            tvCorreo.setText(mail);

            //informacion de facturacion
            tvUsuarioCedula = (TextView) getView().findViewById(R.id.textViewCed);
            tvUsuarioCedula.setText(nid);
            tvUsuarioEmpresa = (TextView) getView().findViewById(R.id.textViewEmpresa);
            tvUsuarioEmpresa.setText(empresa_user);
            tvUsuarioTit = (TextView) getView().findViewById(R.id.textViewTit);
            tvUsuarioTit.setText(Tit);
            tvUsuarioNombreFact = (TextView) getView().findViewById(R.id.textViewNameFact);
            tvUsuarioNombreFact.setText(name_fact);
            tvUsuarioDir = (TextView) getView().findViewById(R.id.textViewDir);
            tvUsuarioDir.setText(direccion_fact);
            tvUsuarioRefDir = (TextView) getView().findViewById(R.id.textViewRef);
            tvUsuarioRefDir.setText(ref_dir_fact);
            tvUsuarioCell = (TextView) getView().findViewById(R.id.textViewCell);
            tvUsuarioCell.setText(cellphone_fact);
            tvUsuarioRefC = (TextView) getView().findViewById(R.id.textViewRefC);
            tvUsuarioRefC.setText(reference_canasta);
            tvUsuarioPhone = (TextView) getView().findViewById(R.id.textViewPhone);
            tvUsuarioPhone.setText(phone_fact);

            //Guardamos los datos en la memoria interna
            SharedPreferences mPrefs = getContext().getSharedPreferences("fact", getContext().MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("usr", username);
            prefsEditor.putString("ced", nid);
            prefsEditor.putString("emp", empresa_user);
            prefsEditor.putString("tit", Tit);
            prefsEditor.putString("namef", name_fact);
            prefsEditor.putString("dir", direccion_fact);
            prefsEditor.putString("ref", ref_dir_fact);
            prefsEditor.putString("cell", cellphone_fact);
            prefsEditor.putString("canasta", reference_canasta);
            prefsEditor.putString("phone", phone_fact);
            prefsEditor.commit();

                }

        }

        }




