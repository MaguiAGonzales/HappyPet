package com.happypet.movil.happypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adaptadores.adaptadorMascotas;
import clases.Mascota;
import parseadores.JsonMascotaParser;

public class MismascotasActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progress;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismascotas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);

        lista = (ListView) findViewById(R.id.lvMascotas);
        cargar("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAgregarMascota);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), MascotaAgregarActivity.class );
                startActivityForResult(intent, 0);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        swipeContainer.setOnRefreshListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==0){
            cargar("");
        }
    }

    private void cargar (String filtro){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=listar";
                filtro = filtro.equals("")?"":"?nombre=" + filtro;
                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_mascota.php" +filtroInicio + filtro;
                System.out.println("direccion ------      " + url);

                new JsonTask().execute(new URL(url) );
            } else {
                Toast.makeText(this, "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        cargar("");
        swipeContainer.setRefreshing(false);
    }

    public class JsonTask extends AsyncTask<URL, Void, List<Mascota>> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MismascotasActivity.this, "", "Cargando la lista de Mascotas...");
        }

        @Override
        protected List<Mascota> doInBackground(URL... urls) {
            List<Mascota> mascotas = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();
//                System.out.println("codigo del estado ---->>  " + statusCode);

                if(statusCode!=200) {
                    mascotas = new ArrayList<>();
                    mascotas.add(new Mascota(0,"",null,null,null, null,null,null,null,null,null,null,1));
                } else {
//                    System.out.println("a leer el msje json");
                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonMascotaParser parser = new JsonMascotaParser();

//                    System.out.println("ENTRADA ---- " + in.toString());

                    mascotas = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            System.out.println(mascotas);
            return mascotas;
        }

        @Override
        protected void onPostExecute(List<Mascota> mascotas) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(mascotas!=null) {
                adaptador = new adaptadorMascotas(getBaseContext(), mascotas);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json en las Mascotas",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }
}
