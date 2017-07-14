package com.happypet.movil.happypet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MascotaDetalle2Activity extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID, MASCOTA_FOTO, MASCOTA_NOMBRE, MASCOTA_TIPO, MASCOTA_SEXO, MASCOTA_PARTICULARIDAD;
    String MASCOTA_SALUD, MASCOTA_TAMANIO, MASCOTA_NACIMIENTO, MASCOTA_EDAD, MASCOTA_ADOPTADO, MASCOTA_ESTERELIZADO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_detalle2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");

        MASCOTA_ID = getIntent().getStringExtra("id");
        MASCOTA_FOTO = getIntent().getStringExtra("foto");
        MASCOTA_NOMBRE = getIntent().getStringExtra("nombre");
        MASCOTA_TIPO = getIntent().getStringExtra("tipo");
        MASCOTA_SEXO = getIntent().getStringExtra("sexo");
        MASCOTA_PARTICULARIDAD = getIntent().getStringExtra("particularidad");
        MASCOTA_SALUD = getIntent().getStringExtra("salud");
        MASCOTA_TAMANIO = getIntent().getStringExtra("tamanio");
        MASCOTA_NACIMIENTO = getIntent().getStringExtra("nacimiento");
        MASCOTA_EDAD = getIntent().getStringExtra("edad");
        MASCOTA_ADOPTADO = getIntent().getStringExtra("adoptado");
        MASCOTA_ESTERELIZADO = getIntent().getStringExtra("esterelizado");

        ImageView foto = (ImageView)findViewById(R.id.iv_detalle2_FotoMascota);
        byte[] decodedString = Base64.decode(getIntent().getStringExtra("foto"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        foto.setImageBitmap(decodedByte);

        TextView tbNombre = (TextView)findViewById(R.id.tv_detalle2_nombreMascota);
        tbNombre.setText(getIntent().getStringExtra("nombre"));

        TextView tbTipo = (TextView)findViewById(R.id.tv_detalle2_tipoMascota);
        tbTipo.setText(getIntent().getStringExtra("tipo"));

        TextView tbSexo = (TextView)findViewById(R.id.tv_detalle2_sexoMascota);
        tbSexo.setText(getIntent().getStringExtra("sexo"));

        TextView tbNacimiento = (TextView)findViewById(R.id.tv_detalle2_NacimientoMascota);
        tbNacimiento.setText(getIntent().getStringExtra("nacimiento"));

        TextView tbEdad = (TextView)findViewById(R.id.tv_detalle2_edadMascota);
        tbEdad.setText(getIntent().getStringExtra("edad") + " años");

        TextView tbParticula = (TextView)findViewById(R.id.tv_detalle2_particularidadesMascota);
        tbParticula.setText(getIntent().getStringExtra("particularidad"));

        TextView tbSalud = (TextView)findViewById(R.id.tv_detalle2_saludMascota);
        tbSalud.setText(getIntent().getStringExtra("salud"));

        TextView tbTamanio = (TextView)findViewById(R.id.tv_detalle2_tamanioMascota);
        tbTamanio.setText(getIntent().getStringExtra("tamanio"));

        TextView tbAdoptado = (TextView)findViewById(R.id.tv_detalle2_AdoptadoMascota);
        tbAdoptado.setText(getIntent().getStringExtra("adoptado").equals("1")?"SI":"NO");

        TextView tbEsterelizado = (TextView)findViewById(R.id.tv_detalle2_esterilizadoMascota);
        tbEsterelizado.setText(getIntent().getStringExtra("esterelizado").equals("1")?"SI":"NO");
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
}
