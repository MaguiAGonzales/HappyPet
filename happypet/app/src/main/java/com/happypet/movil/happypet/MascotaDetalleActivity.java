package com.happypet.movil.happypet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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
import android.widget.Toast;

import clases.DialogoAlerta;

public class MascotaDetalleActivity extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_detalle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        MASCOTA_ID = getIntent().getStringExtra("id");

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
                Toast.makeText(MascotaDetalleActivity.this, "muestra los terminso y condiciones", Toast.LENGTH_LONG).show();
            }
        });

        final Button btnAdoptar = (Button)findViewById(R.id.btnAdoptarMascota);
        btnAdoptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoAlerta dialogo = new DialogoAlerta();
                dialogo.show(fragmentManager, "tagAlerta");
            }
        });

        final CheckBox chbTerminos = (CheckBox)findViewById(R.id.cb_detalle_terminosYcondiciones);

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
        Toast.makeText(MascotaDetalleActivity.this, "abrimos la informacion del usuario", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MascotaDetalleActivity.this, UsuarioDetalle.class);
        intent.putExtra("idUsuario", USUARIO_ID);
        intent.putExtra("id", MASCOTA_ID);
        startActivity(intent);
    }





}