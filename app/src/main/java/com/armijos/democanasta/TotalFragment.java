package com.armijos.democanasta;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TotalFragment extends Fragment {

    //Boton
    Button btnFinalizarCompra;

    private TextView tvTotal;
    Context context = getContext();
    List<Carrito> carritoList = new ArrayList<>();
    float precio, cantidad;
    float sumaparcial = 0;

    String total;

    public TotalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.total, container, false);

        //Calculamos el total de la compra

        SharedPreferences mPrefs = getActivity().getSharedPreferences("carritolist", getActivity().MODE_PRIVATE);
        String json = mPrefs.getString("lista", "");
        Gson gson = new Gson();
        if (json.isEmpty()) {
            total = "No hay productos agregados al carrito";

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

        tvTotal = (TextView) view.findViewById(R.id.total);
        tvTotal.setText(total + " $");

        //Boton Finalizar Compra
        btnFinalizarCompra=view.findViewById(R.id.ButtonFinalizarCompra);
        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FinalizarActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
