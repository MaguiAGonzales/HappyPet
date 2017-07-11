package com.happypet.movil.happypet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import fragmentos.AdopcionesFragment;
import fragmentos.AlertasFragment;
import fragmentos.DenunciasFragment;
import fragmentos.MiCuentaFragment;
import fragmentos.MisMascotasFragment;
import fragmentos.TerminosCuentaFragment;

public class MenuLateralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdopcionesFragment.OnFragmentInteractionListener,
        MisMascotasFragment.OnFragmentInteractionListener,
        MiCuentaFragment.OnFragmentInteractionListener,
        TerminosCuentaFragment.OnFragmentInteractionListener,
        DenunciasFragment.OnFragmentInteractionListener,
        AlertasFragment.OnFragmentInteractionListener{

    TextView tbNombreUsuario, tbCorreoUsuario;
    ImageView imgfotoUsuario;

    String ID, FOTO, USUARIO, CORREO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent myIntent = getIntent();
        ID = myIntent.getStringExtra("id");
        FOTO = myIntent.getStringExtra("foto");
        USUARIO = myIntent.getStringExtra("nombre");
        CORREO = myIntent.getStringExtra("correo");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        tbNombreUsuario = (TextView)header.findViewById(R.id.tvNombreUsuario);
        tbNombreUsuario.setText(USUARIO);

        tbCorreoUsuario = (TextView)header.findViewById(R.id.tvCorreoUsuario);
        tbCorreoUsuario.setText(CORREO);

        imgfotoUsuario = (ImageView) header.findViewById(R.id.ivFotoUsuario);
        byte[] decodedString = Base64.decode(FOTO, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgfotoUsuario.setImageBitmap(decodedByte);


        Fragment fragment = new AdopcionesFragment();
        Bundle args = new Bundle();
        args.putString("id", ID);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorPrincipal, fragment).commit();

        MenuItem item = navigationView.getMenu().getItem(1);
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        Fragment fragment = null;

        if (id == R.id.nav_mascotas) {
            fragment = new MisMascotasFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_adopciones) {
            fragment = new AdopcionesFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_denuncias) {
            fragment = new DenunciasFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_alertas) {
            fragment = new AlertasFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_eventos) {
            Intent intent = new Intent (this, EventosActivity.class );
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_terminos) {
            fragment = new TerminosCuentaFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_micuenta) {
            fragment = new MiCuentaFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_cerrar) {
            Intent intent = new Intent (this, LoginActivity.class );
            startActivity(intent);
            return true;
        }

        if(FragmentTransaction){
            Bundle args = new Bundle();
            args.putString("id", ID);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedorPrincipal, fragment, "FragmentTag")
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    @Override
//    public void onRestart() {
//        super.onRestart();
//        AdopcionesFragment frag = (AdopcionesFragment) getSupportFragmentManager().findFragmentByTag("FragmentTag");
//        frag.cargarAdopcionesDisponibles();
//    }

}
