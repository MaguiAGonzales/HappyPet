package com.happypet.movil.happypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import clases.DialogoAlerta;
import fragmentos.AdopcionesFragment;

public class ProcesoAdopcion extends AppCompatActivity {

    String USUARIO_ID, ADOPCION_ID;
    String MASCOTA_ID, MASCOTA_FOTO, MASCOTA_NOMBRE, MASCOTA_TIPO, MASCOTA_SEXO, MASCOTA_PARTICULARIDAD, MASCOTA_SALUD, MASCOTA_EDAD;
    String ESTADO;

    String VISITA_FECHA, VISITA_HORA, VISITA_DESCRIPCION;

    Button btnFase1, btnFase2, btnFase3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_adopcion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        ADOPCION_ID = getIntent().getStringExtra("idAdopcion");
        MASCOTA_ID = getIntent().getStringExtra("idMascota");

        MASCOTA_FOTO = getIntent().getStringExtra("foto");
        MASCOTA_NOMBRE = getIntent().getStringExtra("nombre");
        MASCOTA_TIPO = getIntent().getStringExtra("tipo");
        MASCOTA_SEXO = getIntent().getStringExtra("sexo");
        MASCOTA_PARTICULARIDAD = getIntent().getStringExtra("particularidad");
        MASCOTA_SALUD = getIntent().getStringExtra("salud");
        MASCOTA_EDAD = getIntent().getStringExtra("edad");

        String fechaVisita = getIntent().getStringExtra("fecha");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        try {
            Date fechaInicio = format.parse(fechaVisita);
            format = new SimpleDateFormat("dd/MM/yyyy");
            fechaVisita = format.format(fechaInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        VISITA_FECHA = fechaVisita;


        String horaVisita = getIntent().getStringExtra("hora");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        try {
            Date horaInicio = formatHora.parse(horaVisita);
            formatHora = new SimpleDateFormat("hh:mm a");
            horaVisita = formatHora.format(horaInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        VISITA_HORA = horaVisita;

        VISITA_DESCRIPCION = getIntent().getStringExtra("descripcion");

        ESTADO = getIntent().getStringExtra("estado");
        System.out.println("ESTADO -->  " + ESTADO);

        ImageView foto = (ImageView)findViewById(R.id.iv_detalle_FotoMascota);
        byte[] decodedString = Base64.decode(MASCOTA_FOTO, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        foto.setImageBitmap(decodedByte);

        TextView tbNombre = (TextView)findViewById(R.id.tv_pa_nombreMascota);
        tbNombre.setText(MASCOTA_NOMBRE);

        TextView tbTipo = (TextView)findViewById(R.id.tv_pa_tipoMascota);
        tbTipo.setText(MASCOTA_TIPO);

        TextView tbSexo = (TextView)findViewById(R.id.tv_pa_sexoMascota);
        tbSexo.setText(MASCOTA_SEXO);

        TextView tbEdad = (TextView)findViewById(R.id.tv_pa_edadMascota);
        tbEdad.setText(MASCOTA_EDAD + " años");

        TextView tbParticula = (TextView)findViewById(R.id.tv_pa_particularidadesMascota);
        tbParticula.setText(MASCOTA_PARTICULARIDAD);

        TextView tbSalud = (TextView)findViewById(R.id.tv_pa_saludMascota);
        tbSalud.setText(MASCOTA_SALUD);

        btnFase1 = (Button) findViewById(R.id.btn_pa_fase1);
        btnFase2 = (Button) findViewById(R.id.btn_pa_fase2);
        btnFase3 = (Button) findViewById(R.id.btn_pa_fase3);

        habilitarBotonesEstado(ESTADO);

        btnFase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TestAdopcion.class);
                intent.putExtra("idUsuario", USUARIO_ID);
                intent.putExtra("idAdopcion", ADOPCION_ID);
                intent.putExtra("idMascota", MASCOTA_ID);
                startActivityForResult(intent, 2);
            }
        });


        btnFase3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String fecha = "", hora = "", descripcion = "";

                AlertDialog.Builder dialog = new AlertDialog.Builder(ProcesoAdopcion.this);
                dialog.setCancelable(false);
                dialog.setTitle("Fecha de Visita Programada");
                dialog.setMessage("Día : " + VISITA_FECHA + "\n" + "Hora :" + VISITA_HORA + "\n" + "Descripción :" + VISITA_DESCRIPCION);
//                dialog.setMessage("Día : " + fecha + "\n" + "Hora :" + hora + "\n" + "Descripción :" + descripcion);
                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

//
//    @Override
//    public void onBackPressed() {
//
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        MenuItem item = navigationView.getMenu().getItem(1);
//        item.setChecked(true);
//        getSupportActionBar().setTitle(item.getTitle());
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                habilitarBotonesEstado("F2");
//                String result=data.getStringExtra("msg");
//                Toast.makeText(this, "Se tiene que guardarel test" , Toast.LENGTH_LONG).show();
            }else {

            }
        }
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

    private void habilitarBotonesEstado(String estado){

        switch(estado) {
            case "F1":
                btnFase1.setText("COMPLETO");
                btnFase1.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase1.setEnabled(false);

                btnFase2.setText("PENDIENTE");
                btnFase3.setText("PENDIENTE");
                btnFase3.setEnabled(false);
                break;

            case "F2":
                btnFase1.setText("COMPLETO");
                btnFase1.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase1.setEnabled(false);

                btnFase2.setText("COMPLETO");
                btnFase2.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase2.setEnabled(false);

                btnFase3.setText("PENDIENTE");
                break;

            case "F3":
                btnFase1.setText("COMPLETO");
                btnFase1.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase1.setEnabled(false);

                btnFase2.setText("COMPLETO");
                btnFase2.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase2.setEnabled(false);

                btnFase3.setText("ESPERANDO FECHA");
                btnFase3.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase3.setEnabled(false);
                break;

            case "TE":
                btnFase1.setText("COMPLETO");
                btnFase1.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase1.setEnabled(false);

                btnFase2.setText("COMPLETO");
                btnFase2.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase2.setEnabled(false);

                btnFase3.setText("COMPLETO");
                btnFase3.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase3.setEnabled(true);
                break;
        }
    }




}
