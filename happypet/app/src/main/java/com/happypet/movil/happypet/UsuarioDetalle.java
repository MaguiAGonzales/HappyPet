package com.happypet.movil.happypet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UsuarioDetalle extends AppCompatActivity {

    String USUARIO_ID;
    String MASCOTA_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");
        MASCOTA_ID = getIntent().getStringExtra("id");
    }
}
