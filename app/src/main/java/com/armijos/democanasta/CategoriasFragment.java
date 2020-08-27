package com.armijos.democanasta;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**

 */
public class CategoriasFragment extends Fragment {

    public Bundle args;

    //url
    public String url_carnes = "http://canastaazul.atwebpages.com/getCarnes.php";
    public String url_condimentos = "http://canastaazul.atwebpages.com/getCondimentos.php";



    public CategoriasFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = new Bundle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categorias, container, false);
        String[] categorias={"Frutas", "Carnes", "Condimentos", "Granos", "Hortalizas y Verduras",
                             "Lacteos", "Licores", "Plantas Aromaticas y medicinales", "Otros"};

        ListView listView = v.findViewById(R.id.categoria_list);
        ArrayAdapter<String> listviewAdapter = new ArrayAdapter<String>(
          getActivity(),
                android.R.layout.simple_list_item_1,
                categorias
        );

        listView.setAdapter(listviewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Fragment fragment = null;
                switch (i){
                    case 1:
                        args.putString("url",url_carnes );
                        fragment = new ProductosFragment();
                        fragment.setArguments(args);
                        break;
                    case 2:
                        args.putString("url",url_condimentos );
                        fragment = new ProductosFragment();
                        fragment.setArguments(args);
                        break;

                }
                 loadFragment(fragment);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private boolean loadFragment(Fragment fragment){

        if (fragment != null) {

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout,fragment)
                    .commit();
            return  true;
        }

        return  false;

    }




}


