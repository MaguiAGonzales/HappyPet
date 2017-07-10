package com.happypet.movil.happypet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLOutput;

import clases.DialogoAlerta;

public class MascotaDetalleActivity extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID, MASCOTA_FOTO, MASCOTA_NOMBRE, MASCOTA_TIPO, MASCOTA_SEXO, MASCOTA_PARTICULARIDAD, MASCOTA_SALUD, MASCOTA_EDAD;


    public CheckBox chbTerminos;
    public Button btnAdoptar;

    static final int TERMINOS_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_detalle);

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

        ImageView foto = (ImageView)findViewById(R.id.iv_detalle_FotoMascota);
        byte[] decodedString = Base64.decode(getIntent().getStringExtra("foto"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        foto.setImageBitmap(decodedByte);

        TextView tbNombre = (TextView)findViewById(R.id.tv_detalle_nombreMascota);
        tbNombre.setText(getIntent().getStringExtra("nombre"));

        TextView tbTipo = (TextView)findViewById(R.id.tv_detalle_tipoMascota);
        tbTipo.setText(getIntent().getStringExtra("tipo"));

        TextView tbSexo = (TextView)findViewById(R.id.tv_detalle_sexoMascota);
        tbSexo.setText(getIntent().getStringExtra("sexo"));

        TextView tbEdad = (TextView)findViewById(R.id.tv_detalle_edadMascota);
        tbEdad.setText(getIntent().getStringExtra("edad") + " años");

        TextView tbParticula = (TextView)findViewById(R.id.tv_detalle_particularidadesMascota);
        tbParticula.setText(getIntent().getStringExtra("particularidad"));

        TextView tbSalud = (TextView)findViewById(R.id.tv_detalle_saludMascota);
        tbSalud.setText(getIntent().getStringExtra("salud"));

        TextView tbAbrirTerminos = (TextView)findViewById(R.id.tv_detalle_terminosYcondiciones);
        tbAbrirTerminos.setPaintFlags(tbAbrirTerminos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tbAbrirTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MascotaDetalleActivity.this, TerminosYCondicionesAdopcion.class);
                startActivityForResult(intent, TERMINOS_REQUEST);
            }
        });

        btnAdoptar = (Button)findViewById(R.id.btnAdoptarMascota);

        btnAdoptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoAlerta dialogo = new DialogoAlerta();
                dialogo.show(fragmentManager, "tagAlerta");
            }
        });

        chbTerminos = (CheckBox)findViewById(R.id.cb_detalle_terminosYcondiciones);

        chbTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdoptar.setEnabled(((CheckBox) v).isChecked());
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

    public void PrimeraFaseClick(){
        Intent intent = new Intent(MascotaDetalleActivity.this, UsuarioDetalleActivity.class);
        intent.putExtra("idUsuario", USUARIO_ID);
        intent.putExtra("id", MASCOTA_ID);

        intent.putExtra("foto", MASCOTA_FOTO);
        intent.putExtra("nombre", MASCOTA_NOMBRE);
        intent.putExtra("tipo", MASCOTA_TIPO);
        intent.putExtra("sexo", MASCOTA_SEXO);
        intent.putExtra("particularidad", MASCOTA_PARTICULARIDAD);
        intent.putExtra("salud", MASCOTA_SALUD);
        intent.putExtra("edad", MASCOTA_EDAD);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TERMINOS_REQUEST) {
            if (resultCode == RESULT_OK) {
//                String result=data.getStringExtra("result");
                chbTerminos.setChecked(true);
                btnAdoptar.setEnabled(true);
            }else {
                chbTerminos.setChecked(false);
                btnAdoptar.setEnabled(false);
            }
        }
    }





}