package com.armijos.democanasta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Comprar extends Fragment {

    String total;
    private TextView tvTotal;
    Context context = getContext();
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
    Button btnEditPerfil;
    Button btnFinalizarCompra;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Calculamos el total de la compra

        SharedPreferences mPrefs = getActivity().getSharedPreferences("carritolist", getActivity().MODE_PRIVATE);
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
        mPrefs = getContext().getSharedPreferences("fact", getContext().MODE_PRIVATE);
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
        View v = inflater.inflate(R.layout.fragment_comprar, null);
        tvTotal = (TextView) v.findViewById(R.id.textView_Total);
        tvTotal.setText(total + " $");

        //Datos de facturacion
        tvUsuarioCedula = (TextView) v.findViewById(R.id.textViewCed);
        tvUsuarioCedula.setText(nid);
        tvUsuarioEmpresa = (TextView) v.findViewById(R.id.textViewEmpresa);
        tvUsuarioEmpresa.setText(empresa_user);
        tvUsuarioTit = (TextView) v.findViewById(R.id.textViewTit);
        tvUsuarioTit.setText(Tit);
        tvUsuarioNombreFact = (TextView) v.findViewById(R.id.textViewNameFact);
        tvUsuarioNombreFact.setText(name_fact);
        tvUsuarioDir = (TextView) v.findViewById(R.id.textViewDir);
        tvUsuarioDir.setText(direccion_fact);
        tvUsuarioRefDir = (TextView) v.findViewById(R.id.textViewRef);
        tvUsuarioRefDir.setText(ref_dir_fact);
        tvUsuarioCell = (TextView) v.findViewById(R.id.textViewCell);
        tvUsuarioCell.setText(cellphone_fact);
        tvUsuarioRefC = (TextView) v.findViewById(R.id.textViewRefC);
        tvUsuarioRefC.setText(reference_canasta);
        tvUsuarioPhone = (TextView) v.findViewById(R.id.textViewPhone);
        tvUsuarioPhone.setText(phone_fact);

        //Boton
        btnEditPerfil = v.findViewById(R.id.button_EditarDatos);
        btnEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //colocamos informacion de facturacion

                args.putString("username",username );
                args.putString("nid",nid );
                args.putString("empresa_user",empresa_user );
                args.putString("Tit",Tit );
                args.putString("name_fact",name_fact );
                args.putString("direccion_fact",direccion_fact );
                args.putString("ref_dir_fact",ref_dir_fact );
                args.putString("cellphone_fact",cellphone_fact );
                args.putString("phone_fact",phone_fact );
                args.putString("reference_canasta",reference_canasta );

                Fragment fragment = null;
                fragment = new EditarPerfilFragment();
                fragment.setArguments(args);
                loadFragment(fragment);

            }

        });
        btnFinalizarCompra=v.findViewById(R.id.button_finalizarCompra);
        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Compra finalizada", Toast.LENGTH_SHORT).show();
                SharedPreferences mPrefs = getContext().getSharedPreferences("carritolist", getContext().MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.clear().apply();
                Intent i = new Intent(getActivity(), Principal.class);
                startActivity(i);
            }
        });

        return v;

    }

    private boolean loadFragment(Fragment fragment){

        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout,fragment)
                    .commit();
            return  true;
        }

        return  false;

    }
}
