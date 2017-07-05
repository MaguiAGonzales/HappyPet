package com.happypet.movil.happypet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class ProcesoAdopcion extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID, MASCOTA_FOTO, MASCOTA_NOMBRE, MASCOTA_TIPO, MASCOTA_SEXO, MASCOTA_PARTICULARIDAD, MASCOTA_SALUD, MASCOTA_EDAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_adopcion);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        MASCOTA_ID = getIntent().getStringExtra("idMascota");

        MASCOTA_FOTO = getIntent().getStringExtra("foto");
        MASCOTA_NOMBRE = getIntent().getStringExtra("nombre");
        MASCOTA_TIPO = getIntent().getStringExtra("tipo");
        MASCOTA_SEXO = getIntent().getStringExtra("sexo");
        MASCOTA_PARTICULARIDAD = getIntent().getStringExtra("particularidad");
        MASCOTA_SALUD = getIntent().getStringExtra("salud");
        MASCOTA_EDAD = getIntent().getStringExtra("edad");

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
    }
}
