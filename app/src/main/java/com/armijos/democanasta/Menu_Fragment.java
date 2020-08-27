package com.armijos.democanasta;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Menu_Fragment extends Fragment {

    GridLayout mainGrid;

    public Bundle args;

    //url
    public String url_carnes = "http://canastaazul.atwebpages.com/getCarnes.php";
    public String url_condimentos = "http://canastaazul.atwebpages.com/getCondimentos.php";


    public Menu_Fragment() {
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
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_menu,container, false);
        mainGrid = (GridLayout) v.findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);

        return v;


    }

    private boolean loadFragment(Fragment fragment){

        if (fragment != null) {

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout,fragment)
                    .addToBackStack(null)
                    .commit();
            return  true;
        }

        return  false;

    }


    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = null;

                    ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        // Si hay conexión a Internet en este momento

                        switch (finalI){
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


                    } else {
                        // No hay conexión a Internet en este momento
                        fragment=new NoConectionFragment();
                    }


                    loadFragment(fragment);



                }
            });
        }
    }

}
