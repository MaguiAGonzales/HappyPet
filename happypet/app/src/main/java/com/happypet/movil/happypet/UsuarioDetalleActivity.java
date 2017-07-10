package com.happypet.movil.happypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import adaptadores.adaptadorMascotas;
import clases.DialogoAlerta;
import clases.Mascota;
import parseadores.JsonMascotaParser;

public class UsuarioDetalleActivity extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID, MASCOTA_FOTO, MASCOTA_NOMBRE, MASCOTA_TIPO, MASCOTA_SEXO, MASCOTA_PARTICULARIDAD, MASCOTA_SALUD, MASCOTA_EDAD;

    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progress;

    TextView tvNombeUsuario;
    EditText tbDni, tbFecNac, tbTelFijo, tbTelCelular, tbDireccion, tbReferencia;
    EditText tbMini, tbMfin, tbTini, tbTfin;
    CheckBox cbLunes, cbMartes, cbMiercoles, cbJueves,cbViernes;
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        MASCOTA_ID = getIntent().getStringExtra("id");

        MASCOTA_FOTO = getIntent().getStringExtra("foto");
        MASCOTA_NOMBRE = getIntent().getStringExtra("nombre");
        MASCOTA_TIPO = getIntent().getStringExtra("tipo");
        MASCOTA_SEXO = getIntent().getStringExtra("sexo");
        MASCOTA_PARTICULARIDAD = getIntent().getStringExtra("particularidad");
        MASCOTA_SALUD = getIntent().getStringExtra("salud");
        MASCOTA_EDAD = getIntent().getStringExtra("edad");

        tvNombeUsuario = (TextView) findViewById(R.id.tv_ud_nombreUsuario);
        tbDni = (EditText) findViewById(R.id.et_ud_dni);
        tbFecNac = (EditText) findViewById(R.id.et_ud_fechaNacimiento);
        tbTelFijo = (EditText) findViewById(R.id.et_ud_telefonoFijo);
        tbTelCelular = (EditText) findViewById(R.id.et_ud_telefonoCelular);
        tbDireccion = (EditText) findViewById(R.id.et_ud_Direccion);
        tbReferencia = (EditText) findViewById(R.id.et_ud_Referencia);

        cbLunes = (CheckBox) findViewById(R.id.chb_ud_lunes);
        cbMartes = (CheckBox) findViewById(R.id.chb_ud_martes);
        cbMiercoles = (CheckBox) findViewById(R.id.chb_ud_miercoles);
        cbJueves = (CheckBox) findViewById(R.id.chb_ud_jueves);
        cbViernes= (CheckBox) findViewById(R.id.chb_ud_viernes);

        tbMini = (EditText) findViewById(R.id.et_ud_ManianaInicio);
        tbMfin = (EditText) findViewById(R.id.et_ud_ManianaFin);
        tbTini = (EditText) findViewById(R.id.et_ud_TardeInicio);
        tbTfin = (EditText) findViewById(R.id.et_ud_TardeFin);


        btnSiguiente = (Button) findViewById(R.id.btn_ud_continuar);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guarddarFase1();
            }
        });

        cargar(USUARIO_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ============== CARGAR INFO USUARIO ====================

    private void cargar (String id){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=listar&id=" + id;
                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_usuario.php" +filtroInicio ;
                System.out.println("direccion ------      " + url);

                new JsonTask().execute(new URL(url) );
            } else {
                Toast.makeText(this, "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class JsonTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(UsuarioDetalleActivity.this, "", "Cargando mi información...");
        }

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject usuario = null;
            String linea = "";
            StringBuffer result = new StringBuffer();

            try {
                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
//                con.setConnectTimeout(15000);
//                con.setReadTimeout(10000);

                con.connect();

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();
//                System.out.println("ESTADO:   " + statusCode);

                if(statusCode==200) {
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((linea = reader.readLine()) != null) {
                        result.append(linea);
                    }
//                    System.out.println("USUARIO --->  " + result.toString());
                    usuario = new JSONObject(result.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return usuario;
        }

        @Override
        protected void onPostExecute(JSONObject usuario) {
            if(usuario!=null) {
                try {
//                    Toast.makeText(getBaseContext(),"NOMBRE : " + usuario.getString("nombre").toString(),
//                            Toast.LENGTH_SHORT).show();
                    tvNombeUsuario.setText(usuario.getString("nombre") + " " + usuario.getString("apellidos") );
                    tbDni.setText(usuario.getString("dni"));
                    tbFecNac.setText(usuario.getString("fecha_nacimiento"));
                    tbTelCelular.setText(usuario.getString("telefono"));
                    tbDireccion.setText(usuario.getString("direccion"));
                    tbReferencia.setText(usuario.getString("referencia"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getBaseContext(),"Ocurrió un error de Parsing Json en Usuario",
                        Toast.LENGTH_SHORT).show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }

    // =============== GUARDAR DATOS FASE 1 ====================

    private void guarddarFase1 (){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=f1&id=" + USUARIO_ID + "&mid=" + MASCOTA_ID;
                filtroInicio += "&lu=" + (cbLunes.isChecked()?1:0);
                filtroInicio += "&ma=" + (cbMartes.isChecked()?1:0);
                filtroInicio += "&mi=" + (cbMiercoles.isChecked()?1:0);
                filtroInicio += "&ju=" + (cbJueves.isChecked()?1:0);
                filtroInicio += "&vi=" + (cbViernes.isChecked()?1:0);
                filtroInicio += "&mini=" + tbMini.getText().toString();
                filtroInicio += "&mfin=" + tbMfin.getText().toString();
                filtroInicio += "&tini=" + tbTini.getText().toString();
                filtroInicio += "&tfin=" + tbTini.getText().toString();

                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_adopcion.php" +filtroInicio ;
                System.out.println("direccion ------      " + url);

                new Fase1Task().execute(new URL(url) );
            } else {
                Toast.makeText(this, "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class Fase1Task extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(UsuarioDetalleActivity.this, "", "Guardando FASE 1...");
        }

        @Override
        protected String doInBackground(URL... urls) {
            String linea = "";
            StringBuffer result = new StringBuffer();

            try {
                con = (HttpURLConnection)urls[0].openConnection();
                con.connect();
                int statusCode = con.getResponseCode();
                if(statusCode==200) {
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((linea = reader.readLine()) != null) {
                        result.append(linea);
                    }
                    System.out.println("DATA --->  " + result.toString());
//                    usuario = new JSONObject(result.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String data) {
            if(data!=null) {
                try {
                    JSONObject obj = new JSONObject(data);
                    Toast.makeText(getBaseContext(), obj.getString("msg").toString(),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getBaseContext(), ProcesoAdopcion.class);
                    intent.putExtra("idUsuario", USUARIO_ID);
                    intent.putExtra("idMascota", MASCOTA_ID);
                    intent.putExtra("idAdopcion", obj.getString("id").toString());

                    intent.putExtra("foto", MASCOTA_FOTO);
                    intent.putExtra("nombre", MASCOTA_NOMBRE);
                    intent.putExtra("tipo", MASCOTA_TIPO);
                    intent.putExtra("sexo", MASCOTA_SEXO);
                    intent.putExtra("particularidad", MASCOTA_PARTICULARIDAD);
                    intent.putExtra("salud", MASCOTA_SALUD);
                    intent.putExtra("edad", MASCOTA_EDAD );

//                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("estado", "F1" );

                    startActivity(intent);
                    finish();
//                    startActivityForResult(intent, 2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getBaseContext(),"Ocurrió un error de parsing Json en FASE 1",
                        Toast.LENGTH_SHORT).show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }
}
