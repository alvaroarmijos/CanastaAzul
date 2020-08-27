package com.armijos.democanasta;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;


import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductosFragment extends Fragment {

    RecyclerView recyclerView;
    public ProductosAdapter adapterProductos;
    public List<Producto> productoList;

    JSONParser jParser = new JSONParser();
    public JSONObject c;


    // url to get all products list
    private   String url_all_empresas;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_tipo = "tipo";
    private static final String TAG_cantidad = "cantidad";
    private static final String TAG_unidad = "unidad";
    private static final String TAG_precio = "precio_unidad";
    private static final String TAG_PRODUCTS = "productos";
    private static final String TAG_IMAGEN = "imagen";

    public String tipo;
    public String cantidad;
    public String unidad;
    public String precio_unidad;
    public String imagen;

    // products JSONArray
    JSONArray products = null;

    // TODO: Customize parameters
    private int mColumnCount = 1;

    // Progress Dialog
    public ProgressDialog pDialog;

    private  View view;


    //Search View
    SearchView searchView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_productos_list, container, false);
        url_all_empresas = getArguments().getString("url");
        new LoadAllProducts().execute();


        //Busqueda
        searchView=(SearchView)view.findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapterProductos.filtrar(s);



                return false;
            }
        });



        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }




        }
        return view;
    }



    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
                    int j = products.length();
                    productoList = new ArrayList<>();

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {

                        c = products.getJSONObject(i);

                        // Storing each json item in variable
                        tipo = c.getString(TAG_tipo);
                        unidad = c.getString(TAG_unidad);
                        cantidad = c.getString(TAG_cantidad);
                        precio_unidad=c.getString(TAG_precio);
                        imagen=c.get(TAG_IMAGEN).toString();

                        //Lista de elementos

                        productoList.add(new Producto(tipo, imagen , unidad + " "+cantidad ,precio_unidad,"1"));



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

            // updating UI from Background Thread

            //Asociamos el adaptador al RecycleView
            recyclerView = (RecyclerView)view.findViewById(R.id.list_producto) ;
            adapterProductos= new ProductosAdapter(productoList);
            recyclerView.setAdapter(adapterProductos);

        }
    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Producto item);


    }
}
