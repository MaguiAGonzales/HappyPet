package com.happypet.movil.happypet;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import adaptadores.adaptadorEvento;
import adaptadores.adaptadorMascotas;
import clases.Evento;
import clases.Mascota;
import parseadores.JsonEventoParser;
import parseadores.JsonMascotaParser;

public class EventosActivity extends AppCompatActivity {

    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progress;

    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);

        lista = (ListView) findViewById(R.id.lvEventos);
        cargar("");

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargar("");
                swipeContainer.setRefreshing(false);
            }
        });
    }


    private void cargar(String filtro) {
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

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_eventos.php" +filtroInicio + filtro;
                System.out.println("direccion ------      " + url);

                new JsonTask().execute(new URL(url) );
            } else {
                Toast.makeText(this, "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }




    public class JsonTask extends AsyncTask<URL, Void, List<Evento>> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EventosActivity.this, "", "Cargando la lista de Eventos...");
        }

        @Override
        protected List<Evento> doInBackground(URL... urls) {
            List<Evento> eventos = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();
//                System.out.println("codigo del estado ---->>  " + statusCode);

                if(statusCode!=200) {
                    eventos = new ArrayList<>();
                    eventos.add(new Evento(0,"",null,null,null,null,null));
                } else {
//                    System.out.println("a leer el msje json");
                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonEventoParser parser = new JsonEventoParser();

//                    System.out.println("ENTRADA ---- " + in.toString());

                    eventos = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            System.out.println(eventos);
            return eventos;
        }


        protected void onPostExecute(List<Evento> eventos) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(eventos!=null) {
                adaptador = new adaptadorEvento(getBaseContext(), eventos);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json en los Eventos",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }
}
