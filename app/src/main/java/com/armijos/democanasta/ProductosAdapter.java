package com.armijos.democanasta;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {


    List<Producto> List = new ArrayList<>();
    Context context;
    Boolean comparador;
    View view;


    private final List<Producto> mValues;
    private final List<Producto> copymValues = new ArrayList<>();

    //private final OnListFragmentInteractionListener mListener;


    public ProductosAdapter(List<Producto> items) {
        mValues = items;
        copymValues.addAll(mValues);

        //mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_productos, parent, false);
        context = parent.getContext();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //REscatamos los datos del elemenento que ocupa la posicion "position
        holder.mItem = mValues.get(position);
        holder.textViewNombreProducto.setText(holder.mItem.getNombre());
        holder.textViewDescripcionProducto.setText(holder.mItem.getDescripcion());
        holder.textViewPrcioRestaurante.setText(holder.mItem.getPrecio());
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;
        public TextView textViewNombreProducto;
        public TextView textViewDescripcionProducto;
        public ImageView imageViewPhotoProducto;
        public TextView textViewPrcioRestaurante;
        public TextView textViewCantidad;
        public Producto mItem;

        //botones
        public ImageView imageViewAdd;
        public ImageView imageViewSubstract;
        public ImageView ButtonCarrito;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewNombreProducto = (TextView) view.findViewById(R.id.textViewNombreProducto);
            textViewDescripcionProducto = (TextView) view.findViewById(R.id.textViewDescripcionProducto);
            imageViewPhotoProducto = (ImageView) view.findViewById(R.id.imageViewProducto);
            textViewPrcioRestaurante = (TextView) view.findViewById(R.id.textViewPrecio);
            textViewCantidad = (TextView) view.findViewById(R.id.textView_Cantidad);

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
            String cantidad;
            int cantidadnueva;
            switch (view.getId()) {
                case R.id.imageViewAdd:
                    cantidadnueva = Integer.parseInt((String) textViewCantidad.getText());
                    cantidadnueva = cantidadnueva + 1;
                    cantidad = Integer.toString(cantidadnueva);
                    textViewCantidad.setText(cantidad);
                    break;
                case R.id.imageViewSubstract:
                    cantidadnueva = Integer.parseInt((String) textViewCantidad.getText());
                    if (cantidadnueva == 1) {
                        break;
                    } else {
                        cantidadnueva = cantidadnueva - 1;
                        cantidad = Integer.toString(cantidadnueva);
                        textViewCantidad.setText(cantidad);

                    }
                    break;
                case R.id.buttonDeleteCarrito:
                    List<Producto> carritoList = new ArrayList<>();
                    //almacenamiento de los datos en la memoria interna
                    SharedPreferences mPrefs = context.getSharedPreferences("carritolist", context.MODE_PRIVATE);
                    String json = mPrefs.getString("lista", "");
                    Gson gson = new Gson();
                    //Si esta vacion agregamos directamente los productos caso contrario
                    //se hace una comparacion, si el producto ya existe no se lo agrega al carrito
                    if (json.isEmpty()) {
                        carritoList.add(new Producto(textViewNombreProducto.getText().toString(),mItem.getUrlPhoto() , textViewDescripcionProducto.getText().toString()
                                , textViewPrcioRestaurante.getText().toString(), textViewCantidad.getText().toString()));
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        json = gson.toJson(carritoList);
                        prefsEditor.putString("lista", json);
                        prefsEditor.commit();
                        Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show();
                        break;

                    } else {
                        Type type = new TypeToken<ArrayList<Producto>>() {
                        }.getType();
                        List = gson.fromJson(json, type);
                        comparador = true;
                        String nombre = textViewNombreProducto.getText().toString();
                        for (Producto List : List) {
                            String nombre2 = List.getNombre();
                            carritoList.add(new Producto(List.getNombre(), List.getUrlPhoto(), List.getDescripcion(), List.getPrecio(), List.getCantidad()));
                            if (nombre.equals(nombre2)) {
                                comparador = false;
                            }
                        }
                        if (comparador) {
                            carritoList.add(new Producto(textViewNombreProducto.getText().toString(), mItem.getUrlPhoto(), textViewDescripcionProducto.getText().toString()
                                    , textViewPrcioRestaurante.getText().toString(), textViewCantidad.getText().toString()));
                            Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show();
                        } else {
                            comparador = true;
                            Toast.makeText(context, "El producto ya esta en el carrito", Toast.LENGTH_SHORT).show();
                        }
                        //Guardamos en la memoria interna
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        json = gson.toJson(carritoList);
                        prefsEditor.putString("lista", json);
                        prefsEditor.commit();
                    }

                    break;
            }
        }
    }

    public void filtrar(String texto) {
        // Elimina todos los datos del ArrayList que se cargan en los
        // elementos del adaptador
        mValues.clear();

        if (texto.length() == 0) {
            mValues.addAll(copymValues);
        } else {

            // Recorre todos los elementos que contiene el ArrayList copiado
            // y dependiendo de si estos contienen el texto ingresado por el
            // usuario los agrega de nuevo al ArrayList que se carga en los
            // elementos del adaptador.
            for (Producto producto : copymValues) {

                if (producto.getNombre().contains(texto)) {
                    mValues.add(producto);
                }
            }
        }
        notifyDataSetChanged();
    }
}
