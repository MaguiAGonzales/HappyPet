package com.happypet.movil.happypet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class TestAdopcion extends AppCompatActivity {

    ProgressDialog progress;
    HttpURLConnection con;

    String USUARIO_ID, ADOPCION_ID, MASCOTA_ID;
    Button btnGuardar;

    EditText etR1, etR2, etR3, etR4, etR6, etR7, etR8, etR9, etR10, etR101, etR12;
    RadioButton rbR2, rbR3, rbR4, rbR5, rbR8;
    CheckBox chbR111, chbR112, chbR113, chbR114, chbR115, chbR116, chbR117, chbR118, chbR119;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_adopcion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        ADOPCION_ID = getIntent().getStringExtra("idAdopcion");
        MASCOTA_ID = getIntent().getStringExtra("idMascota");

        etR1 = (EditText) findViewById(R.id.et_test_p1);
        etR2 = (EditText) findViewById(R.id.et_test_p2);
        etR3 = (EditText) findViewById(R.id.et_test_p3);
        etR4 = (EditText) findViewById(R.id.et_test_p4);
        etR6 = (EditText) findViewById(R.id.et_test_p6);
        etR7 = (EditText) findViewById(R.id.et_test_p7);
        etR8 = (EditText) findViewById(R.id.et_test_p8);
        etR9 = (EditText) findViewById(R.id.et_test_p9);
        etR10 = (EditText) findViewById(R.id.et_test_p10);
        etR101 = (EditText) findViewById(R.id.et_test_p101);
        etR12 = (EditText) findViewById(R.id.et_test_p12);

        rbR2 = (RadioButton) findViewById(R.id.rb_test_p21);
        rbR3 = (RadioButton) findViewById(R.id.rb_test_p31);
        rbR4 = (RadioButton) findViewById(R.id.rb_test_p41);
        rbR5 = (RadioButton) findViewById(R.id.rb_test_p51);
        rbR8 = (RadioButton) findViewById(R.id.rb_test_p81);

        chbR111 = (CheckBox) findViewById(R.id.chb_test_p111);
        chbR112 = (CheckBox) findViewById(R.id.chb_test_p112);
        chbR113 = (CheckBox) findViewById(R.id.chb_test_p113);
        chbR114 = (CheckBox) findViewById(R.id.chb_test_p114);
        chbR115 = (CheckBox) findViewById(R.id.chb_test_p115);
        chbR116 = (CheckBox) findViewById(R.id.chb_test_p116);
        chbR117 = (CheckBox) findViewById(R.id.chb_test_p117);
        chbR118 = (CheckBox) findViewById(R.id.chb_test_p118);
        chbR119 = (CheckBox) findViewById(R.id.chb_test_p119);

        btnGuardar = (Button)findViewById(R.id.btnGuardarTest);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result",result);
//                setResult(Activity.RESULT_OK,returnIntent);
//                finish();
                guardarFase2();
            }
        });
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

    private void guardarFase2 (){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=f2&ida=" + ADOPCION_ID;
                filtroInicio += "&r1=" + URLEncoder.encode(etR1.getText().toString(), "UTF-8");
                filtroInicio += "&r2=" + URLEncoder.encode(etR2.getText().toString(), "UTF-8");
                filtroInicio += "&r21=" + (rbR2.isChecked()?1:0);
                filtroInicio += "&r3=" + URLEncoder.encode(etR3.getText().toString(), "UTF-8");
                filtroInicio += "&r31=" + (rbR3.isChecked()?1:0);
                filtroInicio += "&r4=" + URLEncoder.encode(etR4.getText().toString(), "UTF-8");
                filtroInicio += "&r41=" + (rbR4.isChecked()?1:0);
                filtroInicio += "&r51=" + (rbR5.isChecked()?1:0);
                filtroInicio += "&r6=" + URLEncoder.encode(etR6.getText().toString(), "UTF-8");
                filtroInicio += "&r7=" + URLEncoder.encode(etR7.getText().toString(), "UTF-8");
                filtroInicio += "&r8=" + URLEncoder.encode(etR8.getText().toString(), "UTF-8");
                filtroInicio += "&r81=" + (rbR8.isChecked()?1:0);
                filtroInicio += "&r9=" + URLEncoder.encode(etR9.getText().toString(), "UTF-8");
                filtroInicio += "&r10=" + URLEncoder.encode(etR10.getText().toString(), "UTF-8");
                filtroInicio += "&r101=" + etR101.getText().toString();
                filtroInicio += "&r111=" + (chbR111.isChecked()?1:0);
                filtroInicio += "&r112=" + (chbR112.isChecked()?1:0);
                filtroInicio += "&r113=" + (chbR113.isChecked()?1:0);
                filtroInicio += "&r114=" + (chbR114.isChecked()?1:0);
                filtroInicio += "&r115=" + (chbR115.isChecked()?1:0);
                filtroInicio += "&r116=" + (chbR116.isChecked()?1:0);
                filtroInicio += "&r117=" + (chbR117.isChecked()?1:0);
                filtroInicio += "&r118=" + (chbR118.isChecked()?1:0);
                filtroInicio += "&r119=" + (chbR119.isChecked()?1:0);
                filtroInicio += "&r12=" + URLEncoder.encode(etR12.getText().toString(), "UTF-8");

                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_adopcion.php" +filtroInicio ;
                System.out.println("direccion ------      " + url);

                new Fase2Task().execute(new URL(url) );
            } else {
                Toast.makeText(this, "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public class Fase2Task extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(TestAdopcion.this, "", "Guardando FASE 2 : TEST...");
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

                    Boolean rpta = Boolean.valueOf(obj.getString("success").toString());
                    if (rpta){
                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("msg",obj.getString("msg").toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getBaseContext(),"Ocurrió un error de parsing Json en FASE 2",
                        Toast.LENGTH_SHORT).show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }
}
