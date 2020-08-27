package com.armijos.democanasta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;


public class Principal extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener  {


    public String nombre;


    //Contexto
    Context context;
    public String name;


    BottomNavigationView navView;

    //Variable que nos permite saber cuando nos encontramos en el item principal del menu
    private boolean viewIsAtHome;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal2);

            // Cargamos el usuario de la memoria interna
        context=getApplicationContext();
        SharedPreferences mPrefs = context.getSharedPreferences("fact", context.MODE_PRIVATE);
        nombre = mPrefs.getString("usr", "");


        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        navView.performClick();

        navView.setSelectedItemId(R.id.navigation_productos);






    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

        //vemos cuantos elementos hat en la pila BackStack

        int count = getSupportFragmentManager().getBackStackEntryCount();

        //si no hay ningun elemento comprobamos si estamos en la vista home o no y depende de eso
        //cambiamos el navigationView o cerramos la app
        if (count == 0) {
            if (!viewIsAtHome) { //if the current view is not the News fragment
                BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_productos);

            } else {
                moveTaskToBack(true);  //If view is in News fragment, exit application
            }
        } else {

            //si es direcente de cero cargamos el fragment que tenemos en la vista, pero cargamos el
            //navitionView en la vista Home primero
            //ya que el unico elemento que esta en la pila es el Menu Fragment ye ste siempre esta en Home
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_productos);
            getSupportFragmentManager().popBackStack();

        }


    }



    private boolean loadFragment(Fragment fragment){

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, fragment);
            transaction.commit();
        return  true;
        }

        return  false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
         switch (menuItem.getItemId()){
             case R.id.navigation_perfil:
                 ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                 NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                 if (networkInfo != null && networkInfo.isConnected()) {
                     // Si hay conexión a Internet en este momento
                     fragment = new PerfilFragment();
                 } else {
                     // No hay conexión a Internet en este momento
                     fragment = new NoConectionFragment();
                 }

             viewIsAtHome = false;
             loadFragment(fragment);
             break;
             case  R.id.navigation_productos:
                 fragment = new Menu_Fragment();
                 viewIsAtHome = true;
                 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                 transaction.add(R.id.framelayout, fragment);
                 transaction.commit();
                 break;

             case R.id.navigation_carrito:

                 context=getApplicationContext();
                 SharedPreferences mPrefs = context.getSharedPreferences("carritolist", context.MODE_PRIVATE);
                 String json = mPrefs.getString("lista", "");

                 if (json.isEmpty()){
                     fragment = new CarritoVacioFragment();
                      transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.framelayout, fragment);
                     transaction.commit();
                 }else {

                     //cargamos el total
                     fragment = new CarritoTotalFragment();
                      transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.framelayout, fragment);
                     transaction.commit();

                     fragment = new CarritoFragment();
                     transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.framelayoutcarrito, fragment);
                     transaction.commit();

                     //cargamos el precio total
                     fragment = new TotalFragment();
                     transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.framelayout_totalfinal, fragment);
                     transaction.commit();
                 }
                 viewIsAtHome = false;






                 break;
         }
        return true;

    }


}
