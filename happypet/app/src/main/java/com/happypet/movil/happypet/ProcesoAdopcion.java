package com.happypet.movil.happypet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import clases.DialogoAlerta;

public class ProcesoAdopcion extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID, MASCOTA_FOTO, MASCOTA_NOMBRE, MASCOTA_TIPO, MASCOTA_SEXO, MASCOTA_PARTICULARIDAD, MASCOTA_SALUD, MASCOTA_EDAD;
    String ESTADO;

    Button btnFase1, btnFase2, btnFase3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_adopcion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        MASCOTA_ID = getIntent().getStringExtra("idMascota");

        MASCOTA_FOTO = getIntent().getStringExtra("foto");
        MASCOTA_NOMBRE = getIntent().getStringExtra("nombre");
        MASCOTA_TIPO = getIntent().getStringExtra("tipo");
        MASCOTA_SEXO = getIntent().getStringExtra("sexo");
        MASCOTA_PARTICULARIDAD = getIntent().getStringExtra("particularidad");
        MASCOTA_SALUD = getIntent().getStringExtra("salud");
        MASCOTA_EDAD = getIntent().getStringExtra("edad");

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

        switch(ESTADO) {
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

                btnFase3.setText("COMPLETO");
                btnFase3.setBackgroundColor(Color.argb(255,0,166,90));
                btnFase3.setEnabled(false);
                break;
        }

        btnFase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TestAdopcion.class);
                intent.putExtra("idUsuario", USUARIO_ID);
                intent.putExtra("idMascota", MASCOTA_ID);
//                intent.putExtra("foto", mascotaSel.getImagen());
//                intent.putExtra("nombre", mascotaSel.getNombre());
//                intent.putExtra("tipo", mascotaSel.getTipo());
//                intent.putExtra("sexo", mascotaSel.getSexo());
//                intent.putExtra("particularidad", mascotaSel.getParticularidades());
//                intent.putExtra("salud", mascotaSel.getSalud());
//                intent.putExtra("edad", String.valueOf(mascotaSel.getEdad()) );
                startActivityForResult(intent, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                String result=data.getStringExtra("result");
                Toast.makeText(this, "Se tiene que guardarel test" , Toast.LENGTH_LONG).show();
            }else {

            }
        }
    }

}
