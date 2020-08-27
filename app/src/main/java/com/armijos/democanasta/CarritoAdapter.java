package com.armijos.democanasta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    public List<Carrito> mValues;
    List<Producto> List = new ArrayList<>();
    Context context;
    Boolean comparador;
    //private final OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    CarritoAdapter adapterProductos;
    Context myContext;
    FragmentActivity activity = (FragmentActivity) myContext;
    String total;

    public CarritoAdapter(List<Carrito> items) {
        mValues = items;
        //mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_carrito, parent, false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //REscatamos los datos del elemenento que ocupa la posicion "position
        holder.mItem = mValues.get(position);
        holder.textViewNombreProducto.setText(holder.mItem.getNombre());
        holder.textViewDescripcionProducto.setText(holder.mItem.getDescripcion());
        holder.textViewPrcioRestaurante.setText(holder.mItem.getPrecio()+"$");
        holder.textViewCantidad.setText(holder.mItem.getCantidad());

        Picasso.get()
                .load(holder.mItem.getUrlPhoto())
                .into(holder.imageViewPhotoProducto);

        //Botones
        holder.setOnClickListeners();


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public  View mView;
        public  TextView textViewNombreProducto;
        public  TextView textViewDescripcionProducto;
        public  ImageView imageViewPhotoProducto;
        public  TextView textViewPrcioRestaurante;
        public TextView textViewCantidad;
        public Carrito mItem;
        public TextView textViewTotal;

        //botones
        public  ImageView imageViewAdd;
        public  ImageView imageViewSubstract;
        public ImageView ButtonCarrito;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewNombreProducto = (TextView) view.findViewById(R.id.textViewNombreProducto);
            textViewDescripcionProducto =(TextView) view.findViewById(R.id.textViewDescripcionProducto);
            imageViewPhotoProducto =(ImageView) view.findViewById(R.id.imageViewProducto);
            textViewPrcioRestaurante= (TextView) view.findViewById(R.id.textViewPrecio);
            textViewCantidad=(TextView) view.findViewById(R.id.textViewCantidad);
            textViewTotal=(TextView) view.findViewById(R.id.total);

            //botones
            imageViewAdd = (ImageView) view.findViewById(R.id.imageViewAdd);
            imageViewSubstract = (ImageView) view.findViewById(R.id.imageViewSubstract);
            ButtonCarrito = (ImageView) view.findViewById(R.id.buttonDeleteCarrito);
        }

        void setOnClickListeners() {
            imageViewAdd.setOnClickListener(this);
            imageViewSubstract.setOnClickListener(this);
            ButtonCarrito.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewNombreProducto.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            String cantidad, cantidadfinal;
            int cantidadnueva;
            switch (view.getId()) {
                case R.id.imageViewAdd:
                    cantidadnueva = Integer.parseInt((String) textViewCantidad.getText());
                    cantidadnueva=cantidadnueva+1;
                    cantidad=Integer.toString(cantidadnueva);
                    textViewCantidad.setText(cantidad);

                    SharedPreferences mPrefs = context.getSharedPreferences("carritolist", context.MODE_PRIVATE);
                    String json = mPrefs.getString("lista", "");
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Producto>>(){}.getType();
                    List = gson.fromJson(json, type );
                    comparador = true;
                    String nombre = textViewNombreProducto.getText().toString();
                    for (Producto List: List) {
                        if (nombre.equals(List.getNombre())) {
                            List.setCantidad(cantidad);
                        }
                    }

                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    json = gson.toJson(List);
                    prefsEditor.putString("lista", json);
                    prefsEditor.commit();

                    //cargamos el precio total
                    Fragment fragment = new TotalFragment();
                    FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framelayout_totalfinal, fragment);
                    transaction.commit();
                    break;
                case R.id.imageViewSubstract:
                    cantidadnueva = Integer.parseInt((String) textViewCantidad.getText());
                    if (cantidadnueva == 1) {
                        break;
                    } else {
                        cantidadnueva = cantidadnueva - 1;
                        cantidad = Integer.toString(cantidadnueva);
                        textViewCantidad.setText(cantidad);

                         mPrefs = context.getSharedPreferences("carritolist", context.MODE_PRIVATE);
                         json = mPrefs.getString("lista", "");
                         gson = new Gson();
                         type = new TypeToken<ArrayList<Producto>>(){}.getType();
                        List = gson.fromJson(json, type );
                        comparador = true;
                        nombre = textViewNombreProducto.getText().toString();
                        for (Producto List: List) {
                            if (nombre.equals(List.getNombre())) {
                                List.setCantidad(cantidad);
                            }
                        }

                         prefsEditor = mPrefs.edit();
                        json = gson.toJson(List);
                        prefsEditor.putString("lista", json);
                        prefsEditor.commit();

                    }

                    //cargamos el precio total
                     fragment = new TotalFragment();
                     transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framelayout_totalfinal, fragment);
                    transaction.commit();
                    break;
                case R.id.buttonDeleteCarrito:

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Eliminar del Carrito");
                    dialog.setMessage("¿Seguro deseas eliminar este produto del carrito?");
                    dialog.setNegativeButton("Cancelar", null);
                    dialog.setPositiveButton("si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences mPrefs = context.getSharedPreferences("carritolist", context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Producto>>(){}.getType();
                            mValues.remove(getAdapterPosition());
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            String json = gson.toJson(mValues);
                            prefsEditor.putString("lista", json);
                            prefsEditor.commit();
                            //Actualizamos la vista
                            notifyItemRemoved(getAdapterPosition());

                            //cargamos el precio total
                            Fragment fragment = new TotalFragment();
                            FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.framelayout_totalfinal, fragment);
                            transaction.commit();
                        }
                    });
                    dialog.show();


                    break;
            }
        }

    }
}